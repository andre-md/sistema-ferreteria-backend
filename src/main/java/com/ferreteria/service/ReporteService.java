package com.ferreteria.service;

import com.ferreteria.dto.GananciaMensualResponse;
import com.ferreteria.dto.MovimientoStockMensualResponse;
import com.ferreteria.dto.ProductoMasVendidoResponse;
import com.ferreteria.dto.VentasMensualesResponse;
import com.ferreteria.model.Pedido;
import com.ferreteria.model.ProductoProveedor;
import com.ferreteria.model.TipoCambio;
import com.ferreteria.model.enums.EstadoPago;
import com.ferreteria.model.enums.EstadoPedido;
import com.ferreteria.model.enums.Moneda;
import com.ferreteria.model.enums.TipoMovimiento;
import com.ferreteria.repository.DetallePedidoRepository;
import com.ferreteria.repository.MovimientoInventarioRepository;
import com.ferreteria.repository.MovimientoStockProjection;
import com.ferreteria.repository.PedidoRepository;
import com.ferreteria.repository.ProductoProveedorRepository;
import com.ferreteria.repository.ProductoVentaProjection;
import com.ferreteria.repository.TipoCambioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoProveedorRepository productoProveedorRepository;
    private final TipoCambioRepository tipoCambioRepository;

    public VentasMensualesResponse obtenerVentasMensuales(Integer mes, Integer anio) {
        LocalDateTime[] rango = calcularRangoMes(mes, anio);

        List<Pedido> pedidos = pedidoRepository
                .findByEstadoPagoAndEstadoPedidoAndFechaCreacionGreaterThanEqualAndFechaCreacionLessThan(
                        EstadoPago.PAGADO, EstadoPedido.ACTIVO, rango[0], rango[1]);

        BigDecimal totalVentas = pedidos.stream()
                .map(Pedido::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentasMensualesResponse(redondear(totalVentas), pedidos.size());
    }

    public List<ProductoMasVendidoResponse> obtenerProductosMasVendidos(Integer mes, Integer anio, Integer limite) {
        LocalDateTime[] rango = calcularRangoMes(mes, anio);
        int limiteFinal = limite != null ? limite : 5;

        return detallePedidoRepository.obtenerVentasPorProducto(EstadoPedido.ACTIVO, rango[0], rango[1]).stream()
                .limit(limiteFinal)
                .map(v -> new ProductoMasVendidoResponse(
                        v.getProductoId(),
                        v.getProductoNombre(),
                        v.getCantidadVendida(),
                        redondear(v.getTotalGenerado())))
                .toList();
    }

    public GananciaMensualResponse calcularGananciaMensual(Integer mes, Integer anio) {
        LocalDateTime[] rango = calcularRangoMes(mes, anio);
        List<ProductoVentaProjection> ventas = detallePedidoRepository.obtenerVentasPorProducto(EstadoPedido.ACTIVO, rango[0], rango[1]);

        BigDecimal tipoCambio = tipoCambioRepository.findTopByOrderByFechaDesc().map(TipoCambio::getValor).orElse(null);

        BigDecimal gananciaTotal = BigDecimal.ZERO;
        int excluidos = 0;

        for (ProductoVentaProjection venta : ventas) {
            ProductoProveedor masBarato = productoProveedorRepository
                    .findByProductoIdOrderByPrecioCostoAsc(venta.getProductoId())
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (masBarato == null) {
                excluidos++;
                continue;
            }

            BigDecimal costoEnSoles = aSolesONull(masBarato.getPrecioCosto(), masBarato.getMoneda(), tipoCambio);
            BigDecimal totalGeneradoEnSoles = aSolesONull(venta.getTotalGenerado(), venta.getMoneda(), tipoCambio);

            if (costoEnSoles == null || totalGeneradoEnSoles == null) {
                excluidos++;
                continue;
            }

            BigDecimal gananciaProducto = totalGeneradoEnSoles.subtract(costoEnSoles.multiply(venta.getCantidadVendida()));
            gananciaTotal = gananciaTotal.add(gananciaProducto);
        }

        return new GananciaMensualResponse(redondear(gananciaTotal), excluidos);
    }

    public List<MovimientoStockMensualResponse> obtenerMovimientosStockMensual(Integer mes, Integer anio) {
        LocalDateTime[] rango = calcularRangoMes(mes, anio);

        List<MovimientoStockProjection> resumen = movimientoInventarioRepository.obtenerResumenMensual(
                TipoMovimiento.ENTRADA, TipoMovimiento.SALIDA, rango[0], rango[1]);

        return resumen.stream()
                .map(m -> new MovimientoStockMensualResponse(
                        m.getProductoId(),
                        m.getProductoNombre(),
                        redondear(m.getTotalEntradas()),
                        redondear(m.getTotalSalidas())))
                .toList();
    }

    // Devuelve null si hace falta convertir de USD y no hay ningun tipo de cambio
    // registrado - el llamador lo trata como "no se pudo calcular con confianza".
    private BigDecimal aSolesONull(BigDecimal monto, Moneda moneda, BigDecimal tipoCambio) {
        if (moneda == Moneda.PEN) {
            return monto;
        }
        if (tipoCambio == null) {
            return null;
        }
        return monto.multiply(tipoCambio);
    }

    private LocalDateTime[] calcularRangoMes(Integer mes, Integer anio) {
        LocalDate hoy = LocalDate.now();
        int mesFinal = mes != null ? mes : hoy.getMonthValue();
        int anioFinal = anio != null ? anio : hoy.getYear();

        if (mesFinal < 1 || mesFinal > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }

        YearMonth mesConsultado = YearMonth.of(anioFinal, mesFinal);
        LocalDateTime inicio = mesConsultado.atDay(1).atStartOfDay();
        LocalDateTime fin = mesConsultado.plusMonths(1).atDay(1).atStartOfDay();

        return new LocalDateTime[]{inicio, fin};
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }
}
