package com.achadoseperdidos.controller;

import com.achadoseperdidos.dto.LocalRequest;
import com.achadoseperdidos.dto.LocalResponse;
import com.achadoseperdidos.service.LocalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/locais")
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @GetMapping
    public ResponseEntity<List<LocalResponse>> listar() {
        return ResponseEntity.ok(localService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<LocalResponse> criar(@Valid @RequestBody LocalRequest requisicao) {
        LocalResponse local = localService.criar(requisicao);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(local.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(local);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LocalRequest requisicao
    ) {
        return ResponseEntity.ok(localService.atualizar(id, requisicao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        localService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
