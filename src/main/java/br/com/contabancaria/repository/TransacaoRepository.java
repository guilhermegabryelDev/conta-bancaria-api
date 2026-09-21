package br.com.contabancaria.repository;

import br.com.contabancaria.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

	List<Transacao> findByContaOrigemIdOrderByDataDesc(Long contaId);
}
