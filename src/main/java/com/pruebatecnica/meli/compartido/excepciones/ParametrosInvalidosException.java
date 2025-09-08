package com.pruebatecnica.meli.compartido.excepciones;

public class ParametrosInvalidosException extends RuntimeException {
    public ParametrosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
