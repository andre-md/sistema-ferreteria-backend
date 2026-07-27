package com.ferreteria.mapper;

import com.ferreteria.dto.TipoCambioRequest;
import com.ferreteria.dto.TipoCambioResponse;
import com.ferreteria.model.TipoCambio;
import com.ferreteria.model.Usuario;

import java.time.LocalDate;
import java.util.List;

public final class TipoCambioMapper {

    private TipoCambioMapper() {
    }

    public static TipoCambio toEntity(TipoCambioRequest request, Usuario usuario) {
        return TipoCambio.builder()
                .fecha(LocalDate.now())
                .valor(request.valor())
                .actualizadoPor(usuario)
                .build();
    }

    public static TipoCambioResponse toResponse(TipoCambio tipoCambio) {
        return new TipoCambioResponse(
                tipoCambio.getId(),
                tipoCambio.getFecha(),
                tipoCambio.getValor(),
                tipoCambio.getActualizadoPor().getNombre(),
                tipoCambio.getFechaCreacion()
        );
    }

    public static List<TipoCambioResponse> toResponseList(List<TipoCambio> tiposCambio) {
        return tiposCambio.stream()
                .map(TipoCambioMapper::toResponse)
                .toList();
    }
}
