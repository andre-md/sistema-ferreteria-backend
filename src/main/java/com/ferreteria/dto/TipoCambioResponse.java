package com.ferreteria.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TipoCambioResponse(
        Long id,
        LocalDate fecha,
        BigDecimal valor,
        String actualizadoPorNombre,
        LocalDateTime fechaCreacion
) {
}
