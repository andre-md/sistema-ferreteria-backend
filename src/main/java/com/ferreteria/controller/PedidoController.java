package com.ferreteria.controller;

import com.ferreteria.dto.ActualizarPagoRequest;
import com.ferreteria.dto.PedidoRequest;
import com.ferreteria.dto.PedidoResponse;
import com.ferreteria.dto.ReasignarPedidoRequest;
import com.ferreteria.model.enums.EstadoEntrega;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/api/pedidos")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crear(@Valid @RequestBody PedidoRequest request, Authentication authentication) {
        return pedidoService.crear(request, authentication.getName());
    }

    @PostMapping("/api/public/pedidos")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse crearPublico(@Valid @RequestBody PedidoRequest request) {
        return pedidoService.crear(request, null);
    }

    @GetMapping("/api/pedidos")
    public List<PedidoResponse> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/api/pedidos/sin-atender")
    public List<PedidoResponse> listarSinAtender() {
        return pedidoService.listarSinAtender();
    }

    @GetMapping("/api/pedidos/cancelados")
    public List<PedidoResponse> listarCancelados() {
        return pedidoService.listarCancelados();
    }

    @GetMapping("/api/pedidos/estado-pago/{estado}")
    public List<PedidoResponse> listarPorEstadoPago(@PathVariable EstadoPago estado) {
        return pedidoService.listarPorEstadoPago(estado);
    }

    @GetMapping("/api/pedidos/estado-entrega/{estado}")
    public List<PedidoResponse> listarPorEstadoEntrega(@PathVariable EstadoEntrega estado) {
        return pedidoService.listarPorEstadoEntrega(estado);
    }

    @GetMapping("/api/pedidos/{id}")
    public PedidoResponse obtenerPorId(@PathVariable Long id) {
        return pedidoService.obtenerPorId(id);
    }

    @PutMapping("/api/pedidos/{id}/asignar")
    public PedidoResponse asignarVendedor(@PathVariable Long id, Authentication authentication) {
        String rolUsuarioAutenticado = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("");
        return pedidoService.asignarVendedor(id, authentication.getName(), rolUsuarioAutenticado);
    }

    @PutMapping("/api/pedidos/{id}/reasignar")
    @PreAuthorize("hasRole('ADMIN')")
    public PedidoResponse reasignarAVendedor(@PathVariable Long id, @Valid @RequestBody ReasignarPedidoRequest request) {
        return pedidoService.reasignarAVendedor(id, request.vendedorId());
    }

    @PutMapping("/api/pedidos/{id}/pago")
    public PedidoResponse actualizarPago(@PathVariable Long id, @Valid @RequestBody ActualizarPagoRequest request) {
        return pedidoService.actualizarPago(id, request);
    }

    @PutMapping("/api/pedidos/{id}/entregar")
    public PedidoResponse marcarComoEntregado(@PathVariable Long id) {
        return pedidoService.marcarComoEntregado(id);
    }

    @DeleteMapping("/api/pedidos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
    }
}
