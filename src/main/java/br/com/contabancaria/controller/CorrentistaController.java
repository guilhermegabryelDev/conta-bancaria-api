package br.com.contabancaria.controller;

import br.com.contabancaria.dto.request.CorrentistaRequest;
import br.com.contabancaria.dto.response.CorrentistaResponse;
import br.com.contabancaria.service.CorrentistaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@Validated
@RestController
@RequestMapping("/correntistas")
public class CorrentistaController {

    private final CorrentistaService service;

    public CorrentistaController(CorrentistaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CorrentistaResponse> cadastrar(@Valid @RequestBody CorrentistaRequest request) {
        CorrentistaResponse response = CorrentistaResponse.from(service.cadastrar(request));
        return ResponseEntity.created(URI.create("/correntistas/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public CorrentistaResponse buscar(@PathVariable Long id) {
        return CorrentistaResponse.from(service.buscar(id));
    }

    @GetMapping
    public List<CorrentistaResponse> listar() {
        return service.listar().stream().map(CorrentistaResponse::from).collect(Collectors.toList());
    }
}