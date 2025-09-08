# Cómo ejecutar y probar la API (Swagger UI)

Este proyecto es una API REST de Spring Boot. El prefijo global es `/v1`.

## Requisitos
- JDK 21 instalado y en el PATH.
- Gradle Wrapper incluido (no necesitas instalar Gradle).

## Ejecutar la aplicación
Opciones equivalentes; usa la que prefieras.

1) Gradle Wrapper (desarrollo)
- Windows:
```bat
.\gradlew.bat bootRun
```
- Linux/macOS:
```bash
./gradlew bootRun
```
La API quedará disponible en: http://localhost:8080/v1

## Probar desde Swagger UI
1. Con la app corriendo, abre:
   - Swagger UI: http://localhost:8080/v1/swagger-ui/index.html
   - OpenAPI (JSON): http://localhost:8080/v1/v3/api-docs
2. En Swagger UI, despliega "Producto" y pulsa "Try it out" en los endpoints.

## Health checks (opcional)
- http://localhost:8080/v1/actuator/health
- http://localhost:8080/v1/actuator/info

## Notas
- El prefijo `/v1` es obligatorio (configurado en spring.mvc.servlet.path=/v1).
- La fuente de datos es `src/main/resources/json/productos.json` (no requiere base de datos).
