package br.com.contabancaria.dto.request;

import br.com.contabancaria.model.TipoConta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ContaRequest {

    @NotBlank
    private String numero;

    @NotNull
    private TipoConta tipo;

    @NotNull
    private Long correntistaId;

    @PositiveOrZero
    private BigDecimal limite;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public Long getCorrentistaId() {
        return correntistaId;
    }

    public void setCorrentistaId(Long correntistaId) {
        this.correntistaId = correntistaId;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }
}
