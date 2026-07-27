package com.ferreteria.dto;

public record LoginResponse(
        String token,
        String email,
        String nombre,
        String rol
) {
}
