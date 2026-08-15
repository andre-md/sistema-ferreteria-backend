package com.ferreteria.dto;

import java.math.BigDecimal;

public record MovimientoStockMensualResponse(
        Long productoId,
        String productoNombre,
        BigDecimal totalEntradas,
        BigDecimal totalSalidas
) {
}
