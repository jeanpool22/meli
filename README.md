# API de Productos (MELI)

API REST para consulta y comparación de productos con filtros y paginación. Lee un catálogo JSON embebido y expone endpoints de lectura con documentación OpenAPI y health checks.

El proyecto se encuentra alojado en repositorio: https://github.com/jeanpool22/meli

## Diseño y arquitectura
Se hace uso de la arquitecura de puertos y adaptadores (Hexagonal Architecture):
- ¿Por qué puertos y adaptadores?
  - Enfoca el dominio y los casos de uso, desacoplando el núcleo de frameworks e infraestructura.
  - Permite intercambiar adaptadores fácilmente: hoy JSON en classpath, mañana base de datos u otro proveedor, sin tocar la lógica de negocio.
  - Facilita pruebas unitarias e integración al poder sustituir puertos con mocks.
  - Mejora mantenibilidad y evolución: agregar nuevos drivers (REST, CLI, schedulers) y cambiar infraestructura con bajo impacto.
  - Reduce acoplamiento y favorece separación de responsabilidades.
- Capas limpias y desacopladas:
  - Dominio: modelos, repositorios, especificaciones y servicios de negocio (`com.pruebatecnica.meli.dominio.*`).
  - Aplicación: casos de uso orquestan el flujo (`ListarProductosCasoUso`, `ObtenerProductoPorIdCasoUso`, `ObtenerProductosPorIdsCasoUso`).
  - Infraestructura: controladores REST, persistencia (fuente JSON), configuración y salud.
  - Compartido: utilidades y excepciones comunes (incluye `ErrorRespuesta` para errores estandarizados).
- Controlador principal: `ProductoControlador` bajo el prefijo global `/v1` (configurado con `spring.mvc.servlet.path=/v1`).
- Persistencia: `ProductoRepositorioJson` usa Jackson para leer `src/main/resources/json/productos.json` desde el classpath.
- Manejo de errores: `ManejadorGlobalExcepciones` mapea excepciones a HTTP 400/404/500/503 con estructura uniforme (`status`, `titulo`, `mensaje`, `ruta`).
- Observabilidad: Spring Boot Actuator + `ProductoHealthIndicator`.

## Endpoints
Se decidio crear tres servicios REST con la finalidad de ofrecer diferentes opciones de obtener los productos.

Prefijo común: `/v1`. Recurso base: `/productos`.

- GET `/v1/productos`
  - Filtros opcionales: `nombre` (contiene, case-insensitive), `categoria` (igualdad, case-insensitive), `precioMinimo`, `precioMaximo`.
  - Paginación: `pagina` (base 0), `tamañoPagina` (> 0).
  - Respuesta: estructura paginada con lista de `Producto` y metadatos.

- GET `/v1/productos/{idProducto}`
  - Path variable: `idProducto` (>= 1).

- GET `/v1/productos/comparador?ids=1,2,3`
  - Query param: `ids` (lista de enteros positivos).
  - Reglas: cantidad permitida entre 2 y 5; todos los IDs deben ser > 0.

### Salud y documentación
- Health: `/v1/actuator/health` (detalles habilitados) y `/v1/actuator/info`.
- OpenAPI: UI en `/v1/swagger-ui/index.html` y spec en `/v1/v3/api-docs`.

## Stack tecnológico
- Java 21
- Spring Boot 3.5.5 (Web, Actuator)
- springdoc-openapi 2.8.12 (Swagger UI)
- Jackson (mapeo JSON)
- Lombok (anotaciones, compileOnly)
- JUnit 5 (tests)
- PIT Mutation Testing (plugin Gradle: info.solidsoft.pitest)
- Gradle Wrapper

## Logs y pruebas
- Logs: uso de SLF4J/Spring Boot logging en capas de infraestructura (controlador, repositorio) con niveles INFO/WARN/ERROR para trazabilidad y diagnóstico.
- Pruebas unitarias: JUnit 5 cubriendo dominio, casos de uso y manejo de excepciones.
- Pruebas de integración: pruebas sobre controladores y configuración (incluye `ManejadorGlobalExcepciones`), ejecutables con `./gradlew test`.
- Pruebas de mutación: PIT configurado en Gradle; ejecutar con `./gradlew pitest` y revisar reportes en `build/reports/pitest`.

## Datos
- Fuente: `src/main/resources/json/productos.json` (empaquetado en el jar). No se requiere base de datos.

## Configuración relevante
`src/main/resources/application.properties`:
- `spring.application.name=productos`
- `spring.mvc.servlet.path=/v1`
- Actuator: `management.endpoints.web.exposure.include=health,info`, `management.endpoint.health.show-details=always`

## Estructura (resumen)
- `src/main/java/com/pruebatecnica/meli`
  - `dominio/` (modelo, repositorio, especificacion, servicio)
  - `aplicacion/` (casodeuso)
  - `infraestructura/` (controlador, persistencia, configuracion, salud)
  - `compartido/` (excepciones, utilidad)
- `src/main/resources/` (application.properties, json/productos.json)

## Notas
- Los errores siguen un contrato consistente (`ErrorRespuesta`).
- La API es de solo lectura; ideal para catálogos pequeños/medianos empaquetados en el artefacto.

## GenAI
El proyecto fue desarrollado usando la asistencia de Copilot mediante agentes, usando los modelos GPT-4.1, GPT-5 y Claude Sonnet 4.
Al igual que se utilo directamente ChatGPT 5 cuando el agente no estaba devolviendo respuestas satisfactorias.

Las herramientas de GenAI fueron de gran apoyo en la generación de código, corrección de errores, generación de pruebas unitarias e integración, mejora de mensajes de error y mejora de documentación.
Son una gran ayuda para acelerar el desarrollo, pero aún así es necesaria la intervención humana para verificar y ajustar los resultados.