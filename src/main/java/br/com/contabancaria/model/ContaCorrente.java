package br.com.contabancaria.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class ContaCorrente extends Conta {

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limite;

    protected ContaCorrente() {
    }

    public ContaCorrente(String numero, Correntista correntista, BigDecimal limite) {
        super(numero, TipoConta.CORRENTE, correntista);
        this.limite = limite;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public void setLimite(BigDecimal limite) {
        this.limite = limite;
    }

    @Override
    protected boolean saquePermitido(BigDecimal valor) {
        return getSaldo().add(limite).compareTo(valor) >= 0;
    }

    @Override
    public BigDecimal aplicarJuros(BigDecimal taxa) {
        validarTaxa(taxa);
        if (getSaldo().signum() >= 0) {
            throw new IllegalArgumentException("Juros podem ser aplicados somente sobre saldo negativo");
        }
        BigDecimal juros = getSaldo().abs().multiply(taxa).divide(BigDecimal.valueOf(100));
        definirSaldo(getSaldo().subtract(juros));
        return juros;
    }
}
