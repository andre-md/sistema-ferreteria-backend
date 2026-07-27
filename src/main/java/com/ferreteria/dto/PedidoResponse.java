package com.ferreteria.dto;

import com.ferreteria.model.enums.EstadoEntrega;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.model.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String clienteNombre,
        String clienteTelefono,
        EstadoPago estadoPago,
        EstadoEntrega estadoEntrega,
        EstadoPedido estadoPedido,
        BigDecimal montoAdelanto,
        BigDecimal montoTotal,
        BigDecimal saldoPendiente,
        LocalDateTime fechaCreacion,
        String vendedorNombre,
        List<DetallePedidoResponse> detalles
) {
}
