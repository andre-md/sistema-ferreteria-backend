package com.ferreteria.exception;

public class PedidoNoPagadoException extends RuntimeException {

    public PedidoNoPagadoException(String mensaje) {
        super(mensaje);
    }
}
