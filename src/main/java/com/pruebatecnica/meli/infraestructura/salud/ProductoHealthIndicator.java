package com.pruebatecnica.meli.infraestructura.salud;

import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ProductoHealthIndicator implements HealthIndicator {

    private static final String ERROR = "error";
    private static final String SIMULADO = "simulado";
    private static final String TOTAL_PRODUCTOS = "totalProductos";
    private static final String NO_SE_ENCONTRARON_PRODUCTOS_EN_EL_REPOSITORIO = "No se encontraron productos en el repositorio";
    private static final String STATUS = "status";
    private static final String SERVICIO_DE_PRODUCTOS_OPERATIVO = "Servicio de productos operativo";
    private static final String REPOSITORIO_TIPO = "repositorioTipo";
    private static final String ERROR_AL_ACCEDER_AL_REPOSITORIO_DE_PRODUCTOS = "Error al acceder al repositorio de productos";
    private static final String MENSAJE = "mensaje";

    private final ProductoRepositorio productoRepositorio;

    public ProductoHealthIndicator(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    @Override
    public Health health() {
        try {
            var productos = productoRepositorio.listarProductos();

            if (productos.isEmpty()) {
                return Health.down()
                        .withDetail(ERROR, NO_SE_ENCONTRARON_PRODUCTOS_EN_EL_REPOSITORIO)
                        .withDetail(TOTAL_PRODUCTOS, 0)
                        .withDetail(SIMULADO, false)
                        .build();
            }

            return Health.up()
                    .withDetail(STATUS, SERVICIO_DE_PRODUCTOS_OPERATIVO)
                    .withDetail(TOTAL_PRODUCTOS, productos.size())
                    .withDetail(REPOSITORIO_TIPO, productoRepositorio.getClass().getSimpleName())
                    .withDetail(SIMULADO, false)
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail(ERROR, ERROR_AL_ACCEDER_AL_REPOSITORIO_DE_PRODUCTOS)
                    .withDetail(MENSAJE, e.getMessage())
                    .withDetail(SIMULADO, false)
                    .withException(e)
                    .build();
        }
    }
}
