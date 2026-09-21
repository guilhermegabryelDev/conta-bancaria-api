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
}
