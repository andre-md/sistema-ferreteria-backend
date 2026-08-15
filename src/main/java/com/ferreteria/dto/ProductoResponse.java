package com.ferreteria.dto;

import com.ferreteria.model.enums.Moneda;
import com.ferreteria.model.enums.UnidadMedida;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponse(
        Long id,
        String nombre,
        Long categoriaId,
        String categoriaNombre,
        UnidadMedida unidadMedida,
        BigDecimal precioVenta,
        BigDecimal stockActual,
        Moneda moneda,
        boolean activo,
        LocalDateTime fechaCreacion,
        String imagenUrl
) {
}
