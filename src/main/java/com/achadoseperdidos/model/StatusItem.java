package com.achadoseperdidos.model;

public enum StatusItem {
    ABERTO,
    EM_CONTATO,
    RESOLVIDO,
    CANCELADO;

    public boolean permiteTransicaoPara(StatusItem proximoStatus) {
        return switch (this) {
            case ABERTO -> proximoStatus == EM_CONTATO || proximoStatus == CANCELADO;
            case EM_CONTATO -> proximoStatus == RESOLVIDO || proximoStatus == CANCELADO;
            case RESOLVIDO, CANCELADO -> false;
        };
    }
}
