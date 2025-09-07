package com.pruebatecnica.meli.dominio.modelo;

import java.util.List;

public record Producto(
        Long idProducto,
        String nombre,
        String imagenUrl,
        String descripcion,
        double precio,
        double calificacion,
        String categoria,
        String marca,
        List<String> especificaciones
) {}
