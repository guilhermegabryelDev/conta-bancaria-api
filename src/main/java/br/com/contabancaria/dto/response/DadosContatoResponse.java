package br.com.contabancaria.dto.response;

import br.com.contabancaria.model.DadosContato;

public class DadosContatoResponse {

    private final String email;
    private final String telefone;
    private final String endereco;

    private DadosContatoResponse(String email, String telefone, String endereco) {
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public static DadosContatoResponse from(DadosContato dadosContato) {
        return new DadosContatoResponse(dadosContato.getEmail(), dadosContato.getTelefone(), dadosContato.getEndereco());
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }
}
