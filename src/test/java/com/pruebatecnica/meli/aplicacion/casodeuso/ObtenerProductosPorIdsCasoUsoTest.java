package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.compartido.excepciones.CantidadIdsInvalidaException;
import com.pruebatecnica.meli.compartido.excepciones.ParametrosInvalidosException;
import com.pruebatecnica.meli.compartido.utilidad.ProductoConstantes;
import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObtenerProductosPorIdsCasoUsoTest {

    @Mock
    private ProductoRepositorio productoRepositorio;

    private ObtenerProductosPorIdsCasoUso obtenerProductosPorIdsCasoUso;

    private static final String LISTA_VACIA = "La lista de IDs no puede estar vacía";
    private static final String LISTA_NULA = "La lista de IDs no puede ser nula";
    private static final String IDS_POSITIVOS = "Todos los IDs deben ser mayores a 0";

    private void initCasoUso() {
        obtenerProductosPorIdsCasoUso = new ObtenerProductosPorIdsCasoUso(productoRepositorio);
    }

    private Producto crearProducto(long id, String nombre) {
        return new Producto(id, nombre, "url" + id, "desc" + id, 100.0, 4.0, "cat", "marca", Collections.emptyList());
    }

    private void assertParametrosInvalidos(List<Long> ids, String mensajeEsperado) {
        initCasoUso();
        ParametrosInvalidosException excepcion = assertThrows(
                ParametrosInvalidosException.class,
                () -> obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(ids)
        );
        assertEquals(mensajeEsperado, excepcion.getMessage());
        verify(productoRepositorio, never()).obtenerProductosPorIds(any());
    }

    private void assertParametrosInvalidosNulo() {
        initCasoUso();
        ParametrosInvalidosException excepcion = assertThrows(
                ParametrosInvalidosException.class,
                () -> obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(null)
        );
        assertEquals(LISTA_NULA, excepcion.getMessage());
        verify(productoRepositorio, never()).obtenerProductosPorIds(any());
    }

    @Test
    void deberiaObtenerProductosConIdsValidos() {
        initCasoUso();

        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<Producto> productosEsperados = Arrays.asList(
                crearProducto(1L, "Producto 1"),
                crearProducto(2L, "Producto 2"),
                crearProducto(3L, "Producto 3")
        );
        when(productoRepositorio.obtenerProductosPorIds(ids)).thenReturn(productosEsperados);

        List<Producto> resultado = obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(ids);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(productoRepositorio).obtenerProductosPorIds(ids);
    }

    @Test
    void deberiaObtenerProductosCuandoCantidadEsElMinimo() {
        initCasoUso();

        List<Long> ids = Arrays.asList(1L, 2L);
        List<Producto> productosEsperados = Arrays.asList(
                crearProducto(1L, "Producto 1"),
                crearProducto(2L, "Producto 2")
        );
        when(productoRepositorio.obtenerProductosPorIds(ids)).thenReturn(productosEsperados);

        List<Producto> resultado = obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(ids);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(productoRepositorio).obtenerProductosPorIds(ids);
    }

    @Test
    void deberiaObtenerProductosCuandoCantidadEsElMaximo() {
        initCasoUso();

        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L);
        List<Producto> productosEsperados = Arrays.asList(
                crearProducto(1L, "Producto 1"),
                crearProducto(2L, "Producto 2"),
                crearProducto(3L, "Producto 3"),
                crearProducto(4L, "Producto 4"),
                crearProducto(5L, "Producto 5")
        );
        when(productoRepositorio.obtenerProductosPorIds(ids)).thenReturn(productosEsperados);

        List<Producto> resultado = obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(ids);

        assertNotNull(resultado);
        assertEquals(5, resultado.size());
        verify(productoRepositorio).obtenerProductosPorIds(ids);
    }

    @Test
    void deberiaLanzarCantidadIdsInvalidaExceptionConMenosDelMinimo() {
        initCasoUso();

        List<Long> ids = List.of(1L);

        CantidadIdsInvalidaException exception = assertThrows(
                CantidadIdsInvalidaException.class,
                () -> obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(ids)
        );

        assertEquals(ids.size(), exception.getCantidadRecibida());
        assertEquals(ProductoConstantes.MINIMO_IDS_CONSULTA, exception.getMinimo());
        assertEquals(ProductoConstantes.MAXIMO_IDS_CONSULTA, exception.getMaximo());
        assertEquals(String.format(ProductoConstantes.ERROR_CANTIDAD_IDS, ProductoConstantes.MINIMO_IDS_CONSULTA, ProductoConstantes.MAXIMO_IDS_CONSULTA, ids.size()), exception.getMessage());
        verify(productoRepositorio, never()).obtenerProductosPorIds(any());
    }

    @Test
    void deberiaLanzarCantidadIdsInvalidaExceptionConMasDelMaximo() {
        initCasoUso();

        List<Long> ids = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L);

        CantidadIdsInvalidaException exception = assertThrows(
                CantidadIdsInvalidaException.class,
                () -> obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(ids)
        );

        assertEquals(ids.size(), exception.getCantidadRecibida());
        assertEquals(ProductoConstantes.MINIMO_IDS_CONSULTA, exception.getMinimo());
        assertEquals(ProductoConstantes.MAXIMO_IDS_CONSULTA, exception.getMaximo());
        assertEquals(String.format(ProductoConstantes.ERROR_CANTIDAD_IDS, ProductoConstantes.MINIMO_IDS_CONSULTA, ProductoConstantes.MAXIMO_IDS_CONSULTA, ids.size()), exception.getMessage());
        verify(productoRepositorio, never()).obtenerProductosPorIds(any());
    }

    @Test
    void deberiaLanzarParametrosInvalidosExceptionConListaVacia() {
        assertParametrosInvalidos(Collections.emptyList(), LISTA_VACIA);
    }

    @Test
    void deberiaLanzarParametrosInvalidosExceptionConListaNula() {
        assertParametrosInvalidosNulo();
    }

    @Test
    void deberiaLanzarParametrosInvalidosExceptionCuandoHayIdCero() {
        assertParametrosInvalidos(Arrays.asList(0L, 2L), IDS_POSITIVOS);
    }

    @Test
    void deberiaLanzarParametrosInvalidosExceptionCuandoHayIdNegativo() {
        assertParametrosInvalidos(Arrays.asList(-1L, 2L), IDS_POSITIVOS);
    }

    @Test
    void deberiaLanzarParametrosInvalidosExceptionCuandoHayIdNulo() {
        assertParametrosInvalidos(Arrays.asList(1L, null), IDS_POSITIVOS);
    }
}
