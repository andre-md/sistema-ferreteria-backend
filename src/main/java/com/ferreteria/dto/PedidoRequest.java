package com.ferreteria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public record PedidoRequest(

        @NotBlank
        String clienteNombre,

        @NotBlank
        String clienteTelefono,

        BigDecimal montoAdelanto,

        Boolean usarSaldoAFavor,

        @NotEmpty
        @Valid
        List<DetallePedidoRequest> detalles
) {
}
