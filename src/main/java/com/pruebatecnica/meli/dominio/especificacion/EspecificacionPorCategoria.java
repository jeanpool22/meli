package com.pruebatecnica.meli.dominio.especificacion;

import com.pruebatecnica.meli.dominio.modelo.Producto;

public class EspecificacionPorCategoria implements Especificacion<Producto> {
    private final String categoria;

    public EspecificacionPorCategoria(String categoria) {
        this.categoria = categoria.toLowerCase();
    }

    @Override
    public boolean esSatisfechoPor(Producto producto) {
        return producto.categoria().toLowerCase().equals(categoria);
    }
}
