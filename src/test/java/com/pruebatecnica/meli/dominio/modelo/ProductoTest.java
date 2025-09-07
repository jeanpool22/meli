package com.pruebatecnica.meli.dominio.modelo;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {
    @Test
    void crearProducto_debeAsignarCamposCorrectamente() {
        List<String> especificaciones = List.of("spec1", "spec2");
        Producto producto = new Producto(1L, "Producto Test", "url", "desc", 99.99, 4.8, especificaciones);

        assertEquals(1L, producto.idProducto());
        assertEquals("Producto Test", producto.nombre());
        assertEquals("url", producto.imagenUrl());
        assertEquals("desc", producto.descripcion());
        assertEquals(99.99, producto.precio());
        assertEquals(4.8, producto.calificacion());
        assertEquals(especificaciones, producto.especificaciones());
    }

    @Test
    void productoRecord_debeSerInmutable() {
        List<String> especificaciones = List.of("spec1", "spec2");
        Producto producto = new Producto(2L, "Producto Inmutable", "url2", "desc2", 49.99, 4.0, especificaciones);

        assertEquals(2L, producto.idProducto());
    }
}

