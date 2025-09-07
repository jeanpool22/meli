package com.pruebatecnica.meli.dominio.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    private static final Long PRODUCTO_ID_1 = 1L;
    private static final Long PRODUCTO_ID_2 = 2L;
    private static final Long PRODUCTO_ID_3 = 3L;
    private static final String NOMBRE_PRODUCTO = "Producto Test";
    private static final String NOMBRE_INMUTABLE = "Producto Inmutable";
    private static final String NOMBRE_CELULAR = "Celular Samsung";
    private static final String NOMBRE_LAPTOP = "Laptop Dell";
    private static final String IMAGEN_URL = "http://ejemplo.com/imagen.jpg";
    private static final String IMAGEN_URL_2 = "http://ejemplo.com/imagen2.jpg";
    private static final String DESCRIPCION = "Descripción del producto test";
    private static final String DESCRIPCION_2 = "Descripción del producto inmutable";
    private static final double PRECIO_99_99 = 99.99;
    private static final double PRECIO_49_99 = 49.99;
    private static final double PRECIO_1500_00 = 1500.00;
    private static final double CALIFICACION_4_8 = 4.8;
    private static final double CALIFICACION_4_0 = 4.0;
    private static final double CALIFICACION_4_5 = 4.5;
    private static final String CATEGORIA_TEST = "categoria test";
    private static final String CATEGORIA_INMUTABLE = "categoria inmutable";
    private static final String CATEGORIA_ELECTRONICA = "Electrónica";
    private static final String CATEGORIA_INFORMATICA = "Informática";
    private static final String MARCA_TEST = "marca test";
    private static final String MARCA_INMUTABLE = "marca inmutable";
    private static final String MARCA_SAMSUNG = "Samsung";
    private static final String MARCA_DELL = "Dell";
    private static final List<String> ESPECIFICACIONES_DEFAULT = List.of("spec1", "spec2");
    private static final List<String> ESPECIFICACIONES_CELULAR = List.of("128GB", "5G", "Cámara 108MP");
    private static final List<String> ESPECIFICACIONES_LAPTOP = List.of("Intel i7", "16GB RAM", "512GB SSD");

    private Producto crearProductoPorDefecto() {
        return new Producto(
                PRODUCTO_ID_1,
                NOMBRE_PRODUCTO,
                IMAGEN_URL,
                DESCRIPCION,
                PRECIO_99_99,
                CALIFICACION_4_8,
                CATEGORIA_TEST,
                MARCA_TEST,
                ESPECIFICACIONES_DEFAULT);
    }

    @Test
    @DisplayName("Debe crear producto con todos los campos asignados correctamente")
    void crearProducto_debeAsignarCamposCorrectamente() {
        Producto producto = crearProductoPorDefecto();

        assertAll("Verificar todos los campos del producto",
            () -> assertEquals(PRODUCTO_ID_1, producto.idProducto()),
            () -> assertEquals(NOMBRE_PRODUCTO, producto.nombre()),
            () -> assertEquals(IMAGEN_URL, producto.imagenUrl()),
            () -> assertEquals(DESCRIPCION, producto.descripcion()),
            () -> assertEquals(PRECIO_99_99, producto.precio()),
            () -> assertEquals(CALIFICACION_4_8, producto.calificacion()),
            () -> assertEquals(CATEGORIA_TEST, producto.categoria()),
            () -> assertEquals(MARCA_TEST, producto.marca()),
            () -> assertEquals(ESPECIFICACIONES_DEFAULT, producto.especificaciones())
        );
    }

    @Test
    @DisplayName("El record Producto debe ser inmutable")
    void productoRecord_debeSerInmutable() {
        Producto producto = new Producto(
                PRODUCTO_ID_2,
                NOMBRE_INMUTABLE,
                IMAGEN_URL_2,
                DESCRIPCION_2,
                PRECIO_49_99,
                CALIFICACION_4_0,
                CATEGORIA_INMUTABLE,
                MARCA_INMUTABLE,
                ESPECIFICACIONES_DEFAULT);

        assertEquals(PRODUCTO_ID_2, producto.idProducto());
        assertNotNull(producto.especificaciones());
        assertEquals(2, producto.especificaciones().size());
    }

    @Test
    @DisplayName("Debe crear producto con especificaciones de electrónica")
    void crearProducto_conEspecificacionesElectronica() {
        Producto celular = new Producto(
                PRODUCTO_ID_2,
                NOMBRE_CELULAR,
                IMAGEN_URL,
                "Celular de alta gama",
                PRECIO_1500_00,
                CALIFICACION_4_5,
                CATEGORIA_ELECTRONICA,
                MARCA_SAMSUNG,
                ESPECIFICACIONES_CELULAR);

        assertAll("Verificar producto electrónico",
            () -> assertEquals(NOMBRE_CELULAR, celular.nombre()),
            () -> assertEquals(CATEGORIA_ELECTRONICA, celular.categoria()),
            () -> assertEquals(MARCA_SAMSUNG, celular.marca()),
            () -> assertTrue(celular.especificaciones().contains("128GB")),
            () -> assertTrue(celular.especificaciones().contains("5G")),
            () -> assertTrue(celular.especificaciones().contains("Cámara 108MP"))
        );
    }

    @Test
    @DisplayName("Debe crear producto con especificaciones de informática")
    void crearProducto_conEspecificacionesInformatica() {
        Producto laptop = new Producto(
                PRODUCTO_ID_3,
                NOMBRE_LAPTOP,
                IMAGEN_URL_2,
                "Laptop profesional",
                PRECIO_1500_00,
                CALIFICACION_4_8,
                CATEGORIA_INFORMATICA,
                MARCA_DELL,
                ESPECIFICACIONES_LAPTOP);

        assertAll("Verificar producto informático",
            () -> assertEquals(NOMBRE_LAPTOP, laptop.nombre()),
            () -> assertEquals(CATEGORIA_INFORMATICA, laptop.categoria()),
            () -> assertEquals(MARCA_DELL, laptop.marca()),
            () -> assertTrue(laptop.especificaciones().contains("Intel i7")),
            () -> assertTrue(laptop.especificaciones().contains("16GB RAM")),
            () -> assertTrue(laptop.especificaciones().contains("512GB SSD"))
        );
    }

    @Test
    @DisplayName("Debe validar que las especificaciones no sean nulas")
    void crearProducto_especificacionesNoNulas() {
        Producto producto = crearProductoPorDefecto();

        assertNotNull(producto.especificaciones());
        assertFalse(producto.especificaciones().isEmpty());
        assertEquals(2, producto.especificaciones().size());
    }

    @Test
    @DisplayName("Debe manejar especificaciones vacías correctamente")
    void crearProducto_conEspecificacionesVacias() {
        List<String> especificacionesVacias = List.of();

        Producto producto = new Producto(
                PRODUCTO_ID_1,
                NOMBRE_PRODUCTO,
                IMAGEN_URL,
                DESCRIPCION,
                PRECIO_99_99,
                CALIFICACION_4_8,
                CATEGORIA_TEST,
                MARCA_TEST,
                especificacionesVacias);

        assertNotNull(producto.especificaciones());
        assertTrue(producto.especificaciones().isEmpty());
    }

    @Test
    @DisplayName("Debe validar igualdad entre productos")
    void producto_debeValidarIgualdad() {
        Producto producto1 = crearProductoPorDefecto();
        Producto producto2 = crearProductoPorDefecto();

        assertEquals(producto1, producto2);
        assertEquals(producto1.hashCode(), producto2.hashCode());
    }

    @Test
    @DisplayName("Debe crear producto con diferentes tipos de precios")
    void crearProducto_conDiferentesTiposDePrecios() {
        Producto productoBarato = new Producto(
                1L, "Producto Barato", IMAGEN_URL, "desc", 0.99,
                CALIFICACION_4_0, CATEGORIA_TEST, MARCA_TEST, ESPECIFICACIONES_DEFAULT
        );

        Producto productoCaro = new Producto(
                2L, "Producto Caro", IMAGEN_URL, "desc", 9999.99,
                CALIFICACION_4_8, CATEGORIA_TEST, MARCA_TEST, ESPECIFICACIONES_DEFAULT
        );

        assertAll("Verificar diferentes precios",
            () -> assertEquals(0.99, productoBarato.precio()),
            () -> assertEquals(9999.99, productoCaro.precio()),
            () -> assertTrue(productoCaro.precio() > productoBarato.precio())
        );
    }

    @Test
    @DisplayName("Debe crear producto con diferentes calificaciones")
    void crearProducto_conDiferentesCalificaciones() {
        Producto productoCalificacionBaja = new Producto(
                1L, "Producto", IMAGEN_URL, "desc", PRECIO_99_99,
                1.0, CATEGORIA_TEST, MARCA_TEST, ESPECIFICACIONES_DEFAULT
        );

        Producto productoCalificacionAlta = new Producto(
                2L, "Producto", IMAGEN_URL, "desc", PRECIO_99_99,
                5.0, CATEGORIA_TEST, MARCA_TEST, ESPECIFICACIONES_DEFAULT
        );

        assertAll("Verificar diferentes calificaciones",
            () -> assertEquals(1.0, productoCalificacionBaja.calificacion()),
            () -> assertEquals(5.0, productoCalificacionAlta.calificacion()),
            () -> assertTrue(productoCalificacionAlta.calificacion() > productoCalificacionBaja.calificacion())
        );
    }
}
