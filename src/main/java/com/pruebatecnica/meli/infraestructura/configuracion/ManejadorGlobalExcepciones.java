package com.pruebatecnica.meli.infraestructura.configuracion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pruebatecnica.meli.compartido.excepciones.CantidadIdsInvalidaException;
import com.pruebatecnica.meli.compartido.excepciones.ErrorLecturaJsonException;
import com.pruebatecnica.meli.compartido.excepciones.ParametrosInvalidosException;
import com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException;
import com.pruebatecnica.meli.compartido.utilidad.ErrorRespuesta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    private static final String PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    private static final String RECURSO_NO_ENCONTRADO = "Recurso no encontrado";
    private static final String FUENTE_DE_DATOS_NO_DISPONIBLE = "Fuente de datos no disponible";
    private static final String DATOS_DE_PRODUCTOS_INVALIDOS = "Datos de productos inválidos";
    private static final String ERROR_AL_LEER_DATOS_DE_PRODUCTOS = "Error al leer datos de productos";
    private static final String ERROR_INTERNO_DEL_SERVIDOR = "Error interno del servidor";

    private static final String LOG_PRODUCTO_NO_ENCONTRADO = "Producto no encontrado: {}";
    private static final String LOG_RUTA_NO_ENCONTRADA = "Ruta no encontrada: {}";
    private static final String LOG_ARCHIVO_NO_ENCONTRADO = "Archivo de productos no encontrado";
    private static final String LOG_ERROR_PARSEO_JSON = "Error de parseo del JSON de productos";
    private static final String LOG_ERROR_GENERAL_LECTURA = "Error general leyendo productos";
    private static final String LOG_ERROR_NO_CONTROLADO = "Error no controlado";

    // Cuando no se encuentra un producto específico
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> manejarProductoNoEncontrado(ProductoNoEncontradoException ex,
                                                                      WebRequest request) {
        log.warn(LOG_PRODUCTO_NO_ENCONTRADO, ex.getMessage());
        return respuesta(HttpStatus.NOT_FOUND, PRODUCTO_NO_ENCONTRADO, ex, request);
    }

    // Cuando no existe la ruta
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorRespuesta> manejarNoHandlerFound(NoHandlerFoundException ex,
                                                                WebRequest request) {
        log.warn(LOG_RUTA_NO_ENCONTRADA, ex.getRequestURL());
        return respuesta(HttpStatus.NOT_FOUND, RECURSO_NO_ENCONTRADO, ex, request);
    }

    // Problemas leyendo el JSON interno de productos
    @ExceptionHandler(ErrorLecturaJsonException.class)
    public ResponseEntity<ErrorRespuesta> manejarErrorLecturaJson(ErrorLecturaJsonException ex,
                                                                  WebRequest request) {
        Throwable causa = ex.getCause();

        if (causa instanceof FileNotFoundException) {
            log.error(LOG_ARCHIVO_NO_ENCONTRADO, causa);
            return respuesta(HttpStatus.SERVICE_UNAVAILABLE, FUENTE_DE_DATOS_NO_DISPONIBLE, ex, request);
        }

        if (causa instanceof JsonProcessingException) {
            log.error(LOG_ERROR_PARSEO_JSON, causa);
            return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, DATOS_DE_PRODUCTOS_INVALIDOS, ex, request);
        }

        log.error(LOG_ERROR_GENERAL_LECTURA, causa);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_AL_LEER_DATOS_DE_PRODUCTOS, ex, request);
    }

    // Manejo específico para parámetros inválidos generales
    @ExceptionHandler(ParametrosInvalidosException.class)
    public ResponseEntity<ErrorRespuesta> manejarParametrosInvalidos(ParametrosInvalidosException ex,
                                                                     WebRequest request) {
        log.warn("Parámetros inválidos: {}", ex.getMessage());
        return respuesta(HttpStatus.BAD_REQUEST, "Parámetros inválidos", ex, request);
    }

    // Manejo específico para cantidad de IDs inválida
    @ExceptionHandler(CantidadIdsInvalidaException.class)
    public ResponseEntity<ErrorRespuesta> manejarCantidadIdsInvalida(CantidadIdsInvalidaException ex,
                                                                     WebRequest request) {
        log.warn("Cantidad de IDs inválida - Recibidos: {}, Rango válido: {}-{}",
                ex.getCantidadRecibida(), ex.getMinimo(), ex.getMaximo());
        return respuesta(HttpStatus.BAD_REQUEST, "Cantidad de IDs inválida", ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejarExcepcionGeneral(Exception ex, WebRequest request) {
        log.error(LOG_ERROR_NO_CONTROLADO, ex);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_INTERNO_DEL_SERVIDOR, ex, request);
    }

    private ResponseEntity<ErrorRespuesta> respuesta(HttpStatus status, String titulo,
                                                     Exception ex, WebRequest request) {
        ErrorRespuesta error = new ErrorRespuesta(
                status.value(),
                titulo,
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, status);
    }
}
