package com.ferreteria.dto;

import com.ferreteria.model.enums.RolUsuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        RolUsuario rol,
        boolean activo,
        LocalDateTime fechaCreacion
) {
}
