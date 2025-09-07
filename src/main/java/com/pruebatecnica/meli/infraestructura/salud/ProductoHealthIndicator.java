package com.pruebatecnica.meli.infraestructura.salud;

import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ProductoHealthIndicator implements HealthIndicator {

    private static final String ERROR = "error";
    private static final String SIMULADO = "simulado";

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
                        .withDetail(ERROR, "No se encontraron productos en el repositorio")
                        .withDetail("totalProductos", 0)
                        .withDetail(SIMULADO, false)
                        .build();
            }

            return Health.up()
                    .withDetail("status", "Servicio de productos operativo")
                    .withDetail("totalProductos", productos.size())
                    .withDetail("repositorioTipo", productoRepositorio.getClass().getSimpleName())
                    .withDetail(SIMULADO, false)
                    .build();

        } catch (Exception e) {
            return Health.down()
                    .withDetail(ERROR, "Error al acceder al repositorio de productos")
                    .withDetail("mensaje", e.getMessage())
                    .withDetail(SIMULADO, false)
                    .withException(e)
                    .build();
        }
    }
}
