package com.ferreteria.mapper;

import com.ferreteria.dto.ProductoProveedorRequest;
import com.ferreteria.dto.ProductoProveedorResponse;
import com.ferreteria.exception.TipoCambioNoDisponibleException;
import com.ferreteria.model.Producto;
import com.ferreteria.model.ProductoProveedor;
import com.ferreteria.model.Proveedor;
import com.ferreteria.model.TipoCambio;
import com.ferreteria.model.enums.Moneda;
import com.ferreteria.repository.TipoCambioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * A diferencia de los demas mappers (estaticos), este necesita consultar
 * TipoCambioRepository para calcular precioCostoEnSoles cuando la cotizacion
 * esta en USD, por eso es un bean gestionado por Spring en vez de metodos estaticos.
 */
@Component
@RequiredArgsConstructor
public class ProductoProveedorMapper {

    private final TipoCambioRepository tipoCambioRepository;

    public ProductoProveedor toEntity(ProductoProveedorRequest request, Producto producto, Proveedor proveedor) {
        return ProductoProveedor.builder()
                .producto(producto)
                .proveedor(proveedor)
                .precioCosto(request.precioCosto())
                .moneda(request.moneda())
                .build();
    }

    public void actualizarEntity(ProductoProveedor productoProveedor, ProductoProveedorRequest request) {
        productoProveedor.setPrecioCosto(request.precioCosto());
        productoProveedor.setMoneda(request.moneda());
    }

    public ProductoProveedorResponse toResponse(ProductoProveedor productoProveedor) {
        return new ProductoProveedorResponse(
                productoProveedor.getId(),
                productoProveedor.getProducto().getNombre(),
                productoProveedor.getProveedor().getNombre(),
                productoProveedor.getPrecioCosto(),
                productoProveedor.getMoneda(),
                calcularPrecioCostoEnSoles(productoProveedor),
                productoProveedor.getFechaActualizacion()
        );
    }

    public List<ProductoProveedorResponse> toResponseList(List<ProductoProveedor> productosProveedores) {
        return productosProveedores.stream()
                .map(this::toResponse)
                .toList();
    }

    private BigDecimal calcularPrecioCostoEnSoles(ProductoProveedor productoProveedor) {
        if (productoProveedor.getMoneda() == Moneda.PEN) {
            return productoProveedor.getPrecioCosto();
        }

        BigDecimal tipoCambio = tipoCambioRepository.findTopByOrderByFechaDesc()
                .map(TipoCambio::getValor)
                .orElseThrow(() -> new TipoCambioNoDisponibleException(
                        "No hay un tipo de cambio registrado para convertir precios en USD a soles"));

        return productoProveedor.getPrecioCosto().multiply(tipoCambio);
    }
}
