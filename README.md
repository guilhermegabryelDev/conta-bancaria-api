# conta-bancaria-api

API RESTful de conta bancária desenvolvida com Java 8, Spring Boot, Spring Data JPA e H2.

## Etapa 1

A estrutura inicial foi criada com as camadas `controller`, `service`, `repository` e `model`.
As contas usam herança JPA com a estratégia `JOINED`:

- `Conta` concentra número, saldo, tipo, correntista e transações;
- `ContaCorrente` adiciona o limite;
- `ContaPoupanca` representa a conta sem limite;
- `Correntista` pode possuir várias contas;
- `Transacao` registra tipo, valor, data e conta de origem.

## Execução

Com Maven instalado, execute:

```bash
mvn spring-boot:run
```

O banco H2 é criado em memória. A console fica disponível em `/h2-console`.