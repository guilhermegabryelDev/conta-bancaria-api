package br.com.contabancaria.model;

import javax.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class ContaPoupanca extends Conta {

    protected ContaPoupanca() {
    }

    public ContaPoupanca(String numero, Correntista correntista) {
        super(numero, TipoConta.POUPANCA, correntista);
    }

    @Override
    protected boolean saquePermitido(BigDecimal valor) {
        return getSaldo().compareTo(valor) >= 0;
    }

    @Override
    public BigDecimal aplicarRendimento(BigDecimal taxa) {
        validarTaxa(taxa);
        BigDecimal rendimento = getSaldo().multiply(taxa).divide(BigDecimal.valueOf(100));
        definirSaldo(getSaldo().add(rendimento));
        return rendimento;
    }
}
