package com.pruebatecnica.meli.dominio.modelo;

import java.util.List;

public record ResultadoPaginado<T>(List<T> elementos, int paginaActual, int totalPaginas, long totalElementos) {}
