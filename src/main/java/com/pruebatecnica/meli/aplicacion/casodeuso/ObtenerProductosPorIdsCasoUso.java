package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.compartido.excepciones.CantidadIdsInvalidaException;
import com.pruebatecnica.meli.compartido.excepciones.ParametrosInvalidosException;
import com.pruebatecnica.meli.compartido.utilidad.ProductoConstantes;
import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObtenerProductosPorIdsCasoUso {

    private static final String LA_LISTA_DE_IDS_NO_PUEDE_ESTAR_VACIA = "La lista de IDs no puede estar vacía";
    private static final String LA_LISTA_DE_IDS_NO_PUEDE_SER_NULA = "La lista de IDs no puede ser nula";
    private static final String IDS_DEBEN_SER_MAYORES_A_CERO = "Todos los IDs deben ser mayores a 0";

    private final ProductoRepositorio productoRepositorio;

    public ObtenerProductosPorIdsCasoUso(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> obtenerProductosPorIds(List<Long> ids) {
        validarParametros(ids);
        validarValoresIds(ids);
        validarCantidadIds(ids);

        return productoRepositorio.obtenerProductosPorIds(ids);
    }

    private void validarParametros(List<Long> ids) {
        if (ids == null) {
            throw new ParametrosInvalidosException(LA_LISTA_DE_IDS_NO_PUEDE_SER_NULA);
        }

        if (ids.isEmpty()) {
            throw new ParametrosInvalidosException(LA_LISTA_DE_IDS_NO_PUEDE_ESTAR_VACIA);
        }
    }

    private void validarValoresIds(List<Long> ids) {
        boolean hayInvalido = ids.stream().anyMatch(id -> id == null || id <= 0);
        if (hayInvalido) {
            throw new ParametrosInvalidosException(IDS_DEBEN_SER_MAYORES_A_CERO);
        }
    }

    private void validarCantidadIds(List<Long> ids) {
        int cantidad = ids.size();
        if (cantidad < ProductoConstantes.MINIMO_IDS_CONSULTA || cantidad > ProductoConstantes.MAXIMO_IDS_CONSULTA) {
            throw new CantidadIdsInvalidaException(
                    ProductoConstantes.ERROR_CANTIDAD_IDS,
                    ProductoConstantes.MINIMO_IDS_CONSULTA,
                    ProductoConstantes.MAXIMO_IDS_CONSULTA,
                    cantidad
            );
        }
    }
}
