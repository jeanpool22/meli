package com.pruebatecnica.meli.dominio.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ProductoCriteriosBusquedaTest {

    private static final String NOMBRE_FILTRO = "Celular";
    private static final String CATEGORIA_ELECTRONICA = "Electrónica";
    private static final Double PRECIO_MIN_100 = 100.0;
    private static final Double PRECIO_MAX_2000 = 2000.0;
    private static final Double PRECIO_MIN_500 = 500.0;
    private static final int PAGINA_0 = 0;
    private static final int PAGINA_1 = 1;
    private static final int TAMANIO_PAGINA_10 = 10;
    private static final int TAMANIO_PAGINA_5 = 5;

    private ProductoCriteriosBusqueda crearCriteriosBusqueda(
            Optional<String> nombre,
            Optional<String> categoria,
            Optional<Double> precioMin,
            Optional<Double> precioMax,
            int pagina,
            int tamanioPagina) {
        return new ProductoCriteriosBusqueda(nombre, categoria, precioMin, precioMax, pagina, tamanioPagina);
    }

    @Test
    @DisplayName("Debe crear criterios de búsqueda básicos sin filtros")
    void crearCriteriosBusqueda_sinFiltros() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertAll("Verificar criterios básicos",
            () -> assertTrue(criterios.nombre().isEmpty()),
            () -> assertTrue(criterios.categoria().isEmpty()),
            () -> assertTrue(criterios.precioMin().isEmpty()),
            () -> assertTrue(criterios.precioMax().isEmpty()),
            () -> assertEquals(PAGINA_0, criterios.pagina()),
            () -> assertEquals(TAMANIO_PAGINA_10, criterios.tamanioPagina())
        );
    }

    @Test
    @DisplayName("Debe crear criterios de búsqueda con todos los filtros")
    void crearCriteriosBusqueda_conTodosLosFiltros() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.of(NOMBRE_FILTRO),
                Optional.of(CATEGORIA_ELECTRONICA),
                Optional.of(PRECIO_MIN_100),
                Optional.of(PRECIO_MAX_2000),
                PAGINA_1,
                TAMANIO_PAGINA_5);

        assertAll("Verificar criterios con todos los filtros",
            () -> assertTrue(criterios.nombre().isPresent()),
            () -> assertEquals(NOMBRE_FILTRO, criterios.nombre().orElse(null)),
            () -> assertTrue(criterios.categoria().isPresent()),
            () -> assertEquals(CATEGORIA_ELECTRONICA, criterios.categoria().orElse(null)),
            () -> assertTrue(criterios.precioMin().isPresent()),
            () -> assertEquals(PRECIO_MIN_100, criterios.precioMin().orElse(null)),
            () -> assertTrue(criterios.precioMax().isPresent()),
            () -> assertEquals(PRECIO_MAX_2000, criterios.precioMax().orElse(null)),
            () -> assertEquals(PAGINA_1, criterios.pagina()),
            () -> assertEquals(TAMANIO_PAGINA_5, criterios.tamanioPagina())
        );
    }

    @Test
    @DisplayName("Debe crear criterios de búsqueda solo con filtro de nombre")
    void crearCriteriosBusqueda_soloFiltroNombre() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.of(NOMBRE_FILTRO),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertAll("Verificar criterio solo con nombre",
            () -> assertTrue(criterios.nombre().isPresent()),
            () -> assertEquals(NOMBRE_FILTRO, criterios.nombre().orElse(null)),
            () -> assertTrue(criterios.categoria().isEmpty()),
            () -> assertTrue(criterios.precioMin().isEmpty()),
            () -> assertTrue(criterios.precioMax().isEmpty())
        );
    }

    @Test
    @DisplayName("Debe crear criterios de búsqueda solo con filtro de categoría")
    void crearCriteriosBusqueda_soloFiltroCategoria() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.empty(),
                Optional.of(CATEGORIA_ELECTRONICA),
                Optional.empty(),
                Optional.empty(),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertAll("Verificar criterio solo con categoría",
            () -> assertTrue(criterios.nombre().isEmpty()),
            () -> assertTrue(criterios.categoria().isPresent()),
            () -> assertEquals(CATEGORIA_ELECTRONICA, criterios.categoria().orElse(null)),
            () -> assertTrue(criterios.precioMin().isEmpty()),
            () -> assertTrue(criterios.precioMax().isEmpty())
        );
    }

    @Test
    @DisplayName("Debe crear criterios de búsqueda solo con filtro de precio mínimo")
    void crearCriteriosBusqueda_soloFiltroMinimo() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.empty(),
                Optional.empty(),
                Optional.of(PRECIO_MIN_500),
                Optional.empty(),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertAll("Verificar criterio con precio mínimo",
            () -> assertTrue(criterios.nombre().isEmpty()),
            () -> assertTrue(criterios.categoria().isEmpty()),
            () -> assertTrue(criterios.precioMin().isPresent()),
            () -> assertEquals(PRECIO_MIN_500, criterios.precioMin().orElse(null)),
            () -> assertTrue(criterios.precioMax().isEmpty())
        );
    }

    @Test
    @DisplayName("Debe crear criterios de búsqueda solo con filtro de precio máximo")
    void crearCriteriosBusqueda_soloFiltroMaximo() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(PRECIO_MAX_2000),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertAll("Verificar criterio con precio máximo",
            () -> assertTrue(criterios.nombre().isEmpty()),
            () -> assertTrue(criterios.categoria().isEmpty()),
            () -> assertTrue(criterios.precioMin().isEmpty()),
            () -> assertTrue(criterios.precioMax().isPresent()),
            () -> assertEquals(PRECIO_MAX_2000, criterios.precioMax().orElse(null))
        );
    }

    @Test
    @DisplayName("Debe crear criterios con rango de precios")
    void crearCriteriosBusqueda_conRangoDePrecios() {
        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.empty(),
                Optional.empty(),
                Optional.of(PRECIO_MIN_100),
                Optional.of(PRECIO_MAX_2000),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertAll("Verificar criterio con rango de precios",
            () -> assertTrue(criterios.precioMin().isPresent()),
            () -> assertTrue(criterios.precioMax().isPresent()),
            () -> assertEquals(PRECIO_MIN_100, criterios.precioMin().orElse(null)),
            () -> assertEquals(PRECIO_MAX_2000, criterios.precioMax().orElse(null)),
            () -> assertTrue(criterios.precioMin().orElse(0.0) < criterios.precioMax().orElse(0.0))
        );
    }

    @Test
    @DisplayName("Debe validar diferentes valores de paginación")
    void crearCriteriosBusqueda_conDiferentesValoresPaginacion() {
        ProductoCriteriosBusqueda criterios1 = crearCriteriosBusqueda(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0, 5);

        ProductoCriteriosBusqueda criterios2 = crearCriteriosBusqueda(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                2, 20);

        assertAll("Verificar diferentes valores de paginación",
            () -> assertEquals(0, criterios1.pagina()),
            () -> assertEquals(5, criterios1.tamanioPagina()),
            () -> assertEquals(2, criterios2.pagina()),
            () -> assertEquals(20, criterios2.tamanioPagina())
        );
    }

    @Test
    @DisplayName("Debe validar igualdad entre criterios de búsqueda")
    void criteriosBusqueda_debeValidarIgualdad() {
        ProductoCriteriosBusqueda criterios1 = crearCriteriosBusqueda(
                Optional.of(NOMBRE_FILTRO),
                Optional.of(CATEGORIA_ELECTRONICA),
                Optional.of(PRECIO_MIN_100),
                Optional.of(PRECIO_MAX_2000),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        ProductoCriteriosBusqueda criterios2 = crearCriteriosBusqueda(
                Optional.of(NOMBRE_FILTRO),
                Optional.of(CATEGORIA_ELECTRONICA),
                Optional.of(PRECIO_MIN_100),
                Optional.of(PRECIO_MAX_2000),
                PAGINA_0,
                TAMANIO_PAGINA_10);

        assertEquals(criterios1, criterios2);
        assertEquals(criterios1.hashCode(), criterios2.hashCode());
    }

    @Test
    @DisplayName("Debe crear criterios con valores de paginación específicos")
    void crearCriteriosBusqueda_conValoresPaginacionEspecificos() {
        int paginaEspecifica = 5;
        int tamanioEspecifico = 25;

        ProductoCriteriosBusqueda criterios = crearCriteriosBusqueda(
                Optional.of(NOMBRE_FILTRO),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                paginaEspecifica,
                tamanioEspecifico);

        assertAll("Verificar valores específicos de paginación",
            () -> assertEquals(paginaEspecifica, criterios.pagina()),
            () -> assertEquals(tamanioEspecifico, criterios.tamanioPagina()),
            () -> assertTrue(criterios.nombre().isPresent()),
            () -> assertEquals(NOMBRE_FILTRO, criterios.nombre().orElse(null))
        );
    }
}
