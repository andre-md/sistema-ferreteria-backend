package com.ferreteria.controller;

import com.ferreteria.dto.SaldoClienteResponse;
import com.ferreteria.service.SaldoClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saldo-cliente")
@RequiredArgsConstructor
public class SaldoClienteController {

    private final SaldoClienteService saldoClienteService;

    @GetMapping("/{telefono}")
    public SaldoClienteResponse consultarSaldo(@PathVariable String telefono) {
        return saldoClienteService.consultarSaldo(telefono);
    }
}
