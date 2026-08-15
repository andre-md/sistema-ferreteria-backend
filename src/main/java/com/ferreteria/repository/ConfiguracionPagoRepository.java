package com.ferreteria.repository;

import com.ferreteria.model.ConfiguracionPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionPagoRepository extends JpaRepository<ConfiguracionPago, Long> {
}
