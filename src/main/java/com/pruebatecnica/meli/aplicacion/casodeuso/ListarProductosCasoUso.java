package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListarProductosCasoUso {
    private final ProductoRepositorio productoRepositorio;

    public ListarProductosCasoUso(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> listarProductos() {
        return productoRepositorio.listarProductos();
    }
}
