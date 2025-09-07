package com.pruebatecnica.meli.compartido.utilidad;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Schema(name = "ErrorRespuesta", description = "Estructura estándar de error")
public class ErrorRespuesta {

    @Schema(description = "Código HTTP del error", example = "500")
    private final int status;

    @Schema(description = "Título corto del error", example = "Fuente de datos no disponible")
    private final String titulo;

    @Schema(description = "Mensaje legible para el cliente",
            example = "No se encontró el archivo de productos: json/productos.json")
    private final String mensaje;

    @Schema(description = "Ruta solicitada", example = "uri=/productos")
    private final String ruta;

    @Schema(description = "Marca de tiempo en ISO-8601 (RFC 3339)",
            type = "string", format = "date-time", example = "2025-09-06T21:05:30Z")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final OffsetDateTime timestamp;

    public ErrorRespuesta(int status, String titulo, String mensaje, String ruta) {
        this.status = status;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.ruta = ruta;
        this.timestamp = OffsetDateTime.now();
    }

}
