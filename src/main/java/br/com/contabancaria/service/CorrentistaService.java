package br.com.contabancaria.service;

import br.com.contabancaria.dto.request.CorrentistaRequest;
import br.com.contabancaria.model.Correntista;
import br.com.contabancaria.model.DadosContato;
import br.com.contabancaria.repository.CorrentistaRepository;
import br.com.contabancaria.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CorrentistaService {

    private final CorrentistaRepository correntistaRepository;

    public CorrentistaService(CorrentistaRepository correntistaRepository) {
        this.correntistaRepository = correntistaRepository;
    }

    public Correntista cadastrar(CorrentistaRequest request) {
        DadosContato contato = new DadosContato();
        contato.setEmail(request.getDadosContato().getEmail());
        contato.setTelefone(request.getDadosContato().getTelefone());
        contato.setEndereco(request.getDadosContato().getEndereco());
        return correntistaRepository.save(new Correntista(request.getNome(), request.getDocumento(), contato));
    }

    public Correntista buscar(Long id) {
        return correntistaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Correntista nao encontrado: " + id));
    }

    public List<Correntista> listar() {
        return correntistaRepository.findAll();
    }
}