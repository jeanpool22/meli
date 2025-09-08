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
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Producto", description = "Operaciones sobre productos: listado con filtros y paginación, obtención por ID y comparación por múltiples IDs.")
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

    @Operation(
            summary = "Listar productos",
            description =
            """
            Devuelve una lista paginada de productos.
        
            Filtros opcionales:
            - nombre: coincidencia parcial, sin distinción de mayúsculas/minúsculas.
            - categoria: coincidencia exacta, sin distinción de mayúsculas/minúsculas.
            - precioMinimo / precioMaximo: rangos inclusivos. Si ambos se especifican, se aplican de forma conjunta.
        
            Paginación:
            - pagina: índice base 0.
            - tamañoPagina: tamaño de página (> 0).
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada de productos con metadatos",
                    content = @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = ResultadoPaginado.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al leer datos de productos (JSON inválido o lectura fallida)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(
                                            name = "JSON inválido",
                                            value = """
                                            {
                                              "status": 500,
                                              "titulo": "Datos de productos inválidos",
                                              "mensaje": "Error al parsear el JSON de productos",
                                              "ruta": "uri=/productos"
                                            }
                                            """
                                    ),
                                    @ExampleObject(
                                            name = "Lectura fallida",
                                            value = """
                                            {
                                              "status": 500,
                                              "titulo": "Error al leer datos de productos",
                                              "mensaje": "Error al leer productos desde JSON",
                                              "ruta": "uri=/productos"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Fuente de datos no disponible (archivo de productos no encontrado)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Archivo faltante",
                                            value = """
                                            {
                                              "status": 503,
                                              "titulo": "Fuente de datos no disponible",
                                              "mensaje": "No se encontró el archivo de productos: json/productos.json",
                                              "ruta": "uri=/productos"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    @GetMapping
    public ResultadoPaginado<Producto> listarProductos(
            @Parameter(description = "Filtro por nombre (contains, case-insensitive)", example = "Smartphone")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtro por categoría (igualdad exacta, case-insensitive)", example = "Electrónica")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Precio mínimo (>= 0)", schema = @Schema(minimum = "0"), example = "100")
            @RequestParam(required = false) Double precioMinimo,
            @Parameter(description = "Precio máximo (>= 0)", schema = @Schema(minimum = "0"), example = "1000")
            @RequestParam(required = false) Double precioMaximo,
            @Parameter(description = "Número de página (base 0)", schema = @Schema(minimum = "0", defaultValue = "0"), example = "0")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamaño de página (> 0)", schema = @Schema(minimum = "1", defaultValue = "10"), example = "10")
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

    @Operation(
            summary = "Obtener producto por ID",
            description = "Devuelve un producto dado su identificador. Si el ID no existe, retorna 404."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado (el ID no existe)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(name = "No existe",
                                            value = """
                                            {
                                              "status": 404,
                                              "titulo": "Producto no encontrado",
                                              "mensaje": "No fue posible encontrar el producto 0",
                                              "ruta": "uri=/productos/0",
                                              "timestamp": "2025-09-07T00:46:18.0907211-05:00"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al leer datos de productos (JSON inválido o lectura fallida)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(name = "JSON inválido",
                                            value = """
                                            {
                                              "status": 500,
                                              "titulo": "Datos de productos inválidos",
                                              "mensaje": "Error al parsear el JSON de productos",
                                              "ruta": "uri=/productos/1"
                                            }
                                            """
                                    ),
                                    @ExampleObject(name = "Lectura fallida",
                                            value = """
                                            {
                                              "status": 500,
                                              "titulo": "Error al leer datos de productos",
                                              "mensaje": "Error al leer productos desde JSON",
                                              "ruta": "uri=/productos/1"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Fuente de datos no disponible (archivo de productos no encontrado)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(name = "Archivo faltante",
                                            value = """
                                            {
                                              "status": 503,
                                              "titulo": "Fuente de datos no disponible",
                                              "mensaje": "No se encontró el archivo de productos: json/productos.json",
                                              "ruta": "uri=/productos/1"
                                            }
                                            """
                                    )
                            }
                    )
            )
    })
    @GetMapping("{idProducto}")
    public Producto obtenerProductoPorId(
            @Parameter(description = "Identificador del producto (>= 1)", schema = @Schema(minimum = "1"), example = "1")
            @PathVariable Long idProducto) {
        return obtenerProductoPorIdCasoUso.obtenerProductoPorId(idProducto);
    }

    @Operation(
            summary = "Obtener productos por múltiples IDs",
            description = """
                    Devuelve una lista de productos dados sus identificadores.
                    El número de IDs enviados por parámetro debe estar entre 2 y 5. Todos los IDs deben ser enteros positivos (> 0).
                    """
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
                    description = "Solicitud inválida: parámetros nulos/vacíos, IDs no positivos o cantidad fuera de rango",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(name = "Parámetros inválidos (lista nula/vacía o IDs <= 0)",
                                            value = """
                                            {
                                              "status": 400,
                                              "titulo": "Parámetros inválidos",
                                              "mensaje": "Todos los IDs deben ser mayores a 0",
                                              "ruta": "uri=/productos/comparador?ids=1,0,3"
                                            }
                                            """
                                    ),
                                    @ExampleObject(name = "Cantidad de IDs inválida",
                                            value = """
                                            {
                                              "status": 400,
                                              "titulo": "Cantidad de IDs inválida",
                                              "mensaje": "La cantidad de IDs debe estar entre 2 y 5, pero se recibieron 1",
                                              "ruta": "uri=/productos/comparador?ids=1"
                                            }
                                            """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno al leer datos de productos (JSON inválido o lectura fallida)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(name = "JSON inválido",
                                            value = """
                                            {
                                              "status": 500,
                                              "titulo": "Datos de productos inválidos",
                                              "mensaje": "Error al parsear el JSON de productos",
                                              "ruta": "uri=/productos/comparador?ids=1,2"
                                            }
                                            """),
                                    @ExampleObject(name = "Lectura fallida",
                                            value = """
                                            {
                                              "status": 500,
                                              "titulo": "Error al leer datos de productos",
                                              "mensaje": "Error al leer productos desde JSON",
                                              "ruta": "uri=/productos/comparador?ids=1,2"
                                            }
                                            """)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Fuente de datos no disponible (archivo de productos no encontrado)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = {
                                    @ExampleObject(name = "Archivo faltante",
                                            value = """
                                            {
                                              "status": 503,
                                              "titulo": "Fuente de datos no disponible",
                                              "mensaje": "No se encontró el archivo de productos: json/productos.json",
                                              "ruta": "uri=/productos/comparador?ids=1,2"
                                            }
                                            """)
                            }
                    )
            )
    })
    @GetMapping("/comparador")
    public List<Producto> obtenerProductosPorIds(
            @Parameter(description = "Lista de IDs. Tamaño permitido: " +
                    ProductoConstantes.MINIMO_IDS_CONSULTA + "-" + ProductoConstantes.MAXIMO_IDS_CONSULTA + " elementos. Todos los IDs deben ser > 0.",
                    example = "1,2,3")
            @RequestParam(name = ProductoConstantes.PARAMETRO_IDS) List<Long> idProductos) {
        return obtenerProductosPorIdsCasoUso.obtenerProductosPorIds(idProductos);
    }
}
