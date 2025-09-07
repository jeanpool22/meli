package com.pruebatecnica.meli.dominio.especificacion;

import com.pruebatecnica.meli.dominio.modelo.Producto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EspecificacionesTest {

    private static final Long PRODUCTO_ID_1 = 1L;
    private static final Long PRODUCTO_ID_2 = 2L;
    private static final Long PRODUCTO_ID_3 = 3L;
    private static final String NOMBRE_CELULAR = "Celular Samsung";
    private static final String NOMBRE_CELULAR_PRO = "Celular Pro iPhone";
    private static final String NOMBRE_TABLET = "Tablet Android";
    private static final String IMAGEN_URL = "http://ejemplo.com/imagen.jpg";
    private static final String DESCRIPCION = "Descripción del producto";
    private static final double PRECIO_500_00 = 500.00;
    private static final double PRECIO_1200_00 = 1200.00;
    private static final double PRECIO_1800_00 = 1800.00;
    private static final double CALIFICACION_4_5 = 4.5;
    private static final String CATEGORIA_ELECTRONICA = "Electrónica";
    private static final String CATEGORIA_ELECTRONICA_MAYUS = "ELECTRÓNICA";
    private static final String CATEGORIA_INFORMATICA = "Informática";
    private static final String MARCA_SAMSUNG = "Samsung";
    private static final List<String> ESPECIFICACIONES_DEFAULT = List.of("spec1", "spec2");

    private Producto crearProducto(Long id, String nombre, String categoria, double precio) {
        return new Producto(
                id, nombre, IMAGEN_URL, DESCRIPCION, precio,
                CALIFICACION_4_5, categoria, MARCA_SAMSUNG, ESPECIFICACIONES_DEFAULT
        );
    }

    @Test
    @DisplayName("Especificación por nombre debe encontrar productos que contengan el texto")
    void especificacionPorNombre_debeEncontrarProductosQueContenganElTexto() {
        Producto celular = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);
        Producto celularPro = crearProducto(PRODUCTO_ID_2, NOMBRE_CELULAR_PRO, CATEGORIA_ELECTRONICA, PRECIO_1200_00);
        Producto tablet = crearProducto(PRODUCTO_ID_3, NOMBRE_TABLET, CATEGORIA_ELECTRONICA, PRECIO_1800_00);

        EspecificacionPorNombre especificacion = new EspecificacionPorNombre("Celular");

        assertAll("Verificar búsqueda por nombre",
            () -> assertTrue(especificacion.esSatisfechoPor(celular)),
            () -> assertTrue(especificacion.esSatisfechoPor(celularPro)),
            () -> assertFalse(especificacion.esSatisfechoPor(tablet))
        );
    }

    @Test
    @DisplayName("Especificación por nombre debe ser case insensitive")
    void especificacionPorNombre_debeSerCaseInsensitive() {
        Producto celular = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);

        EspecificacionPorNombre especificacionMinuscula = new EspecificacionPorNombre("celular");
        EspecificacionPorNombre especificacionMayuscula = new EspecificacionPorNombre("CELULAR");
        EspecificacionPorNombre especificacionMixta = new EspecificacionPorNombre("CeLuLaR");

        assertAll("Verificar case insensitive",
            () -> assertTrue(especificacionMinuscula.esSatisfechoPor(celular)),
            () -> assertTrue(especificacionMayuscula.esSatisfechoPor(celular)),
            () -> assertTrue(especificacionMixta.esSatisfechoPor(celular))
        );
    }

    @Test
    @DisplayName("Especificación por categoría debe encontrar productos de la categoría exacta")
    void especificacionPorCategoria_debeEncontrarProductosDeLaCategoriaExacta() {
        Producto productoElectronica = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);
        Producto productoInformatica = crearProducto(PRODUCTO_ID_2, NOMBRE_TABLET, CATEGORIA_INFORMATICA, PRECIO_1200_00);

        EspecificacionPorCategoria especificacion = new EspecificacionPorCategoria(CATEGORIA_ELECTRONICA);

        assertAll("Verificar filtrado por categoría",
            () -> assertTrue(especificacion.esSatisfechoPor(productoElectronica)),
            () -> assertFalse(especificacion.esSatisfechoPor(productoInformatica))
        );
    }

    @Test
    @DisplayName("Especificación por categoría debe ser case insensitive")
    void especificacionPorCategoria_debeSerCaseInsensitive() {
        Producto producto = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);

        EspecificacionPorCategoria especificacionMayus = new EspecificacionPorCategoria(CATEGORIA_ELECTRONICA_MAYUS);
        EspecificacionPorCategoria especificacionMinus = new EspecificacionPorCategoria(CATEGORIA_ELECTRONICA.toLowerCase());

        assertAll("Verificar categoría case insensitive",
            () -> assertTrue(especificacionMayus.esSatisfechoPor(producto)),
            () -> assertTrue(especificacionMinus.esSatisfechoPor(producto))
        );
    }

    @Test
    @DisplayName("Especificación por precio debe filtrar por rango mínimo y máximo")
    void especificacionPorPrecio_debeFiltrarPorRangoMinimoYMaximo() {
        Producto productoBarato = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);
        Producto productoMedio = crearProducto(PRODUCTO_ID_2, NOMBRE_CELULAR_PRO, CATEGORIA_ELECTRONICA, PRECIO_1200_00);
        Producto productoCaro = crearProducto(PRODUCTO_ID_3, NOMBRE_TABLET, CATEGORIA_ELECTRONICA, PRECIO_1800_00);

        EspecificacionPorPrecio especificacion = new EspecificacionPorPrecio(600.0, 1500.0);

        assertAll("Verificar filtrado por rango de precio",
            () -> assertFalse(especificacion.esSatisfechoPor(productoBarato), "Producto barato debería quedar excluido"),
            () -> assertTrue(especificacion.esSatisfechoPor(productoMedio), "Producto medio debería estar incluido"),
            () -> assertFalse(especificacion.esSatisfechoPor(productoCaro), "Producto caro debería quedar excluido")
        );
    }

    @Test
    @DisplayName("Especificación por precio debe filtrar solo por precio mínimo")
    void especificacionPorPrecio_debeFiltrarSoloPorPrecioMinimo() {
        Producto productoBarato = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);
        Producto productoMedio = crearProducto(PRODUCTO_ID_2, NOMBRE_CELULAR_PRO, CATEGORIA_ELECTRONICA, PRECIO_1200_00);
        Producto productoCaro = crearProducto(PRODUCTO_ID_3, NOMBRE_TABLET, CATEGORIA_ELECTRONICA, PRECIO_1800_00);

        EspecificacionPorPrecio especificacion = new EspecificacionPorPrecio(1000.0, null);

        assertAll("Verificar filtrado solo por precio mínimo",
            () -> assertFalse(especificacion.esSatisfechoPor(productoBarato)),
            () -> assertTrue(especificacion.esSatisfechoPor(productoMedio)),
            () -> assertTrue(especificacion.esSatisfechoPor(productoCaro))
        );
    }

    @Test
    @DisplayName("Especificación por precio debe filtrar solo por precio máximo")
    void especificacionPorPrecio_debeFiltrarSoloPorPrecioMaximo() {
        Producto productoBarato = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);
        Producto productoMedio = crearProducto(PRODUCTO_ID_2, NOMBRE_CELULAR_PRO, CATEGORIA_ELECTRONICA, PRECIO_1200_00);
        Producto productoCaro = crearProducto(PRODUCTO_ID_3, NOMBRE_TABLET, CATEGORIA_ELECTRONICA, PRECIO_1800_00);

        EspecificacionPorPrecio especificacion = new EspecificacionPorPrecio(null, 1300.0);

        assertAll("Verificar filtrado solo por precio máximo",
            () -> assertTrue(especificacion.esSatisfechoPor(productoBarato)),
            () -> assertTrue(especificacion.esSatisfechoPor(productoMedio)),
            () -> assertFalse(especificacion.esSatisfechoPor(productoCaro))
        );
    }

    @Test
    @DisplayName("Especificación por precio debe incluir productos en los límites exactos")
    void especificacionPorPrecio_debeIncluirProductosEnLimitesExactos() {
        double precioLimite = 1000.0;
        Producto productoEnLimiteMinimo = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, precioLimite);
        Producto productoEnLimiteMaximo = crearProducto(PRODUCTO_ID_2, NOMBRE_TABLET, CATEGORIA_ELECTRONICA, precioLimite);

        EspecificacionPorPrecio especificacionMin = new EspecificacionPorPrecio(precioLimite, null);
        EspecificacionPorPrecio especificacionMax = new EspecificacionPorPrecio(null, precioLimite);

        assertAll("Verificar inclusión en límites exactos",
            () -> assertTrue(especificacionMin.esSatisfechoPor(productoEnLimiteMinimo)),
            () -> assertTrue(especificacionMax.esSatisfechoPor(productoEnLimiteMaximo))
        );
    }

    @Test
    @DisplayName("Especificación por precio sin límites debe aceptar cualquier producto")
    void especificacionPorPrecio_sinLimitesDebeAceptarCualquierProducto() {
        Producto productoBarato = crearProducto(PRODUCTO_ID_1, NOMBRE_CELULAR, CATEGORIA_ELECTRONICA, PRECIO_500_00);
        Producto productoCaro = crearProducto(PRODUCTO_ID_2, NOMBRE_TABLET, CATEGORIA_ELECTRONICA, PRECIO_1800_00);

        EspecificacionPorPrecio especificacion = new EspecificacionPorPrecio(null, null);

        assertAll("Verificar aceptación sin límites",
            () -> assertTrue(especificacion.esSatisfechoPor(productoBarato)),
            () -> assertTrue(especificacion.esSatisfechoPor(productoCaro))
        );
    }
}
