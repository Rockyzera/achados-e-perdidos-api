package com.achadoseperdidos.controller;

import com.achadoseperdidos.dto.LocalRequest;
import com.achadoseperdidos.dto.LocalResponse;
import com.achadoseperdidos.service.LocalService;
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
@Tag(name = "Locais", description = "Lugares onde itens foram perdidos ou encontrados.")
public class LocalController {

    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @GetMapping
    @Operation(summary = "Listar locais", description = "Retorna todos os locais cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de locais retornada.")
    public ResponseEntity<List<LocalResponse>> listar() {
        return ResponseEntity.ok(localService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar local", description = "Busca um local pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Local encontrado."),
            @ApiResponse(responseCode = "404", description = "Local não encontrado.")
    })
    public ResponseEntity<LocalResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localService.buscarPorId(id));
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar local",
            description = "Registra um local com cidade e estado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = LocalRequest.class), examples = @ExampleObject(
                            value = "{\"nome\":\"Biblioteca\",\"detalhes\":\"Entrada principal\",\"cidade\":\"Jales\",\"estado\":\"SP\"}"
                    ))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Local criado.", content = @Content(
                    schema = @Schema(implementation = LocalResponse.class),
                    examples = @ExampleObject(value = "{\"id\":1,\"nome\":\"Biblioteca\",\"detalhes\":\"Entrada principal\",\"cidade\":\"Jales\",\"estado\":\"SP\"}")
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos.")
    })
    public ResponseEntity<LocalResponse> criar(@Valid @RequestBody LocalRequest requisicao) {
        LocalResponse local = localService.criar(requisicao);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(local.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(local);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar local", description = "Substitui os dados de um local existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Local atualizado."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "404", description = "Local não encontrado.")
    })
    public ResponseEntity<LocalResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LocalRequest requisicao
    ) {
        return ResponseEntity.ok(localService.atualizar(id, requisicao));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir local", description = "Remove um local que não esteja associado a itens.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Local excluído."),
            @ApiResponse(responseCode = "404", description = "Local não encontrado."),
            @ApiResponse(responseCode = "409", description = "O local está associado a itens.")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        localService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
