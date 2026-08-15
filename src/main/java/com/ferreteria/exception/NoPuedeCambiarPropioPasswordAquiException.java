package com.ferreteria.exception;

public class NoPuedeCambiarPropioPasswordAquiException extends RuntimeException {

    public NoPuedeCambiarPropioPasswordAquiException(String mensaje) {
        super(mensaje);
    }
}
