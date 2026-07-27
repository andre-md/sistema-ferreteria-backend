package com.ferreteria.dto;

import jakarta.validation.constraints.NotNull;

public record ReasignarPedidoRequest(

        @NotNull
        Long vendedorId
) {
}
