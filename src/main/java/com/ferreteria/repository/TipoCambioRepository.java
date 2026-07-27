package com.ferreteria.repository;

import com.ferreteria.model.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoCambioRepository extends JpaRepository<TipoCambio, Long> {

    // Tipo de cambio mas reciente
    Optional<TipoCambio> findTopByOrderByFechaDesc();

    List<TipoCambio> findAllByOrderByFechaDesc();
}
