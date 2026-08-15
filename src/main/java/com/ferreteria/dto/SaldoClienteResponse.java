package com.ferreteria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaldoClienteResponse(
        Long id,
        String clienteTelefono,
        String clienteNombre,
        BigDecimal montoDisponible,
        LocalDateTime fechaActualizacion
) {
}
