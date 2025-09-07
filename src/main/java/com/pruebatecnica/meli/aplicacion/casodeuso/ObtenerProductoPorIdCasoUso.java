package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ObtenerProductoPorIdCasoUso {
    public static final String NO_FUE_POSIBLE_ENCONTRAR_EL_PRODUCTO = "No fue posible encontrar el producto %s";

    private final ProductoRepositorio productoRepositorio;

    public ObtenerProductoPorIdCasoUso(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public Producto obtenerProductoPorId(Long idProducto) {
        Optional<Producto> producto = productoRepositorio.obtenerProductoPorId(idProducto);
        return producto.orElseThrow(() -> new ProductoNoEncontradoException(String.format(NO_FUE_POSIBLE_ENCONTRAR_EL_PRODUCTO, idProducto)));
    }
}
