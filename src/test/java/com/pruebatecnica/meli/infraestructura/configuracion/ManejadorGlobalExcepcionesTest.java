package com.pruebatecnica.meli.infraestructura.configuracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pruebatecnica.meli.compartido.excepciones.ErrorLecturaJsonException;
import com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException;
import com.pruebatecnica.meli.compartido.excepciones.ParametrosInvalidosException;
import com.pruebatecnica.meli.compartido.excepciones.CantidadIdsInvalidaException;
import com.pruebatecnica.meli.compartido.utilidad.ErrorRespuesta;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManejadorGlobalExcepcionesTest {

    private static final String RUTA_PRODUCTOS = "/productos";
    private static final String RUTA_PRODUCTOS_99 = "/productos/99";
    private static final String RUTA_PRODUCTOS_COMPARADOR = "/productos/comparador";
    private static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    private static final String NO_EXISTE_EL_PRODUCTO = "No existe el producto";
    private static final String FUENTE_DE_DATOS_NO_DISPONIBLE = "Fuente de datos no disponible";
    private static final String DATOS_DE_PRODUCTOS_INVALIDOS = "Datos de productos inválidos";
    private static final String ERROR_PARSEO = "Error parseo";
    private static final String ERROR_AL_LEER_DATOS_DE_PRODUCTOS = "Error al leer datos de productos";
    private static final String ERROR_INTERNO_DEL_SERVIDOR = "Error interno del servidor";
    private static final String ERROR_INESPERADO = "Error inesperado";
    private static final String PARAMETROS_INVALIDOS = "Parámetros inválidos";
    private static final String CANTIDAD_DE_IDS_INVALIDA = "Cantidad de IDs inválida";
    private static final int CODIGO_DE_RESPUESTA_404 = 404;
    private static final int CODIGO_DE_RESPUESTA_500 = 500;
    private static final int CODIGO_DE_RESPUESTA_503 = 503;
    private static final int CODIGO_DE_RESPUESTA_400 = 400;

    private final ManejadorGlobalExcepciones manejador = new ManejadorGlobalExcepciones();
    private final WebRequest webRequest = mock(WebRequest.class);

    @Test
    void manejarProductoNoEncontrado_debeRetornar404() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS_99);
        ProductoNoEncontradoException excepcion = new ProductoNoEncontradoException(NO_EXISTE_EL_PRODUCTO);

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarProductoNoEncontrado(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_404, respuesta.getStatusCode().value());
        assertEquals(PRODUCTO_NO_ENCONTRADO, respuesta.getBody().getTitulo());
        assertEquals(NO_EXISTE_EL_PRODUCTO, respuesta.getBody().getMensaje());
        assertEquals(RUTA_PRODUCTOS_99, respuesta.getBody().getRuta());
    }

    @Test
    void manejarErrorLecturaJson_archivoNoEncontrado_debeRetornar503() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS);
        ErrorLecturaJsonException excepcion =
                new ErrorLecturaJsonException(FUENTE_DE_DATOS_NO_DISPONIBLE, new FileNotFoundException());

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarErrorLecturaJson(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_503, respuesta.getStatusCode().value());
        assertEquals(FUENTE_DE_DATOS_NO_DISPONIBLE, respuesta.getBody().getMensaje());
    }

    @Test
    void manejarErrorLecturaJson_jsonCorrupto_debeRetornar500() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS);
        ErrorLecturaJsonException excepcion =
                new ErrorLecturaJsonException(DATOS_DE_PRODUCTOS_INVALIDOS, new JsonProcessingException(ERROR_PARSEO) {});

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarErrorLecturaJson(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_500, respuesta.getStatusCode().value());
        assertEquals(DATOS_DE_PRODUCTOS_INVALIDOS, respuesta.getBody().getMensaje());
    }

    @Test
    void manejarErrorLecturaJson_errorGeneral_debeRetornar500() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS);
        ErrorLecturaJsonException excepcion =
                new ErrorLecturaJsonException(ERROR_AL_LEER_DATOS_DE_PRODUCTOS, new RuntimeException());

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarErrorLecturaJson(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_500, respuesta.getStatusCode().value());
        assertEquals(ERROR_AL_LEER_DATOS_DE_PRODUCTOS, respuesta.getBody().getMensaje());
    }

    @Test
    void manejarExcepcionGeneral_debeRetornar500() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS);
        Exception excepcion = new Exception(ERROR_INESPERADO);

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarExcepcionGeneral(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_500, respuesta.getStatusCode().value());
        assertEquals(ERROR_INTERNO_DEL_SERVIDOR, respuesta.getBody().getTitulo());
        assertEquals(ERROR_INESPERADO, respuesta.getBody().getMensaje());
    }

    @Test
    void manejarParametrosInvalidos_debeRetornar400() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS_COMPARADOR);
        String mensaje = "Orden inválida: sort=precio,order=sideways";
        ParametrosInvalidosException excepcion = new ParametrosInvalidosException(mensaje);

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarParametrosInvalidos(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_400, respuesta.getStatusCode().value());
        assertEquals(PARAMETROS_INVALIDOS, respuesta.getBody().getTitulo());
        assertEquals(mensaje, respuesta.getBody().getMensaje());
        assertEquals(RUTA_PRODUCTOS_COMPARADOR, respuesta.getBody().getRuta());
    }

    @Test
    void manejarCantidadIdsInvalida_debeRetornar400() {
        when(webRequest.getDescription(false)).thenReturn(RUTA_PRODUCTOS_COMPARADOR);
        String plantilla = "Cantidad fuera de rango: min %d, max %d, recibidos %d";
        int min = 1, max = 50, recibidos = 0;
        CantidadIdsInvalidaException excepcion = new CantidadIdsInvalidaException(plantilla, min, max, recibidos);

        ResponseEntity<ErrorRespuesta> respuesta = manejador.manejarCantidadIdsInvalida(excepcion, webRequest);

        assertNotNull(respuesta.getBody());
        assertEquals(CODIGO_DE_RESPUESTA_400, respuesta.getStatusCode().value());
        assertEquals(CANTIDAD_DE_IDS_INVALIDA, respuesta.getBody().getTitulo());
        assertEquals(String.format(plantilla, min, max, recibidos), respuesta.getBody().getMensaje());
        assertEquals(RUTA_PRODUCTOS_COMPARADOR, respuesta.getBody().getRuta());
    }
}
