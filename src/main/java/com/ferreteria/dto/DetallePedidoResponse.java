package com.ferreteria.dto;

import java.math.BigDecimal;

public record DetallePedidoResponse(
        Long id,
        Long productoId,
        String productoNombre,
        BigDecimal cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
