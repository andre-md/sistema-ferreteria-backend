package com.ferreteria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DetallePedidoRequest(

        @NotNull
        Long productoId,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal cantidad
) {
}
