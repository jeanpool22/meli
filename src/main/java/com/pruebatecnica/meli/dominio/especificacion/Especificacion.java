package com.pruebatecnica.meli.dominio.especificacion;

public interface Especificacion<T> {
    boolean esSatisfechoPor(T t);
}
