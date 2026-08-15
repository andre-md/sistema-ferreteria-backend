package com.ferreteria.repository;

import com.ferreteria.model.Pedido;
import com.ferreteria.model.enums.EstadoEntrega;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByEstadoPago(EstadoPago estadoPago);

    List<Pedido> findByEstadoEntrega(EstadoEntrega estadoEntrega);

    List<Pedido> findByEstadoPagoAndEstadoEntrega(EstadoPago estadoPago, EstadoEntrega estadoEntrega);

    // Pedidos del catalogo publico aun no atendidos por un vendedor
    List<Pedido> findByUsuarioIsNull();

    List<Pedido> findAllByOrderByFechaCreacionDesc();

    // Variantes que excluyen los pedidos cancelados de las vistas normales
    List<Pedido> findByEstadoPedidoOrderByFechaCreacionDesc(EstadoPedido estadoPedido);

    List<Pedido> findByEstadoPagoAndEstadoPedidoOrderByFechaCreacionDesc(EstadoPago estadoPago, EstadoPedido estadoPedido);

    List<Pedido> findByEstadoEntregaAndEstadoPedidoOrderByFechaCreacionDesc(EstadoEntrega estadoEntrega, EstadoPedido estadoPedido);

    List<Pedido> findByUsuarioIsNullAndEstadoPedidoOrderByFechaCreacionDesc(EstadoPedido estadoPedido);

    // Para el reporte de ventas mensuales
    List<Pedido> findByEstadoPagoAndEstadoPedidoAndFechaCreacionGreaterThanEqualAndFechaCreacionLessThan(
            EstadoPago estadoPago, EstadoPedido estadoPedido, LocalDateTime inicio, LocalDateTime fin);
}
