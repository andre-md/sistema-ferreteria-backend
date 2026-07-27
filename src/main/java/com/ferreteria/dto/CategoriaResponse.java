package com.ferreteria.dto;

import java.time.LocalDateTime;

public record CategoriaResponse(
        Long id,
        String nombre,
        LocalDateTime fechaCreacion
) {
}
