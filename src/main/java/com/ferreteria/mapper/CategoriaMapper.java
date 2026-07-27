package com.ferreteria.mapper;

import com.ferreteria.dto.CategoriaRequest;
import com.ferreteria.dto.CategoriaResponse;
import com.ferreteria.model.Categoria;

import java.util.List;

public final class CategoriaMapper {

    private CategoriaMapper() {
    }

    public static Categoria toEntity(CategoriaRequest request) {
        return Categoria.builder()
                .nombre(request.nombre())
                .build();
    }

    public static void actualizarEntity(Categoria categoria, CategoriaRequest request) {
        categoria.setNombre(request.nombre());
    }

    public static CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getFechaCreacion()
        );
    }

    public static List<CategoriaResponse> toResponseList(List<Categoria> categorias) {
        return categorias.stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }
}
