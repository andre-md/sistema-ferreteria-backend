package com.ferreteria.dto;

import com.ferreteria.model.enums.Moneda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoProveedorResponse(
        Long id,
        String productoNombre,
        String proveedorNombre,
        BigDecimal precioCosto,
        Moneda moneda,
        BigDecimal precioCostoEnSoles,
        LocalDateTime fechaActualizacion
) {
}
