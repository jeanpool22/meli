package com.pruebatecnica.meli.compartido.excepciones;

import java.io.Serial;

public class ErrorLecturaJsonException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ErrorLecturaJsonException(String mensaje) {
        super(mensaje);
    }

    public ErrorLecturaJsonException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ErrorLecturaJsonException(Throwable causa) {
        super(causa);
    }
}


