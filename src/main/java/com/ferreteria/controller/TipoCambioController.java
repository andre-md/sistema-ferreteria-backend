package com.ferreteria.controller;

import com.ferreteria.dto.TipoCambioRequest;
import com.ferreteria.dto.TipoCambioResponse;
import com.ferreteria.service.TipoCambioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-cambio")
@RequiredArgsConstructor
public class TipoCambioController {

    private final TipoCambioService tipoCambioService;

    @GetMapping("/actual")
    public TipoCambioResponse obtenerActual() {
        return tipoCambioService.obtenerActual();
    }

    @GetMapping("/historial")
    public List<TipoCambioResponse> listarHistorial() {
        return tipoCambioService.listarHistorial();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public TipoCambioResponse registrar(@Valid @RequestBody TipoCambioRequest request, Authentication authentication) {
        return tipoCambioService.registrar(request, authentication.getName());
    }
}
