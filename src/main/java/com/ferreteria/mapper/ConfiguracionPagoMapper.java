package com.ferreteria.mapper;

import com.ferreteria.dto.ConfiguracionPagoRequest;
import com.ferreteria.dto.ConfiguracionPagoResponse;
import com.ferreteria.model.ConfiguracionPago;

public final class ConfiguracionPagoMapper {

    private ConfiguracionPagoMapper() {
    }

    public static void actualizarEntity(ConfiguracionPago configuracionPago, ConfiguracionPagoRequest request) {
        configuracionPago.setWhatsappPagos(request.whatsappPagos());
        configuracionPago.setQrYapeUrl(request.qrYapeUrl());
        configuracionPago.setBanco(request.banco());
        configuracionPago.setNumeroCuenta(request.numeroCuenta());
        configuracionPago.setCci(request.cci());
    }

    public static ConfiguracionPagoResponse toResponse(ConfiguracionPago configuracionPago) {
        return new ConfiguracionPagoResponse(
                configuracionPago.getId(),
                configuracionPago.getWhatsappPagos(),
                configuracionPago.getQrYapeUrl(),
                configuracionPago.getBanco(),
                configuracionPago.getNumeroCuenta(),
                configuracionPago.getCci(),
                configuracionPago.getFechaActualizacion()
        );
    }
}
