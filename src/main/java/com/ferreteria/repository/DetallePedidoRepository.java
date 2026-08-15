package com.ferreteria.repository;

import com.ferreteria.model.DetallePedido;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);

    List<DetallePedido> findByProductoId(Long productoId);

    // Ventas agrupadas por producto en un rango de fechas, ordenadas de mas a menos
    // vendido. La usan tanto el reporte de productos mas vendidos como el de
    // ganancia mensual (que ademas necesita "moneda" para convertir a soles).
    @Query("SELECT dp.producto.id AS productoId, dp.producto.nombre AS productoNombre, "
            + "dp.producto.moneda AS moneda, SUM(dp.cantidad) AS cantidadVendida, "
            + "SUM(dp.cantidad * dp.precioUnitario) AS totalGenerado "
            + "FROM DetallePedido dp "
            + "WHERE dp.pedido.estadoPedido = :estadoPedido "
            + "AND dp.pedido.fechaCreacion >= :inicio AND dp.pedido.fechaCreacion < :fin "
            + "GROUP BY dp.producto.id, dp.producto.nombre, dp.producto.moneda "
            + "ORDER BY SUM(dp.cantidad) DESC")
    List<ProductoVentaProjection> obtenerVentasPorProducto(
            @Param("estadoPedido") EstadoPedido estadoPedido,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    // Variante solo con pedidos PAGADO, para el calculo de ganancia real (dinero
    // efectivamente cobrado, no solo pedidos colocados/con adelanto).
    @Query("SELECT dp.producto.id AS productoId, dp.producto.nombre AS productoNombre, "
            + "dp.producto.moneda AS moneda, SUM(dp.cantidad) AS cantidadVendida, "
            + "SUM(dp.cantidad * dp.precioUnitario) AS totalGenerado "
            + "FROM DetallePedido dp "
            + "WHERE dp.pedido.estadoPago = :estadoPago "
            + "AND dp.pedido.estadoPedido = :estadoPedido "
            + "AND dp.pedido.fechaCreacion >= :inicio AND dp.pedido.fechaCreacion < :fin "
            + "GROUP BY dp.producto.id, dp.producto.nombre, dp.producto.moneda "
            + "ORDER BY SUM(dp.cantidad) DESC")
    List<ProductoVentaProjection> obtenerVentasPagadasPorProducto(
            @Param("estadoPago") EstadoPago estadoPago,
            @Param("estadoPedido") EstadoPedido estadoPedido,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}
