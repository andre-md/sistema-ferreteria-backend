package com.ferreteria.mapper;

import com.ferreteria.dto.ProductoRequest;
import com.ferreteria.dto.ProductoResponse;
import com.ferreteria.model.Categoria;
import com.ferreteria.model.Producto;

import java.util.List;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static Producto toEntity(ProductoRequest request, Categoria categoria) {
        return Producto.builder()
                .nombre(request.nombre())
                .categoria(categoria)
                .unidadMedida(request.unidadMedida())
                .precioVenta(request.precioVenta())
                .stockActual(request.stockActual())
                .moneda(request.moneda())
                .imagenUrl(request.imagenUrl())
                .build();
    }

    public static void actualizarEntity(Producto producto, ProductoRequest request, Categoria categoria) {
        producto.setNombre(request.nombre());
        producto.setCategoria(categoria);
        producto.setUnidadMedida(request.unidadMedida());
        producto.setPrecioVenta(request.precioVenta());
        producto.setStockActual(request.stockActual());
        producto.setMoneda(request.moneda());
        producto.setImagenUrl(request.imagenUrl());
    }

    public static ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getUnidadMedida(),
                producto.getPrecioVenta(),
                producto.getStockActual(),
                producto.getMoneda(),
                producto.isActivo(),
                producto.getFechaCreacion(),
                producto.getImagenUrl()
        );
    }

    public static List<ProductoResponse> toResponseList(List<Producto> productos) {
        return productos.stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }
}
