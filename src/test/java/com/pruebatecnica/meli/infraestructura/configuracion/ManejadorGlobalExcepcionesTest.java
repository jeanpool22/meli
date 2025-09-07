package com.pruebatecnica.meli.infraestructura.configuracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pruebatecnica.meli.compartido.excepciones.ErrorLecturaJsonException;
import com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException;
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
    private static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    private static final String NO_EXISTE_EL_PRODUCTO = "No existe el producto";
    private static final String FUENTE_DE_DATOS_NO_DISPONIBLE = "Fuente de datos no disponible";
    private static final String DATOS_DE_PRODUCTOS_INVALIDOS = "Datos de productos inválidos";
    private static final String ERROR_PARSEO = "Error parseo";
    private static final String ERROR_AL_LEER_DATOS_DE_PRODUCTOS = "Error al leer datos de productos";
    private static final String ERROR_INTERNO_DEL_SERVIDOR = "Error interno del servidor";
    private static final String ERROR_INESPERADO = "Error inesperado";
    private static final int CODIGO_DE_RESPUESTA_404 = 404;
    private static final int CODIGO_DE_RESPUESTA_500 = 500;
    private static final int CODIGO_DE_RESPUESTA_503 = 503;

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
}


