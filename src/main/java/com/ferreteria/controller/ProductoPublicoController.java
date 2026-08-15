package com.ferreteria.controller;

import com.ferreteria.dto.ProductoResponse;
import com.ferreteria.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/productos")
@RequiredArgsConstructor
public class ProductoPublicoController {

    private final ProductoService productoService;

    @GetMapping
    public List<ProductoResponse> listarActivos() {
        return productoService.listarActivos();
    }

    @GetMapping("/{id}")
    public ProductoResponse obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerActivoPorId(id);
    }
}
