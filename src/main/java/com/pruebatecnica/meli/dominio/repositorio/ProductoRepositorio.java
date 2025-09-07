package com.pruebatecnica.meli.dominio.repositorio;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositorio {
    List<Producto> listarProductos();
    Optional<Producto> obtenerProductoPorId(Long idProducto);
}
