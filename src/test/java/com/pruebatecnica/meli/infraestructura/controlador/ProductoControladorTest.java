package com.pruebatecnica.meli.infraestructura.controlador;

import com.pruebatecnica.meli.aplicacion.casodeuso.ListarProductosCasoUso;
import com.pruebatecnica.meli.aplicacion.casodeuso.ObtenerProductoPorIdCasoUso;
import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.modelo.ProductoCriteriosBusqueda;
import com.pruebatecnica.meli.dominio.modelo.ResultadoPaginado;
import com.pruebatecnica.meli.infraestructura.configuracion.ManejadorGlobalExcepciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductoControladorTest {
    private ListarProductosCasoUso listarProductosCasoUso;
    private ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        listarProductosCasoUso = mock(ListarProductosCasoUso.class);
        obtenerProductoPorIdCasoUso = mock(ObtenerProductoPorIdCasoUso.class);
        ProductoControlador productoControlador = new ProductoControlador(listarProductosCasoUso, obtenerProductoPorIdCasoUso);
        mockMvc = MockMvcBuilders.standaloneSetup(productoControlador)
                .setControllerAdvice(new ManejadorGlobalExcepciones())
                .build();
    }

    @Test
    void listarProductos_debeRetornarListaDeProductos() throws Exception {
        Producto producto1 = new Producto(1L, "Producto 1", "url1", "desc1", 100.0, 4.5, "cat1", "marca1", List.of("spec1", "spec2"));
        Producto producto2 = new Producto(2L, "Producto 2", "url2", "desc2", 200.0, 4.7, "cat2", "marca2", List.of("specA"));
        ProductoCriteriosBusqueda criterios = new ProductoCriteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(List.of(producto1, producto2), 0, 1, 2);
        when(listarProductosCasoUso.listarProductos(criterios)).thenReturn(resultado);

        mockMvc.perform(get("/productos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elementos[0].nombre").value("Producto 1"))
                .andExpect(jsonPath("$.elementos[1].nombre").value("Producto 2"));
    }

    @Test
    void listarProductos_debeRetornarListaVacia() throws Exception {
        ProductoCriteriosBusqueda criterios = new ProductoCriteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        ResultadoPaginado<Producto> resultado = new ResultadoPaginado<>(List.of(), 0, 0, 0);
        when(listarProductosCasoUso.listarProductos(criterios)).thenReturn(resultado);

        mockMvc.perform(get("/productos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void listarProductos_debeRetornarErrorSiCasoUsoFalla() throws Exception {
        ProductoCriteriosBusqueda criterios = new ProductoCriteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        when(listarProductosCasoUso.listarProductos(criterios)).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(get("/productos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void obtenerProductoPorId_debeRetornarProductoSiExiste() throws Exception {
        Long idProducto = 1L;
        Producto producto = new Producto(idProducto, "Producto 1", "url1", "desc1", 100.0, 4.5, "cat1", "marca1", List.of("spec1", "spec2"));
        when(obtenerProductoPorIdCasoUso.obtenerProductoPorId(idProducto)).thenReturn(producto);

        mockMvc.perform(get("/productos/{idProducto}", idProducto).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(idProducto))
                .andExpect(jsonPath("$.nombre").value("Producto 1"))
                .andExpect(jsonPath("$.precio").value(100.0));
    }

    @Test
    void obtenerProductoPorId_debeRetornar404SiNoExiste() throws Exception {
        Long idProducto = 99L;
        when(obtenerProductoPorIdCasoUso.obtenerProductoPorId(idProducto)).thenThrow(new com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException("No encontrado"));

        mockMvc.perform(get("/productos/{idProducto}", idProducto).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.titulo").value("Producto no encontrado"));
    }
}
