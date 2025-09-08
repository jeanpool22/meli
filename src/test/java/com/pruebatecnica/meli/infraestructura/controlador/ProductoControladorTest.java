package com.pruebatecnica.meli.infraestructura.controlador;

import com.pruebatecnica.meli.aplicacion.casodeuso.ListarProductosCasoUso;
import com.pruebatecnica.meli.aplicacion.casodeuso.ObtenerProductoPorIdCasoUso;
import com.pruebatecnica.meli.aplicacion.casodeuso.ObtenerProductosPorIdsCasoUso;
import com.pruebatecnica.meli.compartido.excepciones.CantidadIdsInvalidaException;
import com.pruebatecnica.meli.compartido.excepciones.ParametrosInvalidosException;
import com.pruebatecnica.meli.compartido.utilidad.ProductoConstantes;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductoControladorTest {
    private ListarProductosCasoUso listarProductosCasoUso;
    private ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso;
    private ObtenerProductosPorIdsCasoUso obtenerProductosPorIdsCasoUso;
    private MockMvc mockMvc;

    private static final String RUTA_PRODUCTOS = "/productos";
    private static final String RUTA_PRODUCTO_ID = "/productos/{idProducto}";
    private static final String RUTA_COMPARADOR = "/productos/comparador";
    private static final String URI_COMPARADOR = "uri=/productos/comparador";

    private static final String PARAMETROS_INVALIDOS = "Parámetros inválidos";
    private static final String CANTIDAD_DE_IDS_INVALIDA = "Cantidad de IDs inválida";
    private static final String TODOS_LOS_IDS_DEBEN_SER_MAYORES_A_0 = "Todos los IDs deben ser mayores a 0";

    @BeforeEach
    void setUp() {
        listarProductosCasoUso = mock(ListarProductosCasoUso.class);
        obtenerProductoPorIdCasoUso = mock(ObtenerProductoPorIdCasoUso.class);
        obtenerProductosPorIdsCasoUso = mock(ObtenerProductosPorIdsCasoUso.class);

        ProductoControlador productoControlador = new ProductoControlador(
                listarProductosCasoUso,
                obtenerProductoPorIdCasoUso,
                obtenerProductosPorIdsCasoUso
        );

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

        mockMvc.perform(get(RUTA_PRODUCTOS)
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

        mockMvc.perform(get(RUTA_PRODUCTOS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void listarProductos_debeRetornarErrorSiCasoUsoFalla() throws Exception {
        ProductoCriteriosBusqueda criterios = new ProductoCriteriosBusqueda(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), 0, 10);
        when(listarProductosCasoUso.listarProductos(criterios)).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(get(RUTA_PRODUCTOS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void obtenerProductoPorId_debeRetornarProductoSiExiste() throws Exception {
        Long idProducto = 1L;
        Producto producto = new Producto(idProducto, "Producto 1", "url1", "desc1", 100.0, 4.5, "cat1", "marca1", List.of("spec1", "spec2"));
        when(obtenerProductoPorIdCasoUso.obtenerProductoPorId(idProducto)).thenReturn(producto);

        mockMvc.perform(get(RUTA_PRODUCTO_ID, idProducto).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(idProducto))
                .andExpect(jsonPath("$.nombre").value("Producto 1"))
                .andExpect(jsonPath("$.precio").value(100.0));
    }

    @Test
    void obtenerProductoPorId_debeRetornar404SiNoExiste() throws Exception {
        Long idProducto = 99L;
        when(obtenerProductoPorIdCasoUso.obtenerProductoPorId(idProducto)).thenThrow(new com.pruebatecnica.meli.compartido.excepciones.ProductoNoEncontradoException("No encontrado"));

        mockMvc.perform(get(RUTA_PRODUCTO_ID, idProducto).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.titulo").value("Producto no encontrado"));
    }

    // Nuevas pruebas para obtenerProductosPorIds

    @Test
    void obtenerProductosPorIds_debeRetornarListaDeProductos() throws Exception {
        List<Producto> productos = List.of(
                new Producto(1L, "P1", "u1", "d1", 10.0, 4.0, "c1", "m1", List.of()),
                new Producto(2L, "P2", "u2", "d2", 20.0, 4.5, "c2", "m2", List.of())
        );
        when(obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(anyList())).thenReturn(productos);

        mockMvc.perform(get(RUTA_COMPARADOR)
                        .param(ProductoConstantes.PARAMETRO_IDS, "1,2,3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("P1"))
                .andExpect(jsonPath("$[1].nombre").value("P2"));

        verify(obtenerProductosPorIdsCasoUso).obtenerProductosPorIds(argThat(l -> l.equals(List.of(1L, 2L, 3L))));
    }

    @Test
    void obtenerProductosPorIds_debeRetornar400ParametrosInvalidos() throws Exception {
        String mensaje = TODOS_LOS_IDS_DEBEN_SER_MAYORES_A_0;
        when(obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(anyList()))
                .thenThrow(new ParametrosInvalidosException(mensaje));

        mockMvc.perform(get(RUTA_COMPARADOR)
                        .param(ProductoConstantes.PARAMETRO_IDS, "0,2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titulo").value(PARAMETROS_INVALIDOS))
                .andExpect(jsonPath("$.mensaje").value(mensaje))
                .andExpect(jsonPath("$.ruta").value(URI_COMPARADOR));
    }

    @Test
    void obtenerProductosPorIds_debeRetornar400CantidadIdsInvalida() throws Exception {
        int min = ProductoConstantes.MINIMO_IDS_CONSULTA;
        int max = ProductoConstantes.MAXIMO_IDS_CONSULTA;
        int recibidos = 1;
        when(obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(anyList()))
                .thenThrow(new CantidadIdsInvalidaException(ProductoConstantes.ERROR_CANTIDAD_IDS, min, max, recibidos));

        mockMvc.perform(get(RUTA_COMPARADOR)
                        .param(ProductoConstantes.PARAMETRO_IDS, "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titulo").value(CANTIDAD_DE_IDS_INVALIDA))
                .andExpect(jsonPath("$.mensaje").value(String.format(ProductoConstantes.ERROR_CANTIDAD_IDS, min, max, recibidos)))
                .andExpect(jsonPath("$.ruta").value(URI_COMPARADOR));
    }
}
