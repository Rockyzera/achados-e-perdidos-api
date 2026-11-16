package com.achadoseperdidos.dto;

import com.achadoseperdidos.model.StatusItem;
import com.achadoseperdidos.model.TipoRegistro;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String nome,
        String descricao,
        TipoRegistro tipo,
        StatusItem status,
        LocalDate dataOcorrencia,
        String nomeContato,
        String emailContato,
        LocalDateTime dataCadastro,
        CategoriaResponse categoria,
        LocalResponse local
) {
}
