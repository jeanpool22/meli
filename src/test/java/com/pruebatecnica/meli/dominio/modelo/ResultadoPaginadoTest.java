package com.pruebatecnica.meli.dominio.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ResultadoPaginadoTest {

    private static final Long PRODUCTO_ID_1 = 1L;
    private static final Long PRODUCTO_ID_2 = 2L;
    private static final Long PRODUCTO_ID_3 = 3L;
    private static final String NOMBRE_CELULAR = "Celular Samsung";
    private static final String NOMBRE_LAPTOP = "Laptop Dell";
    private static final String NOMBRE_TABLET = "Tablet iPad";
    private static final String IMAGEN_URL = "http://ejemplo.com/imagen.jpg";
    private static final String DESCRIPCION = "Descripción del producto";
    private static final double PRECIO_500_00 = 500.00;
    private static final double PRECIO_1200_00 = 1200.00;
    private static final double PRECIO_1800_00 = 1800.00;
    private static final double CALIFICACION_4_5 = 4.5;
    private static final String CATEGORIA_ELECTRONICA = "Electrónica";
    private static final String MARCA_SAMSUNG = "Samsung";
    private static final List<String> ESPECIFICACIONES_DEFAULT = List.of("spec1", "spec2");
    private static final int PAGINA_0 = 0;
    private static final int PAGINA_1 = 1;

    private Producto crearProducto(Long id, String nombre, double precio) {
        return new Producto(
                id, nombre, IMAGEN_URL, DESCRIPCION, precio,
                CALIFICACION_4_5, CATEGORIA_ELECTRONICA, MARCA_SAMSUNG, ESPECIFICACIONES_DEFAULT
        );
    }

    @Test
    @DisplayName("Debe crear resultado paginado con primera página")
    void crearResultadoPaginado_primeraPagina() {
        List<Producto> productos = List.of(
                crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, PRECIO_500_00),
                crearProducto(PRODUCTO_ID_2, NOMBRE_LAPTOP, PRECIO_1200_00)
        );
        long totalElementos = 5L;
        int totalPaginas = 3;

        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(
                productos, PAGINA_0, totalPaginas, totalElementos);

        assertAll("Verificar primera página",
            () -> assertEquals(2, resultado.elementos().size()),
            () -> assertEquals(PAGINA_0, resultado.paginaActual()),
            () -> assertEquals(totalPaginas, resultado.totalPaginas()),
            () -> assertEquals(totalElementos, resultado.totalElementos()),
            () -> assertEquals(NOMBRE_CELULAR, resultado.elementos().getFirst().nombre()),
            () -> assertEquals(NOMBRE_LAPTOP, resultado.elementos().get(1).nombre())
        );
    }

    @Test
    @DisplayName("Debe crear resultado paginado con página intermedia")
    void crearResultadoPaginado_paginaIntermedia() {
        List<Producto> productos = List.of(
                crearProducto(PRODUCTO_ID_3, NOMBRE_TABLET, PRECIO_1800_00)
        );
        long totalElementos = 7L;
        int totalPaginas = 4;

        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(
                productos, PAGINA_1, totalPaginas, totalElementos);

        assertAll("Verificar página intermedia",
            () -> assertEquals(1, resultado.elementos().size()),
            () -> assertEquals(PAGINA_1, resultado.paginaActual()),
            () -> assertEquals(totalPaginas, resultado.totalPaginas()),
            () -> assertEquals(totalElementos, resultado.totalElementos()),
            () -> assertEquals(NOMBRE_TABLET, resultado.elementos().getFirst().nombre())
        );
    }

    @Test
    @DisplayName("Debe crear resultado paginado vacío")
    void crearResultadoPaginado_vacio() {
        List<Producto> productosVacios = List.of();
        long totalElementos = 0L;
        int totalPaginas = 0;

        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(
                productosVacios, PAGINA_0, totalPaginas, totalElementos);

        assertAll("Verificar resultado vacío",
            () -> assertTrue(resultado.elementos().isEmpty()),
            () -> assertEquals(PAGINA_0, resultado.paginaActual()),
            () -> assertEquals(0, resultado.totalPaginas()),
            () -> assertEquals(0L, resultado.totalElementos())
        );
    }

    @Test
    @DisplayName("Debe crear resultado paginado con una sola página")
    void crearResultadoPaginado_unaSolaPagina() {
        List<Producto> productos = List.of(
                crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, PRECIO_500_00),
                crearProducto(PRODUCTO_ID_2, NOMBRE_LAPTOP, PRECIO_1200_00),
                crearProducto(PRODUCTO_ID_3, NOMBRE_TABLET, PRECIO_1800_00)
        );
        long totalElementos = 3L;
        int totalPaginas = 1;

        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(
                productos, PAGINA_0, totalPaginas, totalElementos);

        assertAll("Verificar una sola página",
            () -> assertEquals(3, resultado.elementos().size()),
            () -> assertEquals(PAGINA_0, resultado.paginaActual()),
            () -> assertEquals(1, resultado.totalPaginas()),
            () -> assertEquals(3L, resultado.totalElementos())
        );
    }

    @Test
    @DisplayName("Debe validar que elementos no sean null")
    void crearResultadoPaginado_elementosNoNull() {
        List<Producto> productos = List.of(crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, PRECIO_500_00));
        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(productos, PAGINA_0, 1, 1L);

        assertNotNull(resultado.elementos());
        assertFalse(resultado.elementos().isEmpty());
    }
}
