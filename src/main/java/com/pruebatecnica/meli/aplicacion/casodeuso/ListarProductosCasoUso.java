package com.pruebatecnica.meli.aplicacion.casodeuso;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.modelo.ProductoCriteriosBusqueda;
import com.pruebatecnica.meli.dominio.modelo.ResultadoPaginado;
import com.pruebatecnica.meli.dominio.repositorio.ProductoRepositorio;
import com.pruebatecnica.meli.dominio.especificacion.Especificacion;
import com.pruebatecnica.meli.dominio.especificacion.EspecificacionPorNombre;
import com.pruebatecnica.meli.dominio.especificacion.EspecificacionPorCategoria;
import com.pruebatecnica.meli.dominio.especificacion.EspecificacionPorPrecio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ListarProductosCasoUso {
    private final ProductoRepositorio productoRepositorio;

    public ListarProductosCasoUso(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    // Este método obtiene y retoran la lista de productos una vez aplicados los filtros y la paginación.
    public ResultadoPaginado<Producto> listarProductos(ProductoCriteriosBusqueda criterios) {
        List<Producto> productos = productoRepositorio.listarProductos();

        List<Especificacion<Producto>> especificaciones = construirEspecificaciones(criterios);
        productos = filtrarProductos(productos, especificaciones);

        return paginarProductos(productos, criterios);
    }

    // Este método construye una lista de especificaciones basadas en los criterios de búsqueda proporcionados.
    private List<Especificacion<Producto>> construirEspecificaciones(ProductoCriteriosBusqueda criterios) {
        List<Especificacion<Producto>> especificaciones = new ArrayList<>();

        criterios.nombre().ifPresent(nombre -> especificaciones.add(new EspecificacionPorNombre(nombre)));
        criterios.categoria().ifPresent(categoria -> especificaciones.add(new EspecificacionPorCategoria(categoria)));
        if (criterios.precioMin().isPresent() || criterios.precioMax().isPresent()) {
            especificaciones.add(new EspecificacionPorPrecio(
                criterios.precioMin().orElse(null),
                criterios.precioMax().orElse(null)));
        }

        return especificaciones;
    }

    // Este método filtra la lista de productos utilizando las especificaciones proporcionadas.
    private List<Producto> filtrarProductos(List<Producto> productos, List<Especificacion<Producto>> especificaciones) {
        for (Especificacion<Producto> especificacion : especificaciones) {
            productos = productos.stream().filter(especificacion::esSatisfechoPor).toList();
        }
        return productos;
    }

    // Este método pagina la lista de productos según los criterios de paginación proporcionados.
    private ResultadoPaginado<Producto> paginarProductos(List<Producto> productos, ProductoCriteriosBusqueda criterios) {
        int totalElementos = productos.size();
        int pagina = criterios.pagina();
        int tamanioPagina = criterios.tamanioPagina();
        int desdeIndice = Math.min(pagina * tamanioPagina, totalElementos);
        int hastaIndice = Math.min(desdeIndice + tamanioPagina, totalElementos);
        List<Producto> paginaProductos = productos.subList(desdeIndice, hastaIndice);
        int totalPaginas = (int) Math.ceil((double) totalElementos / tamanioPagina);

        return new ResultadoPaginado<>(paginaProductos, pagina, totalPaginas, totalElementos);
    }
}
