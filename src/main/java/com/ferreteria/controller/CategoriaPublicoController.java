package com.ferreteria.controller;

import com.ferreteria.dto.CategoriaResponse;
import com.ferreteria.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/categorias")
@RequiredArgsConstructor
public class CategoriaPublicoController {

    private final CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponse> listarTodas() {
        return categoriaService.listarTodas();
    }
}
