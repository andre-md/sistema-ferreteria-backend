package com.ferreteria.repository;

import com.ferreteria.model.Pedido;
import com.ferreteria.model.enums.EstadoEntrega;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.model.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

    List<Pedido> findByEstadoPagoAndEstadoPedido(EstadoPago estadoPago, EstadoPedido estadoPedido);

    List<Pedido> findByEstadoEntregaAndEstadoPedido(EstadoEntrega estadoEntrega, EstadoPedido estadoPedido);

    List<Pedido> findByUsuarioIsNullAndEstadoPedido(EstadoPedido estadoPedido);
}
