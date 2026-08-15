package com.ferreteria.dto;

public record ConfiguracionPagoRequest(
        String whatsappPagos,
        String qrYapeUrl,
        String banco,
        String numeroCuenta,
        String cci
) {
}
