package com.ferreteria.service;

import com.ferreteria.dto.ProductoRequest;
import com.ferreteria.dto.ProductoResponse;
import com.ferreteria.exception.CategoriaNoEncontradaException;
import com.ferreteria.exception.ProductoNoEncontradoException;
import com.ferreteria.mapper.ProductoMapper;
import com.ferreteria.model.Categoria;
import com.ferreteria.model.Producto;
import com.ferreteria.repository.CategoriaRepository;
import com.ferreteria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProductoResponse> listarActivos() {
        return ProductoMapper.toResponseList(productoRepository.findByActivoTrueOrderByNombreAsc());
    }

    public List<ProductoResponse> listarPorCategoria(Long categoriaId) {
        return ProductoMapper.toResponseList(productoRepository.findByCategoriaIdAndActivoTrueOrderByNombreAsc(categoriaId));
    }

    public List<ProductoResponse> buscarPorNombre(String texto) {
        return ProductoMapper.toResponseList(productoRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrderByNombreAsc(texto));
    }

    public ProductoResponse obtenerPorId(Long id) {
        return ProductoMapper.toResponse(buscarProductoOLanzar(id));
    }

    // Para el catalogo publico: un producto inactivo debe verse igual que uno
    // inexistente, sin distincion, ya que no deberia ser visible para el publico.
    public ProductoResponse obtenerActivoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .filter(Producto::isActivo)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con id: " + id));
        return ProductoMapper.toResponse(producto);
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = buscarCategoriaOLanzar(request.categoriaId());
        Producto producto = ProductoMapper.toEntity(request, categoria);
        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscarProductoOLanzar(id);
        Categoria categoria = buscarCategoriaOLanzar(request.categoriaId());
        ProductoMapper.actualizarEntity(producto, request, categoria);
        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void desactivar(Long id) {
        Producto producto = buscarProductoOLanzar(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    public List<ProductoResponse> listarStockBajo(BigDecimal umbral) {
        return ProductoMapper.toResponseList(productoRepository.findByStockActualLessThanAndActivoTrueOrderByNombreAsc(umbral));
    }

    private Producto buscarProductoOLanzar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private Categoria buscarCategoriaOLanzar(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException("Categoria no encontrada con id: " + categoriaId));
    }
}
