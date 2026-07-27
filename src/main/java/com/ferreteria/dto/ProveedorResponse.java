package com.ferreteria.dto;

import java.time.LocalDateTime;

public record ProveedorResponse(
        Long id,
        String nombre,
        String contactoWhatsapp,
        LocalDateTime fechaCreacion
) {
}
