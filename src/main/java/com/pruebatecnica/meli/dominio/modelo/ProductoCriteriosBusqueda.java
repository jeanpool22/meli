package com.pruebatecnica.meli.dominio.modelo;

import java.util.Optional;

public record ProductoCriteriosBusqueda(
    Optional<String> nombre,
    Optional<String> categoria,
    Optional<Double> precioMin,
    Optional<Double> precioMax,
    int pagina,
    int tamanioPagina
) {}
