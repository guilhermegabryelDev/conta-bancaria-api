package br.com.contabancaria.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Correntista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String documento;

    @Embedded
    private DadosContato dadosContato;

    @OneToMany(mappedBy = "correntista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conta> contas = new ArrayList<>();

    protected Correntista() {
    }

    public Correntista(String nome, String documento, DadosContato dadosContato) {
        this.nome = nome;
        this.documento = documento;
        this.dadosContato = dadosContato;
    }

    public Long getId() {
        return id;
    }

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

    public DadosContato getDadosContato() {
        return dadosContato;
    }

    public void setDadosContato(DadosContato dadosContato) {
        this.dadosContato = dadosContato;
    }

    public List<Conta> getContas() {
        return contas;
    }
}
