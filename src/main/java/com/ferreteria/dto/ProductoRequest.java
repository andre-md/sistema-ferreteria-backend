package com.ferreteria.dto;

import com.ferreteria.model.enums.Moneda;
import com.ferreteria.model.enums.UnidadMedida;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductoRequest(

        @NotBlank
        String nombre,

        @NotNull
        Long categoriaId,

        @NotNull
        UnidadMedida unidadMedida,

        @NotNull
        @DecimalMin(value = "0", inclusive = true)
        BigDecimal precioVenta,

        @NotNull
        @DecimalMin(value = "0", inclusive = true)
        BigDecimal stockActual,

        @NotNull
        Moneda moneda
) {
}
