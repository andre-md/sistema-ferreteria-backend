package com.ferreteria.service;

import com.ferreteria.dto.ActualizarPagoRequest;
import com.ferreteria.dto.DetallePedidoRequest;
import com.ferreteria.dto.PedidoRequest;
import com.ferreteria.dto.PedidoResponse;
import com.ferreteria.exception.PedidoNoEncontradoException;
import com.ferreteria.exception.PedidoNoPagadoException;
import com.ferreteria.exception.PedidoYaAsignadoException;
import com.ferreteria.exception.PedidoYaCanceladoException;
import com.ferreteria.exception.PedidoYaPagadoException;
import com.ferreteria.exception.ProductoNoEncontradoException;
import com.ferreteria.exception.StockInsuficienteException;
import com.ferreteria.exception.UsuarioNoEncontradoException;
import com.ferreteria.mapper.PedidoMapper;
import com.ferreteria.model.DetallePedido;
import com.ferreteria.model.MovimientoInventario;
import com.ferreteria.model.Pedido;
import com.ferreteria.model.Producto;
import com.ferreteria.model.Usuario;
import com.ferreteria.model.enums.EstadoEntrega;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.model.enums.EstadoPedido;
import com.ferreteria.model.enums.RolUsuario;
import com.ferreteria.model.enums.TipoMovimiento;
import com.ferreteria.repository.MovimientoInventarioRepository;
import com.ferreteria.repository.PedidoRepository;
import com.ferreteria.repository.ProductoRepository;
import com.ferreteria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    @Transactional
    public PedidoResponse crear(PedidoRequest request, String emailUsuarioAutenticado) {
        Map<Long, Producto> productosPorId = new LinkedHashMap<>();
        Map<Long, BigDecimal> cantidadTotalPorProducto = new LinkedHashMap<>();

        for (DetallePedidoRequest detalleRequest : request.detalles()) {
            productosPorId.computeIfAbsent(detalleRequest.productoId(), this::buscarProductoOLanzar);
            cantidadTotalPorProducto.merge(detalleRequest.productoId(), redondear(detalleRequest.cantidad()), BigDecimal::add);
        }

        for (Map.Entry<Long, BigDecimal> entry : cantidadTotalPorProducto.entrySet()) {
            Producto producto = productosPorId.get(entry.getKey());
            BigDecimal cantidadSolicitada = entry.getValue();
            if (producto.getStockActual().compareTo(cantidadSolicitada) < 0) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para '" + producto.getNombre() + "': disponible "
                                + producto.getStockActual().stripTrailingZeros().toPlainString()
                                + ", solicitado " + cantidadSolicitada.stripTrailingZeros().toPlainString());
            }
        }

        Usuario usuario = emailUsuarioAutenticado != null ? buscarUsuarioPorEmailOLanzar(emailUsuarioAutenticado) : null;

        BigDecimal montoAdelanto = request.montoAdelanto() != null ? request.montoAdelanto() : BigDecimal.ZERO;
        EstadoPago estadoPago = montoAdelanto.compareTo(BigDecimal.ZERO) > 0 ? EstadoPago.CON_ADELANTO : EstadoPago.PENDIENTE;

        Pedido pedido = Pedido.builder()
                .clienteNombre(request.clienteNombre())
                .clienteTelefono(request.clienteTelefono())
                .estadoPago(estadoPago)
                .estadoEntrega(EstadoEntrega.PENDIENTE)
                .montoAdelanto(montoAdelanto)
                .montoTotal(BigDecimal.ZERO)
                .usuario(usuario)
                .build();

        BigDecimal montoTotal = BigDecimal.ZERO;
        for (DetallePedidoRequest detalleRequest : request.detalles()) {
            Producto producto = productosPorId.get(detalleRequest.productoId());
            DetallePedido detalle = PedidoMapper.toDetalleEntity(detalleRequest, producto, pedido);
            pedido.getDetalles().add(detalle);
            montoTotal = montoTotal.add(detalle.getCantidad().multiply(detalle.getPrecioUnitario()));
        }
        pedido.setMontoTotal(redondear(montoTotal));

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        for (Map.Entry<Long, BigDecimal> entry : cantidadTotalPorProducto.entrySet()) {
            Producto producto = productosPorId.get(entry.getKey());
            BigDecimal cantidadTotal = entry.getValue();

            producto.setStockActual(redondear(producto.getStockActual().subtract(cantidadTotal)));
            productoRepository.save(producto);

            registrarMovimiento(producto, TipoMovimiento.SALIDA, cantidadTotal,
                    "Pedido #" + pedidoGuardado.getId(), usuario);
        }

        return PedidoMapper.toResponse(pedidoGuardado);
    }

    public List<PedidoResponse> listarTodos() {
        return PedidoMapper.toResponseList(pedidoRepository.findByEstadoPedidoOrderByFechaCreacionDesc(EstadoPedido.ACTIVO));
    }

    public List<PedidoResponse> listarPorEstadoPago(EstadoPago estado) {
        return PedidoMapper.toResponseList(pedidoRepository.findByEstadoPagoAndEstadoPedido(estado, EstadoPedido.ACTIVO));
    }

    public List<PedidoResponse> listarPorEstadoEntrega(EstadoEntrega estado) {
        return PedidoMapper.toResponseList(pedidoRepository.findByEstadoEntregaAndEstadoPedido(estado, EstadoPedido.ACTIVO));
    }

    public List<PedidoResponse> listarSinAtender() {
        return PedidoMapper.toResponseList(pedidoRepository.findByUsuarioIsNullAndEstadoPedido(EstadoPedido.ACTIVO));
    }

    // Vista de auditoria/historial: unico lugar donde SI se ven los cancelados
    public List<PedidoResponse> listarCancelados() {
        return PedidoMapper.toResponseList(pedidoRepository.findByEstadoPedidoOrderByFechaCreacionDesc(EstadoPedido.CANCELADO));
    }

    public PedidoResponse obtenerPorId(Long id) {
        return PedidoMapper.toResponse(buscarPedidoOLanzar(id));
    }

    @Transactional
    public PedidoResponse asignarVendedor(Long pedidoId, String emailUsuarioAutenticado, String rolUsuarioAutenticado) {
        Pedido pedido = buscarPedidoOLanzar(pedidoId);
        Usuario usuarioActual = pedido.getUsuario();

        boolean yaAsignadoAOtro = usuarioActual != null && !usuarioActual.getEmail().equalsIgnoreCase(emailUsuarioAutenticado);
        boolean esAdmin = RolUsuario.ADMIN.name().equals(rolUsuarioAutenticado);

        if (yaAsignadoAOtro && !esAdmin) {
            throw new PedidoYaAsignadoException(
                    "El pedido #" + pedidoId + " ya esta asignado a " + usuarioActual.getNombre());
        }

        Usuario usuario = buscarUsuarioPorEmailOLanzar(emailUsuarioAutenticado);
        pedido.setUsuario(usuario);
        return PedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse reasignarAVendedor(Long pedidoId, Long vendedorId) {
        Pedido pedido = buscarPedidoOLanzar(pedidoId);
        Usuario vendedor = buscarUsuarioPorIdOLanzar(vendedorId);
        pedido.setUsuario(vendedor);
        return PedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse actualizarPago(Long id, ActualizarPagoRequest request) {
        Pedido pedido = buscarPedidoOLanzar(id);
        lanzarSiEstaCancelado(pedido);

        if (pedido.getEstadoPago() == EstadoPago.PAGADO) {
            throw new PedidoYaPagadoException("El pedido #" + id + " ya esta pagado en su totalidad");
        }

        BigDecimal nuevoAdelanto = redondear(pedido.getMontoAdelanto().add(request.montoPago()));

        if (nuevoAdelanto.compareTo(pedido.getMontoTotal()) >= 0) {
            pedido.setMontoAdelanto(pedido.getMontoTotal());
            pedido.setEstadoPago(EstadoPago.PAGADO);
        } else {
            pedido.setMontoAdelanto(nuevoAdelanto);
            pedido.setEstadoPago(EstadoPago.CON_ADELANTO);
        }

        return PedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse marcarComoEntregado(Long id) {
        Pedido pedido = buscarPedidoOLanzar(id);
        lanzarSiEstaCancelado(pedido);
        if (pedido.getEstadoPago() != EstadoPago.PAGADO) {
            throw new PedidoNoPagadoException(
                    "El pedido #" + id + " no esta pagado; no se puede marcar como entregado");
        }
        pedido.setEstadoEntrega(EstadoEntrega.ENTREGADO);
        return PedidoMapper.toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public void cancelar(Long id) {
        Pedido pedido = buscarPedidoOLanzar(id);
        lanzarSiEstaCancelado(pedido);

        Map<Long, Producto> productosPorId = new LinkedHashMap<>();
        Map<Long, BigDecimal> cantidadPorProducto = new LinkedHashMap<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            productosPorId.putIfAbsent(producto.getId(), producto);
            cantidadPorProducto.merge(producto.getId(), redondear(detalle.getCantidad()), BigDecimal::add);
        }

        for (Map.Entry<Long, BigDecimal> entry : cantidadPorProducto.entrySet()) {
            Producto producto = productosPorId.get(entry.getKey());
            BigDecimal cantidad = entry.getValue();

            producto.setStockActual(redondear(producto.getStockActual().add(cantidad)));
            productoRepository.save(producto);

            registrarMovimiento(producto, TipoMovimiento.ENTRADA, cantidad,
                    "Cancelacion de pedido #" + pedido.getId(), null);
        }

        // No se borra el registro: se preserva el historial completo (montoAdelanto,
        // montoTotal, detalles) para que el ADMIN pueda auditar cuanto habia pagado.
        pedido.setEstadoPedido(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private void registrarMovimiento(Producto producto, TipoMovimiento tipo, BigDecimal cantidad, String motivo, Usuario usuario) {
        MovimientoInventario movimiento = MovimientoInventario.builder()
                .producto(producto)
                .tipo(tipo)
                .cantidad(cantidad)
                .motivo(motivo)
                .usuario(usuario)
                .build();
        movimientoInventarioRepository.save(movimiento);
    }

    private Producto buscarProductoOLanzar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private Usuario buscarUsuarioPorEmailOLanzar(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    private Usuario buscarUsuarioPorIdOLanzar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    private Pedido buscarPedidoOLanzar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException("Pedido no encontrado con id: " + id));
    }

    private void lanzarSiEstaCancelado(Pedido pedido) {
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO) {
            throw new PedidoYaCanceladoException("El pedido #" + pedido.getId() + " esta cancelado");
        }
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
