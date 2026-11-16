package com.achadoseperdidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocalRequest(
        @NotBlank(message = "O nome do local é obrigatório.")
        @Size(max = 100, message = "O nome do local deve ter no máximo 100 caracteres.")
        String nome,

        @Size(max = 250, message = "Os detalhes devem ter no máximo 250 caracteres.")
        String detalhes,

        @NotBlank(message = "A cidade é obrigatória.")
        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
        String cidade,

        @NotBlank(message = "A sigla do estado é obrigatória.")
        @Size(min = 2, max = 2, message = "Informe a sigla do estado com duas letras.")
        String estado
) {
}
