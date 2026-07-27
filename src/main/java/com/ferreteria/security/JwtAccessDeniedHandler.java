package com.ferreteria.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferreteria.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Se dispara cuando el usuario SI esta autenticado (token valido) pero no
 * tiene el rol/permiso requerido para la ruta, p.ej. un VENDEDOR llamando a
 * un endpoint restringido a ADMIN. Separado de JwtAuthenticationEntryPoint,
 * que solo cubre el caso de "no autenticado" (token ausente/invalido/expirado).
 */
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), ErrorResponse.of("No tienes permisos para realizar esta accion"));
    }
}
