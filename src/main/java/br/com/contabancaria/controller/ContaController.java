package br.com.contabancaria.controller;

import br.com.contabancaria.dto.request.ContaRequest;
import br.com.contabancaria.dto.request.MovimentacaoRequest;
import br.com.contabancaria.dto.response.ContaResponse;
import br.com.contabancaria.dto.response.TransacaoResponse;
import br.com.contabancaria.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/contas")
public class ContaController {

	private final ContaService service;

	public ContaController(ContaService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<ContaResponse> abrir(@Valid @RequestBody ContaRequest request) {
		ContaResponse response = ContaResponse.from(service.abrir(request));
		return ResponseEntity.created(URI.create("/contas/" + response.getId())).body(response);
	}

	@GetMapping("/{id}")
	public ContaResponse buscar(@PathVariable Long id) {
		return ContaResponse.from(service.buscar(id));
	}

	@GetMapping
	public List<ContaResponse> listar() {
		return service.listar().stream().map(ContaResponse::from).collect(Collectors.toList());
	}

	@PostMapping("/{id}/depositar")
	public ResponseEntity<Void> depositar(@PathVariable Long id, @Valid @RequestBody MovimentacaoRequest request) {
		service.depositar(id, request.getValor());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/sacar")
	public ResponseEntity<Void> sacar(@PathVariable Long id, @Valid @RequestBody MovimentacaoRequest request) {
		service.sacar(id, request.getValor());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/transacoes")
	public List<TransacaoResponse> listarTransacoes(@PathVariable Long id) {
		return service.listarTransacoes(id).stream().map(TransacaoResponse::from).collect(Collectors.toList());
	}
}
