package com.ferreteria.controller;

import com.ferreteria.dto.GananciaMensualResponse;
import com.ferreteria.dto.MovimientoStockMensualResponse;
import com.ferreteria.dto.ProductoMasVendidoResponse;
import com.ferreteria.dto.VentasMensualesResponse;
import com.ferreteria.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas-mensuales")
    public VentasMensualesResponse ventasMensuales(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio
    ) {
        return reporteService.obtenerVentasMensuales(mes, anio);
    }

    @GetMapping("/productos-mas-vendidos")
    public List<ProductoMasVendidoResponse> productosMasVendidos(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer limite
    ) {
        return reporteService.obtenerProductosMasVendidos(mes, anio, limite);
    }

    // Solo ADMIN: el margen de ganancia expone directamente el markup sobre cada
    // producto, informacion comercial que el staff de ventas no deberia ver.
    @GetMapping("/ganancia-mensual")
    @PreAuthorize("hasRole('ADMIN')")
    public GananciaMensualResponse gananciaMensual(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio
    ) {
        return reporteService.calcularGananciaMensual(mes, anio);
    }

    @GetMapping("/movimientos-stock-mensual")
    public List<MovimientoStockMensualResponse> movimientosStockMensual(
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer anio
    ) {
        return reporteService.obtenerMovimientosStockMensual(mes, anio);
    }
}
