package com.ironempire.exception;

public class ErrorAlmacenamientoException extends RuntimeException {
    /*
     * Se conserva la causa original para facilitar su diagnóstico en los registros
     * del servidor.
     */
    public ErrorAlmacenamientoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
