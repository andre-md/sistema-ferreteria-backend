package com.ferreteria.mapper;

import com.ferreteria.dto.SaldoClienteResponse;
import com.ferreteria.model.SaldoCliente;

public final class SaldoClienteMapper {

    private SaldoClienteMapper() {
    }

    public static SaldoClienteResponse toResponse(SaldoCliente saldoCliente) {
        return new SaldoClienteResponse(
                saldoCliente.getId(),
                saldoCliente.getClienteTelefono(),
                saldoCliente.getClienteNombre(),
                saldoCliente.getMontoDisponible(),
                saldoCliente.getFechaActualizacion()
        );
    }
}
