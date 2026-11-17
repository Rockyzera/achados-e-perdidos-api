package com.achadoseperdidos.controller;

import com.achadoseperdidos.dto.CategoriaRequest;
import com.achadoseperdidos.dto.CategoriaResponse;
import com.achadoseperdidos.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Classificação dos itens perdidos e encontrados.")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Retorna todas as categorias cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada.")
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoria", description = "Busca uma categoria pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada.")
    })
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar categoria",
            description = "Cria uma categoria. O nome deve ser único.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = CategoriaRequest.class), examples = @ExampleObject(
                            value = "{\"nome\":\"Eletrônicos\",\"descricao\":\"Celulares, computadores e acessórios\"}"
                    ))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoria criada.", content = @Content(
                    schema = @Schema(implementation = CategoriaResponse.class),
                    examples = @ExampleObject(value = "{\"id\":1,\"nome\":\"Eletrônicos\",\"descricao\":\"Celulares, computadores e acessórios\"}")
            )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "409", description = "Já existe uma categoria com esse nome.")
    })
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody CategoriaRequest requisicao) {
        CategoriaResponse categoria = categoriaService.criar(requisicao);
        URI localizacao = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.id())
                .toUri();
        return ResponseEntity.created(localizacao).body(categoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Substitui os dados de uma categoria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria atualizada."),
            @ApiResponse(responseCode = "400", description = "Dados inválidos."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "409", description = "Já existe uma categoria com esse nome.")
    })
    public ResponseEntity<CategoriaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest requisicao
    ) {
        return ResponseEntity.ok(categoriaService.atualizar(id, requisicao));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria que não esteja associada a itens.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria excluída."),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada."),
            @ApiResponse(responseCode = "409", description = "A categoria está associada a itens.")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
