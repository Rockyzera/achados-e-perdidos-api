package com.achadoseperdidos.dto;

import com.achadoseperdidos.model.TipoRegistro;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ItemRequest(
        @NotBlank(message = "O nome do item é obrigatório.")
        @Size(max = 100, message = "O nome do item deve ter no máximo 100 caracteres.")
        String nome,

        @NotBlank(message = "A descrição do item é obrigatória.")
        @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres.")
        String descricao,

        @NotNull(message = "O tipo do registro é obrigatório.")
        TipoRegistro tipo,

        @NotNull(message = "A data da ocorrência é obrigatória.")
        @PastOrPresent(message = "A data da ocorrência não pode ser futura.")
        LocalDate dataOcorrencia,

        @NotBlank(message = "O nome para contato é obrigatório.")
        @Size(max = 100, message = "O nome para contato deve ter no máximo 100 caracteres.")
        String nomeContato,

        @NotBlank(message = "O e-mail para contato é obrigatório.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres.")
        String emailContato,

        @NotNull(message = "A categoria é obrigatória.")
        Long categoriaId,

        @NotNull(message = "O local é obrigatório.")
        Long localId
) {
}
