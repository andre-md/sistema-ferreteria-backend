package com.ferreteria.repository;

import com.ferreteria.model.enums.Moneda;

import java.math.BigDecimal;

// Proyeccion compartida por el reporte de productos mas vendidos y el de
// ganancia mensual (este ultimo necesita "moneda" para poder convertir a soles).
public interface ProductoVentaProjection {

    Long getProductoId();

    String getProductoNombre();

    Moneda getMoneda();

    BigDecimal getCantidadVendida();

    BigDecimal getTotalGenerado();
}
