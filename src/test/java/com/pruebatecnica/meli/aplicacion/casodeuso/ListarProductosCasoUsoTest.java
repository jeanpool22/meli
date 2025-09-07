package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.modelo.ProductoCriteriosBusqueda;
import com.pruebatecnica.meli.dominio.modelo.ResultadoPaginado;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListarProductosCasoUsoTest {
    private static final String ERROR_DE_REPOSITORIO = "Error de repositorio";
    private static final String NOMBRE_1 = "Producto 1";
    private static final String NOMBRE_2 = "Producto 2";
    private static final String NOMBRE_CELULAR = "Celular";
    private static final String NOMBRE_CELULAR_PRO = "Celular Pro";
    private static final String NOMBRE_TABLET = "Tablet";
    private static final String URL_1 = "url1";
    private static final String URL_2 = "url2";
    private static final String URL_3 = "url3";
    private static final String DESC_1 = "desc1";
    private static final String DESC_2 = "desc2";
    private static final String DESC_3 = "desc3";
    private static final String CATEGORIA_1 = "Categoria1";
    private static final String CATEGORIA_2 = "Categoria2";
    private static final String CATEGORIA_ELECTRONICA = "Electrónica";
    private static final String CATEGORIA_HOGAR = "Hogar";
    private static final String MARCA = "";
    private static final List<String> ESPECIFICACIONES_1 = List.of("spec1", "spec2");
    private static final List<String> ESPECIFICACIONES_2 = List.of("specA");
    private static final List<String> ESPECIFICACIONES_3 = List.of("specB");
    private static final double PRECIO_1 = 100.0;
    private static final double PRECIO_2 = 200.0;
    private static final double PRECIO_3 = 300.0;
    private static final double CALIFICACION_1 = 4.5;
    private static final double CALIFICACION_2 = 4.7;
    private static final double CALIFICACION_3 = 4.8;

    private ProductoRepositorio productoRepositorio;
    private ListarProductosCasoUso listarProductosCasoUso;

    private Producto crearProducto(Long id, String nombre, String url, String desc, double precio, double calificacion, String categoria, String marca, List<String> especificaciones) {
        return new Producto(id, nombre, url, desc, precio, calificacion, categoria, marca, especificaciones);
    }

    private ProductoCriteriosBusqueda criteriosBusqueda(Optional<String> nombre, Optional<String> categoria, Optional<Double> precioMin, Optional<Double> precioMax, int pagina, int tamanioPagina) {
        return new ProductoCriteriosBusqueda(nombre, categoria, precioMin, precioMax, pagina, tamanioPagina);
    }

    @BeforeEach
    void setUp() {
        productoRepositorio = mock(ProductoRepositorio.class);
        listarProductosCasoUso = new ListarProductosCasoUso(productoRepositorio);
    }

    @Test
    void listarProductos_debeRetornarListaDeProductos() {
        Producto producto1 = crearProducto(1L, NOMBRE_1, URL_1, DESC_1, PRECIO_1, CALIFICACION_1, CATEGORIA_1, MARCA, ESPECIFICACIONES_1);
        Producto producto2 = crearProducto(2L, NOMBRE_2, URL_2, DESC_2, PRECIO_2, CALIFICACION_2, CATEGORIA_2, MARCA, ESPECIFICACIONES_2);
        when(productoRepositorio.listarProductos()).thenReturn(Arrays.asList(producto1, producto2));

        ProductoCriteriosBusqueda criterios = criteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        ResultadoPaginado<Producto> resultado = listarProductosCasoUso.listarProductos(criterios);

        assertNotNull(resultado);
        assertEquals(2, resultado.elementos().size());
        assertEquals(NOMBRE_1, resultado.elementos().get(0).nombre());
        assertEquals(NOMBRE_2, resultado.elementos().get(1).nombre());
    }

    @Test
    void listarProductos_debeRetornarListaVaciaSiNoHayProductos() {
        when(productoRepositorio.listarProductos()).thenReturn(Collections.emptyList());
        ProductoCriteriosBusqueda criterios = criteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        ResultadoPaginado<Producto> resultado = listarProductosCasoUso.listarProductos(criterios);
        assertNotNull(resultado);
        assertTrue(resultado.elementos().isEmpty());
    }

    @Test
    void listarProductos_debePropagarExcepcionSiRepositorioFalla() {
        when(productoRepositorio.listarProductos()).thenThrow(new RuntimeException(ERROR_DE_REPOSITORIO));
        ProductoCriteriosBusqueda criterios = criteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> listarProductosCasoUso.listarProductos(criterios));
        assertEquals(ERROR_DE_REPOSITORIO, ex.getMessage());
    }

    @Test
    void listarProductos_conPaginacionYFiltradoPorNombre() {
        Producto producto1 = crearProducto(1L, NOMBRE_CELULAR, URL_1, DESC_1, PRECIO_1, CALIFICACION_1, "", "", List.of("spec1"));
        Producto producto2 = crearProducto(2L, NOMBRE_CELULAR_PRO, URL_2, DESC_2, PRECIO_2, CALIFICACION_2, "", "", ESPECIFICACIONES_2);
        Producto producto3 = crearProducto(3L, NOMBRE_TABLET, URL_3, DESC_3, PRECIO_3, CALIFICACION_3, "", "", ESPECIFICACIONES_3);
        when(productoRepositorio.listarProductos()).thenReturn(Arrays.asList(producto1, producto2, producto3));

        ProductoCriteriosBusqueda criterios = criteriosBusqueda(Optional.of(NOMBRE_CELULAR), Optional.empty(), Optional.empty(), Optional.empty(), 0, 1);
        ResultadoPaginado<Producto> resultado = listarProductosCasoUso.listarProductos(criterios);
        assertEquals(1, resultado.elementos().size());
        assertEquals(NOMBRE_CELULAR, resultado.elementos().getFirst().nombre());
        assertEquals(2, resultado.totalElementos());
        assertEquals(2, resultado.totalPaginas());
    }

    @Test
    void listarProductos_filtradoPorCategoriaYPrecio() {
        Producto producto1 = crearProducto(1L, NOMBRE_CELULAR, URL_1, DESC_1, PRECIO_1, CALIFICACION_1, CATEGORIA_ELECTRONICA, "", ESPECIFICACIONES_1);
        Producto producto2 = crearProducto(2L, NOMBRE_CELULAR_PRO, URL_2, DESC_2, PRECIO_2, CALIFICACION_2, CATEGORIA_ELECTRONICA, "", ESPECIFICACIONES_2);
        Producto producto3 = crearProducto(3L, NOMBRE_TABLET, URL_3, DESC_3, PRECIO_3, CALIFICACION_3, CATEGORIA_HOGAR, "", ESPECIFICACIONES_3);
        when(productoRepositorio.listarProductos()).thenReturn(Arrays.asList(producto1, producto2, producto3));

        ProductoCriteriosBusqueda criterios = criteriosBusqueda(Optional.empty(), Optional.of(CATEGORIA_ELECTRONICA), Optional.of(150.0), Optional.of(250.0), 0, 10);
        ResultadoPaginado<Producto> resultado = listarProductosCasoUso.listarProductos(criterios);

        assertEquals(1, resultado.elementos().size());
        assertEquals(NOMBRE_CELULAR_PRO, resultado.elementos().getFirst().nombre());
    }
}
