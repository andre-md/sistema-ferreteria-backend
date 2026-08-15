package com.ferreteria.repository;

import com.ferreteria.model.ProductoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, Long> {

    // Orden ascendente por precio: facilita comparar cotizaciones de un mismo producto
    List<ProductoProveedor> findByProductoIdOrderByPrecioCostoAsc(Long productoId);

    // Orden alfabetico por nombre de producto: listado legible para un proveedor
    List<ProductoProveedor> findByProveedorIdOrderByProductoNombreAsc(Long proveedorId);

    boolean existsByProveedorId(Long proveedorId);

    Optional<ProductoProveedor> findByProductoIdAndProveedorId(Long productoId, Long proveedorId);
}
