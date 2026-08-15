package com.ferreteria.dto;

import java.time.LocalDateTime;

public record ConfiguracionPagoResponse(
        Long id,
        String whatsappPagos,
        String qrYapeUrl,
        String banco,
        String numeroCuenta,
        String cci,
        LocalDateTime fechaActualizacion
) {
}
