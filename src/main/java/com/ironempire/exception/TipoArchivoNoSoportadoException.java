package com.ironempire.exception;

public class TipoArchivoNoSoportadoException extends RuntimeException {

    public TipoArchivoNoSoportadoException(String mensaje) {
        super(mensaje);
    }
}