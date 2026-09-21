package br.com.contabancaria.dto.response;

import br.com.contabancaria.model.Conta;
import br.com.contabancaria.model.ContaCorrente;

import java.math.BigDecimal;

public class ContaResponse {

    private final Long id;
    private final String numero;
    private final BigDecimal saldo;
    private final String tipo;
    private final BigDecimal limite;
    private final Long correntistaId;

    private ContaResponse(Long id, String numero, BigDecimal saldo, String tipo, BigDecimal limite, Long correntistaId) {
        this.id = id;
        this.numero = numero;
        this.saldo = saldo;
        this.tipo = tipo;
        this.limite = limite;
        this.correntistaId = correntistaId;
    }

    public static ContaResponse from(Conta conta) {
        BigDecimal limite = conta instanceof ContaCorrente ? ((ContaCorrente) conta).getLimite() : null;
        return new ContaResponse(conta.getId(), conta.getNumero(), conta.getSaldo(), conta.getTipo().name(), limite,
                conta.getCorrentista().getId());
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public Long getCorrentistaId() {
        return correntistaId;
    }
}
