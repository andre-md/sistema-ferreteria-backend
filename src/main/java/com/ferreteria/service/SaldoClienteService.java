package com.ferreteria.service;

import com.ferreteria.dto.SaldoClienteResponse;
import com.ferreteria.exception.SaldoClienteNoEncontradoException;
import com.ferreteria.exception.SaldoInsuficienteException;
import com.ferreteria.mapper.SaldoClienteMapper;
import com.ferreteria.model.SaldoCliente;
import com.ferreteria.repository.SaldoClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SaldoClienteService {

    private final SaldoClienteRepository saldoClienteRepository;

    public SaldoClienteResponse consultarSaldo(String clienteTelefono) {
        SaldoCliente saldoCliente = saldoClienteRepository.findByClienteTelefono(clienteTelefono)
                .orElseThrow(() -> new SaldoClienteNoEncontradoException(
                        "No hay saldo a favor registrado para el telefono: " + clienteTelefono));
        return SaldoClienteMapper.toResponse(saldoCliente);
    }

    @Transactional
    public void usarSaldo(String clienteTelefono, BigDecimal monto) {
        SaldoCliente saldoCliente = saldoClienteRepository.findByClienteTelefono(clienteTelefono).orElse(null);
        BigDecimal disponible = saldoCliente != null ? saldoCliente.getMontoDisponible() : BigDecimal.ZERO;

        if (disponible.compareTo(monto) < 0) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente para el telefono " + clienteTelefono + ": disponible "
                            + disponible.stripTrailingZeros().toPlainString()
                            + ", solicitado " + monto.stripTrailingZeros().toPlainString());
        }

        saldoCliente.setMontoDisponible(redondear(saldoCliente.getMontoDisponible().subtract(monto)));
        saldoClienteRepository.save(saldoCliente);
    }

    // No estaba en la lista de metodos pedida explicitamente, pero es el mecanismo
    // simetrico a usarSaldo() para el caso "sumar saldo al cancelar un pedido con
    // adelanto" (punto 2): busca por telefono, crea el registro si no existe, y
    // acumula el monto. Vive aca (no en PedidoService) para no dispersar la logica
    // de SaldoCliente fuera de su propio service.
    @Transactional
    public void agregarSaldo(String clienteTelefono, String clienteNombre, BigDecimal monto) {
        SaldoCliente saldoCliente = saldoClienteRepository.findByClienteTelefono(clienteTelefono)
                .orElseGet(() -> SaldoCliente.builder()
                        .clienteTelefono(clienteTelefono)
                        .clienteNombre(clienteNombre)
                        .montoDisponible(BigDecimal.ZERO)
                        .build());

        saldoCliente.setClienteNombre(clienteNombre);
        saldoCliente.setMontoDisponible(redondear(saldoCliente.getMontoDisponible().add(monto)));
        saldoClienteRepository.save(saldoCliente);
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
