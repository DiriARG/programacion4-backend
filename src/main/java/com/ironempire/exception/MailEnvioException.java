package com.ironempire.exception;

public class MailEnvioException extends RuntimeException {

    public MailEnvioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
