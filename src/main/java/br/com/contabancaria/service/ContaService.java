package br.com.contabancaria.service;

import br.com.contabancaria.dto.request.ContaRequest;
import br.com.contabancaria.exception.RecursoNaoEncontradoException;
import br.com.contabancaria.model.Conta;
import br.com.contabancaria.model.ContaCorrente;
import br.com.contabancaria.model.ContaPoupanca;
import br.com.contabancaria.model.Correntista;
import br.com.contabancaria.model.TipoConta;
import br.com.contabancaria.model.TipoTransacao;
import br.com.contabancaria.model.Transacao;
import br.com.contabancaria.repository.ContaRepository;
import br.com.contabancaria.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ContaService {

	private final ContaRepository contaRepository;
	private final TransacaoRepository transacaoRepository;
	private final CorrentistaService correntistaService;

	public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository,
			CorrentistaService correntistaService) {
		this.contaRepository = contaRepository;
		this.transacaoRepository = transacaoRepository;
		this.correntistaService = correntistaService;
	}

	public Conta abrir(ContaRequest request) {
		Correntista correntista = correntistaService.buscar(request.getCorrentistaId());
		if (request.getTipo() == TipoConta.CORRENTE) {
			if (request.getLimite() == null) {
				throw new IllegalArgumentException("O limite e obrigatorio para conta corrente");
			}
			return contaRepository.save(new ContaCorrente(request.getNumero(), correntista, request.getLimite()));
		}
		if (request.getLimite() != null) {
			throw new IllegalArgumentException("Conta poupanca nao aceita limite");
		}
		return contaRepository.save(new ContaPoupanca(request.getNumero(), correntista));
	}

	public Conta buscar(Long contaId) {
		return contaRepository.findById(contaId)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Conta nao encontrada: " + contaId));
	}

	public List<Conta> listar() {
		return contaRepository.findAll();
	}

	public List<Transacao> listarTransacoes(Long contaId) {
		buscar(contaId);
		return transacaoRepository.findByContaOrigemIdOrderByDataDesc(contaId);
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

	@Transactional
	public void aplicarRendimento(Long contaId, BigDecimal taxa) {
		Conta conta = buscarConta(contaId);
		BigDecimal rendimento = conta.aplicarRendimento(taxa);
		registrarTransacao(conta, TipoTransacao.RENDIMENTO, rendimento);
	}

	@Transactional
	public void aplicarJuros(Long contaId, BigDecimal taxa) {
		Conta conta = buscarConta(contaId);
		BigDecimal juros = conta.aplicarJuros(taxa);
		registrarTransacao(conta, TipoTransacao.JUROS, juros);
	}

	private Conta buscarConta(Long contaId) {
		return buscar(Objects.requireNonNull(contaId, "O id da conta e obrigatorio"));
	}

	private void registrarTransacao(Conta conta, TipoTransacao tipo, BigDecimal valor) {
		Transacao transacao = new Transacao(tipo, valor, LocalDateTime.now(), conta);
		conta.adicionarTransacao(transacao);
		transacaoRepository.save(transacao);
		contaRepository.save(conta);
	}
}
