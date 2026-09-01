package com.ironempire.exception;

public class RecursoExistenteException extends RuntimeException {
    public RecursoExistenteException(String mensaje) {
        /*
         * Pasa el mensaje a la clase padre (RuntimeException),
         * que lo almacena para poder recuperarlo mediante "getMessage()".
         */
        super(mensaje);
    }
}
