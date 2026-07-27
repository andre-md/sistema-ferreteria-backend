package com.ferreteria.mapper;

import com.ferreteria.dto.DetallePedidoRequest;
import com.ferreteria.dto.DetallePedidoResponse;
import com.ferreteria.dto.PedidoResponse;
import com.ferreteria.model.DetallePedido;
import com.ferreteria.model.Pedido;
import com.ferreteria.model.Producto;

import java.math.RoundingMode;
import java.util.List;

public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static DetallePedido toDetalleEntity(DetallePedidoRequest request, Producto producto, Pedido pedido) {
        return DetallePedido.builder()
                .pedido(pedido)
                .producto(producto)
                .cantidad(request.cantidad().setScale(2, RoundingMode.HALF_UP))
                .precioUnitario(producto.getPrecioVenta())
                .build();
    }

    public static DetallePedidoResponse toDetalleResponse(DetallePedido detalle) {
        return new DetallePedidoResponse(
                detalle.getId(),
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getCantidad().multiply(detalle.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP)
        );
    }

    public static PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getClienteNombre(),
                pedido.getClienteTelefono(),
                pedido.getEstadoPago(),
                pedido.getEstadoEntrega(),
                pedido.getEstadoPedido(),
                pedido.getMontoAdelanto(),
                pedido.getMontoTotal(),
                pedido.getMontoTotal().subtract(pedido.getMontoAdelanto()),
                pedido.getFechaCreacion(),
                pedido.getUsuario() != null ? pedido.getUsuario().getNombre() : null,
                pedido.getDetalles().stream()
                        .map(PedidoMapper::toDetalleResponse)
                        .toList()
        );
    }

    public static List<PedidoResponse> toResponseList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(PedidoMapper::toResponse)
                .toList();
    }
}
