package com.ferreteria.mapper;

import com.ferreteria.dto.UsuarioRequest;
import com.ferreteria.dto.UsuarioResponse;
import com.ferreteria.model.Usuario;

import java.util.List;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioRequest request, String passwordHash) {
        return Usuario.builder()
                .nombre(request.nombre())
                .email(request.email())
                .passwordHash(passwordHash)
                .rol(request.rol())
                .build();
    }

    public static void actualizarEntity(Usuario usuario, UsuarioRequest request) {
        usuario.setNombre(request.nombre());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol());
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.isActivo(),
                usuario.getFechaCreacion()
        );
    }

    public static List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }
}
