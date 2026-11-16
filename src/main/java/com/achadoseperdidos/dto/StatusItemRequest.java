package com.achadoseperdidos.dto;

import com.achadoseperdidos.model.StatusItem;
import jakarta.validation.constraints.NotNull;

public record StatusItemRequest(
        @NotNull(message = "O novo status é obrigatório.")
        StatusItem status
) {
}
