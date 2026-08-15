package com.ferreteria.dto;

import java.math.BigDecimal;

public record GananciaMensualResponse(
        BigDecimal gananciaEstimada,
        int productosExcluidosPorFaltaDeCosto
) {
}
