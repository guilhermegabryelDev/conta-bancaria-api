package br.com.contabancaria.dto.response;

import br.com.contabancaria.model.TipoTransacao;
import br.com.contabancaria.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransacaoResponse {

    private final Long id;
    private final TipoTransacao tipo;
    private final BigDecimal valor;
    private final LocalDateTime data;

    private TransacaoResponse(Long id, TipoTransacao tipo, BigDecimal valor, LocalDateTime data) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public static TransacaoResponse from(Transacao transacao) {
        return new TransacaoResponse(transacao.getId(), transacao.getTipo(), transacao.getValor(), transacao.getData());
    }

    public Long getId() {
        return id;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getData() {
        return data;
    }
}
