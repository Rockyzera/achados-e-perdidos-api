package com.achadoseperdidos.service;

import com.achadoseperdidos.dto.CategoriaResponse;
import com.achadoseperdidos.dto.ItemRequest;
import com.achadoseperdidos.dto.ItemResponse;
import com.achadoseperdidos.dto.LocalResponse;
import com.achadoseperdidos.exception.RegraDeNegocioException;
import com.achadoseperdidos.exception.RecursoNaoEncontradoException;
import com.achadoseperdidos.model.Categoria;
import com.achadoseperdidos.model.Item;
import com.achadoseperdidos.model.Local;
import com.achadoseperdidos.model.StatusItem;
import com.achadoseperdidos.repository.CategoriaRepository;
import com.achadoseperdidos.repository.ItemRepository;
import com.achadoseperdidos.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoriaRepository categoriaRepository;
    private final LocalRepository localRepository;

    public ItemService(ItemRepository itemRepository,
                       CategoriaRepository categoriaRepository,
                       LocalRepository localRepository) {
        this.itemRepository = itemRepository;
        this.categoriaRepository = categoriaRepository;
        this.localRepository = localRepository;
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> listar() {
        return itemRepository.findAll().stream().map(this::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public ItemResponse buscarPorId(Long id) {
        return paraResposta(obterEntidade(id));
    }

    public ItemResponse criar(ItemRequest requisicao) {
        Item item = new Item();
        aplicar(requisicao, item);
        return paraResposta(itemRepository.save(item));
    }

    public ItemResponse atualizar(Long id, ItemRequest requisicao) {
        Item item = obterEntidade(id);
        aplicar(requisicao, item);
        return paraResposta(itemRepository.save(item));
    }

    public ItemResponse alterarStatus(Long id, StatusItem novoStatus) {
        Item item = obterEntidade(id);
        try {
            item.alterarStatus(novoStatus);
        } catch (IllegalStateException exception) {
            throw new RegraDeNegocioException(exception.getMessage());
        }
        return paraResposta(itemRepository.save(item));
    }

    public void excluir(Long id) {
        itemRepository.delete(obterEntidade(id));
    }

    private Item obterEntidade(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + id));
    }

    private Categoria obterCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada: " + id));
    }

    private Local obterLocal(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Local não encontrado: " + id));
    }

    private void aplicar(ItemRequest requisicao, Item item) {
        item.setNome(requisicao.nome().trim());
        item.setDescricao(requisicao.descricao().trim());
        item.setTipo(requisicao.tipo());
        item.setDataOcorrencia(requisicao.dataOcorrencia());
        item.setNomeContato(requisicao.nomeContato().trim());
        item.setEmailContato(requisicao.emailContato().trim());
        item.setCategoria(obterCategoria(requisicao.categoriaId()));
        item.setLocal(obterLocal(requisicao.localId()));
    }

    private ItemResponse paraResposta(Item item) {
        Categoria categoria = item.getCategoria();
        Local local = item.getLocal();
        return new ItemResponse(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getTipo(),
                item.getStatus(),
                item.getDataOcorrencia(),
                item.getNomeContato(),
                item.getEmailContato(),
                item.getDataCadastro(),
                new CategoriaResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao()),
                new LocalResponse(local.getId(), local.getNome(), local.getDetalhes(), local.getCidade(), local.getEstado())
        );
    }
}
