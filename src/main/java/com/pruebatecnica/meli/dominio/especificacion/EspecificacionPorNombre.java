package com.pruebatecnica.meli.dominio.especificacion;

import com.pruebatecnica.meli.dominio.modelo.Producto;

public class EspecificacionPorNombre implements Especificacion<Producto> {
    private final String nombre;

    public EspecificacionPorNombre(String nombre) {
        this.nombre = nombre.toLowerCase();
    }

    @Override
    public boolean esSatisfechoPor(Producto producto) {
        return producto.nombre().toLowerCase().contains(nombre);
    }
}