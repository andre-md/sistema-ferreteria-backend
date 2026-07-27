package com.ferreteria.exception;

public class CategoriaNoEncontradaException extends RuntimeException {

    public CategoriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
