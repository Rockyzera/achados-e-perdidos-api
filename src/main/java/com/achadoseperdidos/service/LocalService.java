package com.achadoseperdidos.service;

import com.achadoseperdidos.dto.LocalRequest;
import com.achadoseperdidos.dto.LocalResponse;
import com.achadoseperdidos.exception.RegraDeNegocioException;
import com.achadoseperdidos.exception.RecursoNaoEncontradoException;
import com.achadoseperdidos.model.Local;
import com.achadoseperdidos.repository.ItemRepository;
import com.achadoseperdidos.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocalService {

    private final LocalRepository localRepository;
    private final ItemRepository itemRepository;

    public LocalService(LocalRepository localRepository, ItemRepository itemRepository) {
        this.localRepository = localRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional(readOnly = true)
    public List<LocalResponse> listar() {
        return localRepository.findAll().stream().map(this::paraResposta).toList();
    }

    @Transactional(readOnly = true)
    public LocalResponse buscarPorId(Long id) {
        return paraResposta(obterEntidade(id));
    }

    public LocalResponse criar(LocalRequest requisicao) {
        Local local = new Local();
        aplicar(requisicao, local);
        return paraResposta(localRepository.save(local));
    }

    public LocalResponse atualizar(Long id, LocalRequest requisicao) {
        Local local = obterEntidade(id);
        aplicar(requisicao, local);
        return paraResposta(localRepository.save(local));
    }

    public void excluir(Long id) {
        Local local = obterEntidade(id);
        if (itemRepository.existsByLocal_Id(id)) {
            throw new RegraDeNegocioException("Não é possível excluir um local que está associado a itens.");
        }
        localRepository.delete(local);
    }

    private Local obterEntidade(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Local não encontrado: " + id));
    }

    private void aplicar(LocalRequest requisicao, Local local) {
        local.setNome(requisicao.nome().trim());
        local.setDetalhes(requisicao.detalhes() == null ? null : requisicao.detalhes().trim());
        local.setCidade(requisicao.cidade().trim());
        local.setEstado(requisicao.estado().trim().toUpperCase());
    }

    private LocalResponse paraResposta(Local local) {
        return new LocalResponse(local.getId(), local.getNome(), local.getDetalhes(), local.getCidade(), local.getEstado());
    }
}
