package com.ferreteria.controller;

import com.ferreteria.dto.ProductoProveedorRequest;
import com.ferreteria.dto.ProductoProveedorResponse;
import com.ferreteria.service.ProductoProveedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductoProveedorController {

    private final ProductoProveedorService productoProveedorService;

    @GetMapping("/api/productos/{productoId}/proveedores")
    public List<ProductoProveedorResponse> listarPorProducto(@PathVariable Long productoId) {
        return productoProveedorService.listarPorProducto(productoId);
    }

    @GetMapping("/api/proveedores/{proveedorId}/productos")
    public List<ProductoProveedorResponse> listarPorProveedor(@PathVariable Long proveedorId) {
        return productoProveedorService.listarPorProveedor(proveedorId);
    }

    @GetMapping("/api/productos/{productoId}/proveedores/mas-barato")
    public ProductoProveedorResponse obtenerMasBarato(@PathVariable Long productoId) {
        return productoProveedorService.obtenerMasBarato(productoId);
    }

    @PostMapping("/api/producto-proveedor")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoProveedorResponse crear(@Valid @RequestBody ProductoProveedorRequest request) {
        return productoProveedorService.crear(request);
    }

    @PutMapping("/api/producto-proveedor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoProveedorResponse actualizar(@PathVariable Long id, @Valid @RequestBody ProductoProveedorRequest request) {
        return productoProveedorService.actualizar(id, request);
    }

    @DeleteMapping("/api/producto-proveedor/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        productoProveedorService.eliminar(id);
    }
}
