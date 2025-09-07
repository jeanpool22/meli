package com.pruebatecnica.meli.infraestructura.persistencia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import com.pruebatecnica.meli.compartido.excepciones.ErrorLecturaJsonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class ProductoRepositorioJson implements ProductoRepositorio {
    private static final Logger log = LoggerFactory.getLogger(ProductoRepositorioJson.class);
    private static final String PRODUCTOS_JSON = "json/productos.json";
    private static final String NO_SE_ENCONTRO_EL_ARCHIVO_DE_PRODUCTOS = "No se encontró el archivo de productos: ";
    private static final String ERROR_AL_PARSEAR_EL_JSON_DE_PRODUCTOS = "Error al parsear el JSON de productos";
    private static final String ERROR_AL_LEER_PRODUCTOS_DESDE_JSON = "Error al leer productos desde JSON";

    private final ObjectMapper objectMapper;

    public ProductoRepositorioJson(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Producto> listarProductos() {
        log.info("Intentando leer el archivo de productos: {}", PRODUCTOS_JSON);
        try (InputStream in = new ClassPathResource(PRODUCTOS_JSON).getInputStream()) {
            List<Producto> productos = objectMapper.readValue(in, new TypeReference<List<Producto>>() {});
            log.info("Archivo de productos leído correctamente. Se encontraron {} productos.", productos.size());
            return productos;
        } catch (FileNotFoundException e) {
            throw new ErrorLecturaJsonException(NO_SE_ENCONTRO_EL_ARCHIVO_DE_PRODUCTOS + PRODUCTOS_JSON, e);
        } catch (JsonProcessingException e) {
            throw new ErrorLecturaJsonException(ERROR_AL_PARSEAR_EL_JSON_DE_PRODUCTOS, e);
        } catch (IOException e) {
            throw new ErrorLecturaJsonException(ERROR_AL_LEER_PRODUCTOS_DESDE_JSON, e);
        }
    }
}