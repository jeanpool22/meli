package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListarProductosCasoUsoTest {
    private static final String ERROR_DE_REPOSITORIO = "Error de repositorio";

    private ProductoRepositorio productoRepositorio;
    private ListarProductosCasoUso listarProductosCasoUso;

    @BeforeEach
    void setUp() {
        productoRepositorio = mock(ProductoRepositorio.class);
        listarProductosCasoUso = new ListarProductosCasoUso(productoRepositorio);
    }

    @Test
    void listarProductos_debeRetornarListaDeProductos() {
        Producto producto1 = new Producto(1L, "Producto 1", "url1", "desc1", 100.0, 4.5, List.of("spec1", "spec2"));
        Producto producto2 = new Producto(2L, "Producto 2", "url2", "desc2", 200.0, 4.7, List.of("specA"));
        when(productoRepositorio.listarProductos()).thenReturn(Arrays.asList(producto1, producto2));

        List<Producto> resultado = listarProductosCasoUso.listarProductos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Producto 1", resultado.get(0).nombre());
        assertEquals("Producto 2", resultado.get(1).nombre());
    }

    @Test
    void listarProductos_debeRetornarListaVaciaSiNoHayProductos() {
        when(productoRepositorio.listarProductos()).thenReturn(Collections.emptyList());

        List<Producto> resultado = listarProductosCasoUso.listarProductos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void listarProductos_debePropagarExcepcionSiRepositorioFalla() {
        when(productoRepositorio.listarProductos()).thenThrow(new RuntimeException(ERROR_DE_REPOSITORIO));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> listarProductosCasoUso.listarProductos());
        assertEquals(ERROR_DE_REPOSITORIO, ex.getMessage());
    }
}

