# Conta Bancária API

API RESTful de conta bancária para cadastro de correntistas, abertura de contas,
movimentações e consulta de extrato.

O projeto usa herança JPA com a estratégia `JOINED`: os dados comuns ficam na
tabela `conta`, enquanto os dados específicos ficam em `conta_corrente` e
`conta_poupanca`.

## Tecnologias

- Java 8
- Spring Boot 2.7.18
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation
- H2 em memória
- JUnit 5 e Mockito
- Maven

## Pré-requisitos

- JDK 8 instalado e configurado no `PATH`;
- Maven 3.6 ou superior;
- Git, caso o projeto seja clonado do repositório remoto.

## Executando localmente

Na raiz do projeto, execute:

```bash
mvn clean test
mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### Console H2

Acesse `http://localhost:8080/h2-console` e informe:

| Campo | Valor |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:contabancaria` |
| User Name | `sa` |
| Password | vazio |

O banco é criado em memória e destruído quando a aplicação é encerrada.
O Hibernate gera as tabelas automaticamente por causa de
`spring.jpa.hibernate.ddl-auto=create-drop`.

### Configuração opcional com MySQL

Para usar MySQL, substitua a dependência H2 por um driver MySQL no `pom.xml` e
configure as propriedades abaixo em um arquivo de profile, como
`application-mysql.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/contabancaria
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

Em um ambiente real, as credenciais devem ser fornecidas por variáveis de
ambiente ou por um gerenciador de segredos.

## Schema do banco

Nesta versão não existe `schema.sql`. O schema é gerado pelo Hibernate a cada
execução por `ddl-auto=create-drop`.

O SQL conceitual equivalente é:

```sql
CREATE TABLE correntista (
		id BIGINT AUTO_INCREMENT PRIMARY KEY,
		nome VARCHAR(255) NOT NULL,
		documento VARCHAR(255) NOT NULL UNIQUE,
		email VARCHAR(255),
		telefone VARCHAR(255),
		endereco VARCHAR(255)
);

CREATE TABLE conta (
		id BIGINT AUTO_INCREMENT PRIMARY KEY,
		numero VARCHAR(255) NOT NULL UNIQUE,
		saldo DECIMAL(15, 2) NOT NULL,
		tipo VARCHAR(255) NOT NULL,
		correntista_id BIGINT NOT NULL,
		FOREIGN KEY (correntista_id) REFERENCES correntista(id)
);

CREATE TABLE conta_corrente (
		id BIGINT PRIMARY KEY,
		limite DECIMAL(15, 2) NOT NULL,
		FOREIGN KEY (id) REFERENCES conta(id)
);

CREATE TABLE conta_poupanca (
		id BIGINT PRIMARY KEY,
		FOREIGN KEY (id) REFERENCES conta(id)
);

CREATE TABLE transacao (
		id BIGINT AUTO_INCREMENT PRIMARY KEY,
		tipo VARCHAR(255) NOT NULL,
		valor DECIMAL(15, 2) NOT NULL,
		data TIMESTAMP NOT NULL,
		conta_origem_id BIGINT NOT NULL,
		FOREIGN KEY (conta_origem_id) REFERENCES conta(id)
);
```

Os nomes e detalhes de tipos podem variar conforme o dialeto do banco e a
versão do Hibernate; em produção, o ideal é substituir `ddl-auto` por uma
ferramenta de migração, como Flyway ou Liquibase.

## Endpoints

### Correntistas

#### Cadastrar correntista

`POST /correntistas`
Status: `201 Created`

Request:

```json
{
	"nome": "Maria Silva",
	"documento": "12345678900",
	"dadosContato": {
		"email": "maria@example.com",
		"telefone": "11999999999",
		"endereco": "Rua A, 100"
	}
}
```

Response:

```json
{
	"id": 1,
	"nome": "Maria Silva",
	"documento": "12345678900",
	"dadosContato": {
		"email": "maria@example.com",
		"telefone": "11999999999",
		"endereco": "Rua A, 100"
	}
}
```

#### Consultar correntista

`GET /correntistas/{id}`
Status: `200 OK` ou `404 Not Found`.

Usando `GET /correntistas/1`, a resposta tem o mesmo formato do cadastro.

#### Listar correntistas

`GET /correntistas`
Status: `200 OK`

Response:

```json
[
	{
		"id": 1,
		"nome": "Maria Silva",
		"documento": "12345678900",
		"dadosContato": {
			"email": "maria@example.com",
			"telefone": "11999999999",
			"endereco": "Rua A, 100"
		}
	}
]
```

### Contas

#### Abrir conta corrente

`POST /contas`
Status: `201 Created`.

Request:

```json
{
	"numero": "000123-4",
	"tipo": "CORRENTE",
	"correntistaId": 1,
	"limite": 500.00
}
```

Response:

```json
{
	"id": 1,
	"numero": "000123-4",
	"saldo": 0.00,
	"tipo": "CORRENTE",
	"limite": 500.00,
	"correntistaId": 1
}
```

#### Abrir conta poupança

`POST /contas`
Status: `201 Created`.

Request:

```json
{
	"numero": "000567-8",
	"tipo": "POUPANCA",
	"correntistaId": 1
}
```

Para poupança, o campo `limite` não deve ser enviado.

Response:

```json
{
	"id": 2,
	"numero": "000567-8",
	"saldo": 0.00,
	"tipo": "POUPANCA",
	"limite": null,
	"correntistaId": 1
}
```

#### Consultar conta

`GET /contas/{id}`
Status: `200 OK` ou `404 Not Found`.

#### Listar contas

`GET /contas`
Status: `200 OK`.

Response:

```json
[
	{
		"id": 1,
		"numero": "000123-4",
		"saldo": 0.00,
		"tipo": "CORRENTE",
		"limite": 500.00,
		"correntistaId": 1
	}
]
```

#### Depositar

`POST /contas/{id}/depositar`
Status: `200 OK` ou `400 Bad Request`.

Request:

```json
{
	"valor": 1000.00
}
```

Não há corpo na resposta. O saldo atualizado pode ser consultado em
`GET /contas/{id}`.

#### Sacar

`POST /contas/{id}/sacar`
Status: `200 OK`, `400 Bad Request` para saldo insuficiente ou `404 Not Found`.

Request:

```json
{
	"valor": 200.00
}
```

Conta corrente pode sacar até `saldo + limite`. Conta poupança não pode sacar
mais do que o saldo disponível.

#### Aplicar rendimento na poupança

`POST /contas/{id}/rendimento`
Status: `200 OK` ou `400 Bad Request`.

Request:

```json
{
	"taxa": 1.5
}
```

A taxa representa percentual. Para saldo de `1000.00` e taxa `1.5`, o
rendimento é `15.00`.

#### Aplicar juros na conta corrente

`POST /contas/{id}/juros`
Status: `200 OK` ou `400 Bad Request`.

Request:

```json
{
	"taxa": 10.0
}
```

Os juros só podem ser aplicados quando o saldo está negativo. A taxa representa
percentual sobre o valor absoluto do saldo negativo.

#### Consultar extrato

`GET /contas/{id}/transacoes`
Status: `200 OK` ou `404 Not Found`.

Response:

```json
[
	{
		"id": 10,
		"tipo": "DEPOSITO",
		"valor": 1000.00,
		"data": "2026-09-21T14:30:00"
	},
	{
		"id": 11,
		"tipo": "SAQUE",
		"valor": 200.00,
		"data": "2026-09-21T15:00:00"
	}
]
```

O extrato é retornado do mais recente para o mais antigo. Os tipos possíveis
são `DEPOSITO`, `SAQUE`, `RENDIMENTO` e `JUROS`.

### Erros

As exceções são convertidas por `@RestControllerAdvice` para o formato:

```json
{
	"timestamp": "2026-09-21T15:30:00",
	"status": 400,
	"error": "Bad Request",
	"message": "Saldo insuficiente para realizar o saque"
}
```

Principais status:

- `400 Bad Request`: payload inválido, valor/taxa inválidos, saldo insuficiente
	ou operação incompatível com o tipo da conta;
- `404 Not Found`: correntista ou conta inexistente;
- `201 Created`: cadastro de correntista e abertura de conta;
- `200 OK`: consultas e operações concluídas.

## Testes

Os testes unitários de `ContaService` usam JUnit 5 e Mockito. Repositories são
mockados, então não é necessário iniciar banco ou contexto Spring para validar
as regras de depósito, saque, limite, rendimento, juros e registro de
transações.

Execute:

```bash
mvn test
```

## O que foi feito, o que ficou de fora e por quê

### Implementado

- Cadastro e consulta de correntistas;
- Abertura e consulta de contas corrente e poupança;
- Herança JPA com `JOINED`;
- Depósito e saque com regras específicas por tipo de conta;
- Registro de todas as movimentações em `Transacao`;
- Rendimento mensal parametrizado para poupança;
- Juros parametrizados sobre saldo negativo de conta corrente;
- Testes unitários com JUnit e Mockito;
- DTOs para separar o contrato REST das entidades JPA;
- Tratamento global de erros com `@RestControllerAdvice`;
- Console H2 para desenvolvimento local.

### Não implementado

- **Swagger/OpenAPI:** não foi incluído nesta versão. Para adicionar, seria
	possível incluir `springdoc-openapi-ui`, configurar as informações da API e
	disponibilizar a documentação em `/swagger-ui.html`.
- **Autenticação e autorização:** o desafio não exigiu usuários ou segurança.
	Em produção, seria necessário adicionar Spring Security, autenticação por
	JWT/OAuth2 e regras de acesso por correntista.
- **Migrações versionadas:** o schema é gerado pelo Hibernate com
	`create-drop`, adequado apenas para desenvolvimento. Para entrega produtiva,
	seria melhor criar migrations com Flyway ou Liquibase.
- **Testes de integração HTTP:** a cobertura atual é unitária e isola os
	repositories com Mockito. Poderiam ser adicionados testes com
	`@SpringBootTest`, `MockMvc` e banco H2 para validar o fluxo completo.
- **Paginação, filtros e atualização de cadastros:** a primeira versão expõe
	apenas listagem completa e operações de criação/consulta, pois esses recursos
	não faziam parte do escopo solicitado.