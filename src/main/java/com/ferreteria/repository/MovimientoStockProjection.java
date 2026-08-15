package com.ferreteria.repository;

import java.math.BigDecimal;

public interface MovimientoStockProjection {

    Long getProductoId();

    String getProductoNombre();

    BigDecimal getTotalEntradas();

    BigDecimal getTotalSalidas();
}
