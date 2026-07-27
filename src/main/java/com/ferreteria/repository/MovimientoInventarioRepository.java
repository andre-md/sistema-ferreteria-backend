package com.ferreteria.repository;

import com.ferreteria.model.MovimientoInventario;
import com.ferreteria.model.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByProductoIdOrderByFechaCreacionDesc(Long productoId);

    List<MovimientoInventario> findByTipo(TipoMovimiento tipo);
}
