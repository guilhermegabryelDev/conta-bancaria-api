package br.com.contabancaria.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CorrentistaRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String documento;

    @Valid
    @NotNull
    private DadosContatoRequest dadosContato;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public DadosContatoRequest getDadosContato() {
        return dadosContato;
    }

    public void setDadosContato(DadosContatoRequest dadosContato) {
        this.dadosContato = dadosContato;
    }
}
