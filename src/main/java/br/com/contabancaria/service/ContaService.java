package br.com.contabancaria.service;

import br.com.contabancaria.model.Conta;
import br.com.contabancaria.model.TipoTransacao;
import br.com.contabancaria.model.Transacao;
import br.com.contabancaria.repository.ContaRepository;
import br.com.contabancaria.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ContaService {

	private final ContaRepository contaRepository;
	private final TransacaoRepository transacaoRepository;

	public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
		this.contaRepository = contaRepository;
		this.transacaoRepository = transacaoRepository;
	}

	@Transactional
	public void depositar(Long contaId, BigDecimal valor) {
		Conta conta = buscarConta(contaId);
		conta.depositar(valor);
		registrarTransacao(conta, TipoTransacao.DEPOSITO, valor);
	}

	@Transactional
	public void sacar(Long contaId, BigDecimal valor) {
		Conta conta = buscarConta(contaId);
		conta.sacar(valor);
		registrarTransacao(conta, TipoTransacao.SAQUE, valor);
	}

	private Conta buscarConta(Long contaId) {
		return contaRepository.findById(Objects.requireNonNull(contaId, "O id da conta e obrigatorio"))
				.orElseThrow(() -> new IllegalArgumentException("Conta nao encontrada: " + contaId));
	}

	private void registrarTransacao(Conta conta, TipoTransacao tipo, BigDecimal valor) {
		Transacao transacao = new Transacao(tipo, valor, LocalDateTime.now(), conta);
		conta.adicionarTransacao(transacao);
		transacaoRepository.save(transacao);
		contaRepository.save(conta);
	}
}
