package com.pruebatecnica.meli.compartido.excepciones;

import lombok.Getter;

@Getter
public class CantidadIdsInvalidaException extends RuntimeException {

    private final String mensaje;
    private final int minimo;
    private final int maximo;
    private final int cantidadRecibida;

    public CantidadIdsInvalidaException(String mensaje, int minimo, int maximo, int cantidadRecibida) {
        super(String.format(mensaje,minimo, maximo, cantidadRecibida));

        this.mensaje = mensaje;
        this.minimo = minimo;
        this.maximo = maximo;
        this.cantidadRecibida = cantidadRecibida;
    }

}
