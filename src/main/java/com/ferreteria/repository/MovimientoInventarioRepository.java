package com.ferreteria.repository;

import com.ferreteria.model.MovimientoInventario;
import com.ferreteria.model.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByProductoIdOrderByFechaCreacionDesc(Long productoId);

    List<MovimientoInventario> findByTipo(TipoMovimiento tipo);

    // Resumen mensual de entradas/salidas agrupado por producto (para el dashboard)
    @Query("SELECT m.producto.id AS productoId, m.producto.nombre AS productoNombre, "
            + "COALESCE(SUM(CASE WHEN m.tipo = :tipoEntrada THEN m.cantidad END), 0) AS totalEntradas, "
            + "COALESCE(SUM(CASE WHEN m.tipo = :tipoSalida THEN m.cantidad END), 0) AS totalSalidas "
            + "FROM MovimientoInventario m "
            + "WHERE m.fechaCreacion >= :inicio AND m.fechaCreacion < :fin "
            + "GROUP BY m.producto.id, m.producto.nombre "
            + "ORDER BY m.producto.nombre ASC")
    List<MovimientoStockProjection> obtenerResumenMensual(
            @Param("tipoEntrada") TipoMovimiento tipoEntrada,
            @Param("tipoSalida") TipoMovimiento tipoSalida,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}
