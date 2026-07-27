package com.ferreteria.mapper;

import com.ferreteria.dto.ProveedorRequest;
import com.ferreteria.dto.ProveedorResponse;
import com.ferreteria.model.Proveedor;

import java.util.List;

public final class ProveedorMapper {

    private ProveedorMapper() {
    }

    public static Proveedor toEntity(ProveedorRequest request) {
        return Proveedor.builder()
                .nombre(request.nombre())
                .contactoWhatsapp(request.contactoWhatsapp())
                .build();
    }

    public static void actualizarEntity(Proveedor proveedor, ProveedorRequest request) {
        proveedor.setNombre(request.nombre());
        proveedor.setContactoWhatsapp(request.contactoWhatsapp());
    }

    public static ProveedorResponse toResponse(Proveedor proveedor) {
        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getContactoWhatsapp(),
                proveedor.getFechaCreacion()
        );
    }

    public static List<ProveedorResponse> toResponseList(List<Proveedor> proveedores) {
        return proveedores.stream()
                .map(ProveedorMapper::toResponse)
                .toList();
    }
}
