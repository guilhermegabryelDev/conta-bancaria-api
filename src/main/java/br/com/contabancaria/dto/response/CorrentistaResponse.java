package br.com.contabancaria.dto.response;

import br.com.contabancaria.model.Correntista;

public class CorrentistaResponse {

    private final Long id;
    private final String nome;
    private final String documento;
    private final DadosContatoResponse dadosContato;

    private CorrentistaResponse(Long id, String nome, String documento, DadosContatoResponse dadosContato) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.dadosContato = dadosContato;
    }

    public static CorrentistaResponse from(Correntista correntista) {
        return new CorrentistaResponse(correntista.getId(), correntista.getNome(), correntista.getDocumento(),
                DadosContatoResponse.from(correntista.getDadosContato()));
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public DadosContatoResponse getDadosContato() {
        return dadosContato;
    }
}
