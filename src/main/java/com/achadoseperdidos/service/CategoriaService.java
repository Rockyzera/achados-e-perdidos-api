package com.achadoseperdidos.service;

import com.achadoseperdidos.dto.CategoriaRequest;
import com.achadoseperdidos.dto.CategoriaResponse;
import com.achadoseperdidos.exception.RegraDeNegocioException;
import com.achadoseperdidos.exception.RecursoNaoEncontradoException;
import com.achadoseperdidos.model.Categoria;
import com.achadoseperdidos.repository.CategoriaRepository;
import com.achadoseperdidos.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ItemRepository itemRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, ItemRepository itemRepository) {
        this.categoriaRepository = categoriaRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(this::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return paraResposta(obterEntidade(id));
    }

    public CategoriaResponse criar(CategoriaRequest requisicao) {
        validarNomeUnico(requisicao.nome(), null);
        Categoria categoria = new Categoria();
        aplicar(requisicao, categoria);
        return paraResposta(categoriaRepository.save(categoria));
    }

    public CategoriaResponse atualizar(Long id, CategoriaRequest requisicao) {
        Categoria categoria = obterEntidade(id);
        validarNomeUnico(requisicao.nome(), id);
        aplicar(requisicao, categoria);
        return paraResposta(categoriaRepository.save(categoria));
    }

    public void excluir(Long id) {
        Categoria categoria = obterEntidade(id);
        if (itemRepository.existsByCategoria_Id(id)) {
            throw new RegraDeNegocioException("Não é possível excluir uma categoria que está associada a itens.");
        }
        categoriaRepository.delete(categoria);
    }

    private Categoria obterEntidade(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + id));
    }

    private void validarNomeUnico(String nome, Long idAtual) {
        boolean duplicado = idAtual == null
                ? categoriaRepository.existsByNomeIgnoreCase(nome)
                : categoriaRepository.existsByNomeIgnoreCaseAndIdNot(nome, idAtual);
        if (duplicado) {
            throw new RegraDeNegocioException("Já existe uma categoria com esse nome.");
        }
    }

    private void aplicar(CategoriaRequest requisicao, Categoria categoria) {
        categoria.setNome(requisicao.nome().trim());
        categoria.setDescricao(requisicao.descricao() == null ? null : requisicao.descricao().trim());
    }

    private CategoriaResponse paraResposta(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }
}
