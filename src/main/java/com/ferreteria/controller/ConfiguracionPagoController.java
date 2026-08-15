package com.ferreteria.controller;

import com.ferreteria.dto.ConfiguracionPagoRequest;
import com.ferreteria.dto.ConfiguracionPagoResponse;
import com.ferreteria.service.ConfiguracionPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ConfiguracionPagoController {

    private final ConfiguracionPagoService configuracionPagoService;

    @GetMapping("/api/public/configuracion-pago")
    public ConfiguracionPagoResponse obtener() {
        return configuracionPagoService.obtener();
    }

    @PutMapping("/api/configuracion-pago")
    @PreAuthorize("hasRole('ADMIN')")
    public ConfiguracionPagoResponse actualizar(@RequestBody ConfiguracionPagoRequest request) {
        return configuracionPagoService.actualizar(request);
    }
}
