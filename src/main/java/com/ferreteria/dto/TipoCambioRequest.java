package com.ferreteria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TipoCambioRequest(

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal valor
) {
}
