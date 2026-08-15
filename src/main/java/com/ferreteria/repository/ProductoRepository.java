package com.ferreteria.repository;

import com.ferreteria.model.Producto;
import com.ferreteria.model.enums.Moneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrueOrderByNombreAsc();

    List<Producto> findByCategoriaIdAndActivoTrueOrderByNombreAsc(Long categoriaId);

    boolean existsByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrueOrderByNombreAsc(String nombre);

    List<Producto> findByMoneda(Moneda moneda);

    // Para reporte de stock bajo
    List<Producto> findByStockActualLessThanAndActivoTrueOrderByNombreAsc(BigDecimal umbral);
}
