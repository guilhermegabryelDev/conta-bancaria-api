package br.com.contabancaria.repository;

import br.com.contabancaria.model.Correntista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {
}
