package com.achadoseperdidos.controller;

import com.achadoseperdidos.dto.ItemRequest;
import com.achadoseperdidos.dto.ItemResponse;
import com.achadoseperdidos.dto.StatusItemRequest;
import com.achadoseperdidos.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> listar() {
        return ResponseEntity.ok(itemService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ItemResponse> criar(@Valid @RequestBody ItemRequest requisicao) {
        ItemResponse item = itemService.criar(requisicao);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest requisicao
    ) {
        return ResponseEntity.ok(itemService.atualizar(id, requisicao));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ItemResponse> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusItemRequest requisicao
    ) {
        return ResponseEntity.ok(itemService.alterarStatus(id, requisicao.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        itemService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
