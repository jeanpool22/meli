package com.pruebatecnica.meli.infraestructura.controlador;

import com.pruebatecnica.meli.aplicacion.casodeuso.ListarProductosCasoUso;
import com.pruebatecnica.meli.aplicacion.casodeuso.ObtenerProductoPorIdCasoUso;
import com.pruebatecnica.meli.aplicacion.casodeuso.ObtenerProductosPorIdsCasoUso;
import com.pruebatecnica.meli.compartido.utilidad.ErrorRespuesta;
import com.pruebatecnica.meli.compartido.utilidad.ProductoConstantes;
import com.pruebatecnica.meli.dominio.modelo.Producto;
import com.pruebatecnica.meli.dominio.modelo.ProductoCriteriosBusqueda;
import com.pruebatecnica.meli.dominio.modelo.ResultadoPaginado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "Producto", description = "Operaciones sobre productos")
@RestController
@RequestMapping("/productos")
public class ProductoControlador {
    private final ListarProductosCasoUso listarProductosCasoUso;
    private final ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso;
    private final ObtenerProductosPorIdsCasoUso obtenerProductosPorIdsCasoUso;

    public ProductoControlador(ListarProductosCasoUso listarProductosCasoUso,
                               ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso,
                               ObtenerProductosPorIdsCasoUso obtenerProductosPorIdsCasoUso) {
        this.listarProductosCasoUso = listarProductosCasoUso;
        this.obtenerProductoPorIdCasoUso = obtenerProductoPorIdCasoUso;
        this.obtenerProductosPorIdsCasoUso = obtenerProductosPorIdsCasoUso;
    }

    @Operation(summary = "Listar productos", description = "Devuelve la lista de productos con filtros y paginación")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Producto.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al leer datos de productos",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(
                            name = "Error de parseo",
                            value = """
                            {
                              "status": 500,
                              "titulo": "Datos de productos inválidos",
                              "mensaje": "Error al parsear el JSON de productos",
                              "ruta": "uri=/productos"
                            }
                            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Fuente de datos no disponible",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(
                            name = "Archivo faltante",
                            value = """
                            {
                              "status": 503,
                              "titulo": "Fuente de datos no disponible",
                              "mensaje": "No se encontró el archivo de productos: json/productos.json",
                              "ruta": "uri=/productos"
                            }
                            """)
                    )
            )
    })
    @GetMapping
    public ResultadoPaginado<Producto> listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precioMinimo,
            @RequestParam(required = false) Double precioMaximo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanioPagina) {
        ProductoCriteriosBusqueda criterios = new ProductoCriteriosBusqueda(
            Optional.ofNullable(nombre),
            Optional.ofNullable(categoria),
            Optional.ofNullable(precioMinimo),
            Optional.ofNullable(precioMaximo),
            pagina,
            tamanioPagina
        );
        return listarProductosCasoUso.listarProductos(criterios);
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto dado su identificador")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Producto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(
                            value = """
                            {
                              "status": 404,
                              "titulo": "Producto no encontrado",
                              "mensaje": "Producto no encontrado: 0",
                              "ruta": "uri=/v1/productos/0",
                              "timestamp": "2025-09-07T00:46:18.0907211-05:00"
                            }
                            """))
            )
    })
    @GetMapping("{idProducto}")
    public Producto obtenerProductoPorId(@PathVariable Long idProducto) {
        return obtenerProductoPorIdCasoUso.obtenerProductoPorId(idProducto);
    }

    @Operation(
            summary = "Obtener productos por múltiples IDs",
            description = "Devuelve una lista de productos dados sus identificadores (entre 2 y 5 IDs)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Producto.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorRespuesta.class),
                    examples = @ExampleObject(
                            value = """
                            {
                              "status": 400,
                              "titulo": "Parámetros inválidos",
                              "mensaje": "La cantidad de IDs debe estar entre 2 y 5",
                              "ruta": "uri=/productos/comparador",
                              "timestamp": "2025-09-07T12:00:00.000Z"
                            }
                            """))
            )
    })
    @GetMapping("/comparador")
    public List<Producto> obtenerProductosPorIds(@RequestParam(name = ProductoConstantes.PARAMETRO_IDS) List<Long> idProductos) {
        return obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(idProductos);
    }
}
