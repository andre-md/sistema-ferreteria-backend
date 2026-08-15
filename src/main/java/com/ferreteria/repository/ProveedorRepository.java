package com.ferreteria.repository;

import com.ferreteria.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByNombre(String nombre);

    boolean existsByNombreAndContactoWhatsapp(String nombre, String contactoWhatsapp);

    List<Proveedor> findAllByOrderByNombreAsc();
}
