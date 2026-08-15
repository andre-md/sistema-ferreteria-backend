package com.ferreteria.dto;

import java.math.BigDecimal;

public record PrecioProductoResponse(
        Long productoId,
        String productoNombre,
        BigDecimal precioVenta,
        BigDecimal precioCompra
) {
}
