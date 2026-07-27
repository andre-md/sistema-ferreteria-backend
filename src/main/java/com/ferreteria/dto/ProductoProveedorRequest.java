package com.ferreteria.dto;

import com.ferreteria.model.enums.Moneda;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductoProveedorRequest(

        @NotNull
        Long productoId,

        @NotNull
        Long proveedorId,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = true)
        BigDecimal precioCosto,

        @NotNull
        Moneda moneda
) {
}
