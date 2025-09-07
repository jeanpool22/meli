package com.pruebatecnica.meli.infraestructura.controlador;

import com.pruebatecnica.meli.aplicacion.casodeuso.ListarProductosCasoUso;
import com.pruebatecnica.meli.aplicacion.casodeuso.ObtenerProductoPorIdCasoUso;
import com.pruebatecnica.meli.compartido.utilidad.ErrorRespuesta;
import com.pruebatecnica.meli.dominio.modelo.Producto;
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
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "Producto", description = "Operaciones sobre productos")
@RestController
@RequestMapping("/productos")
public class ProductoControlador {
    private final ListarProductosCasoUso listarProductosCasoUso;
    private final ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso;

    public ProductoControlador(ListarProductosCasoUso listarProductosCasoUso, ObtenerProductoPorIdCasoUso obtenerProductoPorIdCasoUso) {
        this.listarProductosCasoUso = listarProductosCasoUso;
        this.obtenerProductoPorIdCasoUso = obtenerProductoPorIdCasoUso;
    }

    @Operation(summary = "Listar productos", description = "Devuelve la lista de productos")
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
    public List<Producto> listarProductos() {
        return listarProductosCasoUso.listarProductos();
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
}
