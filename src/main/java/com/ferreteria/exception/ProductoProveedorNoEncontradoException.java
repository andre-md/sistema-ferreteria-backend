package com.ferreteria.exception;

public class ProductoProveedorNoEncontradoException extends RuntimeException {

    public ProductoProveedorNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
