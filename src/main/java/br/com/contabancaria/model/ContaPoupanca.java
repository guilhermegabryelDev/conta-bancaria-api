package br.com.contabancaria.model;

import javax.persistence.Entity;

@Entity
public class ContaPoupanca extends Conta {

    protected ContaPoupanca() {
    }

    public ContaPoupanca(String numero, Correntista correntista) {
        super(numero, TipoConta.POUPANCA, correntista);
    }
}
