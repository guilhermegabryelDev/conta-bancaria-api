package br.com.contabancaria.dto.request;

import javax.validation.constraints.Email;

public class DadosContatoRequest {

    @Email
    private String email;
    private String telefone;
    private String endereco;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
