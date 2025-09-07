package com.pruebatecnica.meli.dominio.especificacion;

import com.pruebatecnica.meli.dominio.modelo.Producto;

public class EspecificacionPorPrecio implements Especificacion<Producto> {
    private final Double precioMin;
    private final Double precioMax;

    public EspecificacionPorPrecio(Double precioMin, Double precioMax) {
        this.precioMin = precioMin;
        this.precioMax = precioMax;
    }

    @Override
    public boolean esSatisfechoPor(Producto producto) {
        double precio = producto.precio();

        if (precioMin != null && precio < precioMin) return false;
        if (precioMax != null && precio > precioMax) return false;

        return true;
    }
}
