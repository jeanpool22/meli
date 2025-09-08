## Prompts usados en este proyecto
- El presente proyecto tiene la funcionalidad de suministrar detalles de productos, actualmente está dividido en una arquitectura de puertos y adaptadores. Haremos uso de archivos JSON para la recuperación de la información. Para el primer servicio vamos a devolver una lista de productos, cada producto inicialmente tiene los siguientes campos: nombre del producto, URL de la imagen, descripción, precio, calificación y especificaciones. Genera el código necesario para esta funcionalidad repetando la arquitectura propuesta (puertos y adaptadores); también quiero que generes excepciones personalizadas y las uses en el proyecto.

- El endpoint funciona correctamente. Quiero que agregemos documentación mediante OpenAPI, es decir, quiero abrir en el navegador swagger y poder ejecutar mis servicios desde allí.

- Quiero que agregemos inyección de dependencias al proyecto.

- Quiero agregar robustes a las respuestas de mi servicio, es decir, quiero agregar los escenarios cuando ocurre un 404 o un 500, no quiero devolverle al usuario respuestas génericas como esta: { "timestamp": "2025-09-07T01:57:23.705+00:00", "status": 500, "error": "Internal Server Error", "path": "/v1/productos" }

- Agreguemos loggers en los puntos donde genere valor cuando se ejecuta el servicio.

- Es hora de agregar test al servicio, para eso vamos a agregar pruebas unitarias y pruebas de integración. Empecemos únicamente por las pruebas unitarias, genera el código de las pruebas unitarias para el servicio de listar los prodcutos.

- Genera el servicio GET /v1/productos/{idProducto}. Este servicio debe devolver un único producto dado su identificador. Haz uso de la excepcion ProductoNoEncontrado si el producto no se encuentra. Respeta el uso del arquitectura de puertos y adaptadores que implementa el proyecto.

- Genera los test unitarios para el nuevo caso de uso obtener producto por id.

- Genera las pruebas unitarias para el controlador de obtener producto por id en la clase ProductoControladorTest.

- Genera ahora los test de integración para el controlador de obtener producto por id en la clase ProductoControladorIntegracionTest.

- Quiero agregar dos funcionalidades al servicio de listar productos, la primera funcionalidad es listar los productos con paginación y la segunda funcionalidad es listar los productos mediante un filtro (su precio, marca, categoria, etc). Modifica el código para cumplir con estas funcionalidades.

- Dado un conjunto de 2 a 5 IDs de productos válidos, construir un servicio que retorne los productos solicitadores mediante el query param. Genera código limpio, facil de mantener y escalar, usa constantes, usa principios de código limpio. No quiere que ejecutes ningun comando en la consola del proyecto.

- Agrega las pruebas unitarias para las excepciones CantidadIdsInvalida y ParametrosInvalidos en la clase de test del manejador. No ejecutes ningun comando en la consola.

- Quiero agregar el servicio GET /actuator/health para validar si alguno de mis servicios está operando correctamente, cómo debo hacer esta implementación al proyecto?

## Nota
Adicionalmente se usaron prompts para hacer corrección de errores, agregar test, mejorar código, mejorar mensajes de error, mejorar documentación.