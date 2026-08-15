package com.ferreteria.exception;

public class SaldoClienteNoEncontradoException extends RuntimeException {

    public SaldoClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
