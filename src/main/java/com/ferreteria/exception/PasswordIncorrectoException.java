package com.ferreteria.exception;

public class PasswordIncorrectoException extends RuntimeException {

    public PasswordIncorrectoException(String mensaje) {
        super(mensaje);
    }
}
