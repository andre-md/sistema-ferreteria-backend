package com.ferreteria.service;

import com.ferreteria.dto.ProductoProveedorRequest;
import com.ferreteria.dto.ProductoProveedorResponse;
import com.ferreteria.exception.ProductoNoEncontradoException;
import com.ferreteria.exception.ProductoProveedorDuplicadoException;
import com.ferreteria.exception.ProductoProveedorNoEncontradoException;
import com.ferreteria.exception.ProveedorNoEncontradoException;
import com.ferreteria.mapper.ProductoProveedorMapper;
import com.ferreteria.model.Producto;
import com.ferreteria.model.ProductoProveedor;
import com.ferreteria.model.Proveedor;
import com.ferreteria.repository.ProductoProveedorRepository;
import com.ferreteria.repository.ProductoRepository;
import com.ferreteria.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoProveedorService {

    private final ProductoProveedorRepository productoProveedorRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoProveedorMapper productoProveedorMapper;

    public List<ProductoProveedorResponse> listarPorProducto(Long productoId) {
        return productoProveedorMapper.toResponseList(productoProveedorRepository.findByProductoId(productoId));
    }

    public List<ProductoProveedorResponse> listarPorProveedor(Long proveedorId) {
        return productoProveedorMapper.toResponseList(productoProveedorRepository.findByProveedorId(proveedorId));
    }

    @Transactional
    public ProductoProveedorResponse crear(ProductoProveedorRequest request) {
        Producto producto = buscarProductoOLanzar(request.productoId());
        Proveedor proveedor = buscarProveedorOLanzar(request.proveedorId());

        productoProveedorRepository.findByProductoIdAndProveedorId(request.productoId(), request.proveedorId())
                .ifPresent(existente -> {
                    throw new ProductoProveedorDuplicadoException(
                            "Ya existe una cotizacion de '" + proveedor.getNombre() + "' para '" + producto.getNombre()
                                    + "'; usa actualizar en vez de crear");
                });

        ProductoProveedor productoProveedor = productoProveedorMapper.toEntity(request, producto, proveedor);
        return productoProveedorMapper.toResponse(productoProveedorRepository.save(productoProveedor));
    }

    @Transactional
    public ProductoProveedorResponse actualizar(Long id, ProductoProveedorRequest request) {
        ProductoProveedor productoProveedor = buscarProductoProveedorOLanzar(id);
        productoProveedorMapper.actualizarEntity(productoProveedor, request);
        return productoProveedorMapper.toResponse(productoProveedorRepository.save(productoProveedor));
    }

    @Transactional
    public void eliminar(Long id) {
        ProductoProveedor productoProveedor = buscarProductoProveedorOLanzar(id);
        productoProveedorRepository.delete(productoProveedor);
    }

    public ProductoProveedorResponse obtenerMasBarato(Long productoId) {
        List<ProductoProveedor> cotizaciones = productoProveedorRepository.findByProductoId(productoId);

        if (cotizaciones.isEmpty()) {
            throw new ProductoProveedorNoEncontradoException(
                    "No hay proveedores registrados para el producto con id: " + productoId);
        }

        return cotizaciones.stream()
                .map(productoProveedorMapper::toResponse)
                .min(Comparator.comparing(ProductoProveedorResponse::precioCostoEnSoles))
                .orElseThrow(() -> new ProductoProveedorNoEncontradoException(
                        "No hay proveedores registrados para el producto con id: " + productoId));
    }

    private Producto buscarProductoOLanzar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private Proveedor buscarProveedorOLanzar(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ProveedorNoEncontradoException("Proveedor no encontrado con id: " + id));
    }

    private ProductoProveedor buscarProductoProveedorOLanzar(Long id) {
        return productoProveedorRepository.findById(id)
                .orElseThrow(() -> new ProductoProveedorNoEncontradoException(
                        "Relacion producto-proveedor no encontrada con id: " + id));
    }
}
