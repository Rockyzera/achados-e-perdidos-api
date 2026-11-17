package com.achadoseperdidos.controller;

import com.achadoseperdidos.dto.ItemRequest;
import com.achadoseperdidos.dto.ItemResponse;
import com.achadoseperdidos.dto.StatusItemRequest;
import com.achadoseperdidos.service.ItemService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Itens", description = "Cadastro e acompanhamento de itens perdidos ou encontrados.")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "Listar itens", description = "Retorna todos os itens cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de itens retornada.")
    public ResponseEntity<List<ItemResponse>> listar() {
        return ResponseEntity.ok(itemService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item", description = "Busca um item pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado.")
    })
    public ResponseEntity<ItemResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.buscarPorId(id));
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar item perdido ou encontrado",
            description = "Cria um item associado a uma categoria e a um local existentes.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = ItemRequest.class), examples = @ExampleObject(
                            value = "{\"nome\":\"Carteira\",\"descricao\":\"Carteira marrom com documentos\",\"tipo\":\"ENCONTRADO\",\"dataOcorrencia\":\"2026-11-16\",\"nomeContato\":\"Maria Silva\",\"emailContato\":\"maria@example.com\",\"categoriaId\":1,\"localId\":1}"
                    ))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item criado.", content = @Content(
                    schema = @Schema(implementation = ItemResponse.class),
                    examples = @ExampleObject(value = "{\"id\":1,\"nome\":\"Carteira\",\"descricao\":\"Carteira marrom com documentos\",\"tipo\":\"ENCONTRADO\",\"status\":\"ABERTO\",\"dataOcorrencia\":\"2026-11-16\",\"nomeContato\":\"Maria Silva\",\"emailContato\":\"maria@example.com\",\"dataCadastro\":\"2026-11-16T10:00:00\",\"categoria\":{\"id\":1,\"nome\":\"Documentos\",\"descricao\":null},\"local\":{\"id\":1,\"nome\":\"Biblioteca\",\"detalhes\":null,\"cidade\":\"Jales\",\"estado\":\"SP\"}}")
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "404", description = "Categoria ou local não encontrado.")
    })
    public ResponseEntity<ItemResponse> criar(@Valid @RequestBody ItemRequest requisicao) {
        ItemResponse item = itemService.criar(requisicao);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(item.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(item);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item", description = "Substitui os dados de um item existente, sem alterar seu status.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "404", description = "Item, categoria ou local não encontrado.")
    })
    public ResponseEntity<ItemResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest requisicao
    ) {
        return ResponseEntity.ok(itemService.atualizar(id, requisicao));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Alterar status do item",
            description = "Transições permitidas: ABERTO para EM_CONTATO ou CANCELADO; EM_CONTATO para RESOLVIDO ou CANCELADO.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = StatusItemRequest.class),
                            examples = @ExampleObject(value = "{\"status\":\"EM_CONTATO\"}")
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado."),
            @ApiResponse(responseCode = "400", description = "Status inválido ou corpo malformado."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado."),
            @ApiResponse(responseCode = "409", description = "Transição de status não permitida.")
    })
    public ResponseEntity<ItemResponse> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusItemRequest requisicao
    ) {
        return ResponseEntity.ok(itemService.alterarStatus(id, requisicao.status()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item", description = "Remove um item pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item excluído."),
            @ApiResponse(responseCode = "404", description = "Item não encontrado.")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        itemService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
