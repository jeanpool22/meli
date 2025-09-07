package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObtenerProductoPorIdCasoUsoTest {
    private static final String MENSAJE_BASE = ObtenerProductoPorIdCasoUso.NO_FUE_POSIBLE_ENCONTRAR_EL_PRODUCTO;
    private static final Long ID_PRODUCTO_EXISTENTE = 1L;
    private static final Long ID_PRODUCTO_NO_EXISTENTE = 0L;

    private ProductoRepositorio productoRepositorio;
    private ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso;

    @BeforeEach
    void setUp() {
        productoRepositorio = mock(ProductoRepositorio.class);
        obtenerProductoPorIdCasoUso = new ObtenerProductoPorIdCasoUso(productoRepositorio);
    }

    @Test
    void debeRetornarProductoCuandoExiste() {
        Producto productoMock = mock(Producto.class);
        when(productoRepositorio.obtenerProductoPorId(ID_PRODUCTO_EXISTENTE)).thenReturn(Optional.of(productoMock));

        Producto resultado = obtenerProductoPorIdCasoUso.obtenerProductoPorId(ID_PRODUCTO_EXISTENTE);

        assertNotNull(resultado);
        assertSame(productoMock, resultado);
        verify(productoRepositorio, times(1)).obtenerProductoPorId(ID_PRODUCTO_EXISTENTE);
        verifyNoMoreInteractions(productoRepositorio);
    }

    @Test
    void debeLanzarExcepcionCuandoNoExisteProducto() {
        when(productoRepositorio.obtenerProductoPorId(ID_PRODUCTO_NO_EXISTENTE)).thenReturn(Optional.empty());
        String mensajeEsperado = String.format(MENSAJE_BASE, ID_PRODUCTO_NO_EXISTENTE);

        ProductoNoEncontradoException ex = assertThrows(
                ProductoNoEncontradoException.class,
                () -> obtenerProductoPorIdCasoUso.obtenerProductoPorId(ID_PRODUCTO_NO_EXISTENTE)
        );

        assertEquals(mensajeEsperado, ex.getMessage());
        verify(productoRepositorio, times(1)).obtenerProductoPorId(ID_PRODUCTO_NO_EXISTENTE);
        verifyNoMoreInteractions(productoRepositorio);
    }
}

