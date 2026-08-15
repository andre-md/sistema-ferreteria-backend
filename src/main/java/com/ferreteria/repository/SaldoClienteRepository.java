package com.ferreteria.repository;

import com.ferreteria.model.SaldoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaldoClienteRepository extends JpaRepository<SaldoCliente, Long> {

    Optional<SaldoCliente> findByClienteTelefono(String clienteTelefono);
}
