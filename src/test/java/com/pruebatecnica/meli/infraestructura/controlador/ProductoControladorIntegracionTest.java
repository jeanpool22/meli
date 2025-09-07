package com.pruebatecnica.meli.infraestructura.controlador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductoControladorIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarProductos_debeRetornarListaDeProductos() throws Exception {
        mockMvc.perform(get("/productos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.elementos[0].idProducto").exists())
                .andExpect(jsonPath("$.elementos[0].nombre").exists())
                .andExpect(jsonPath("$.elementos[0].especificaciones").isArray());
    }

    @Test
    void obtenerProductoPorId_debeRetornarProductoSiExiste() throws Exception {
        mockMvc.perform(get("/productos/{idProducto}", 1L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1))
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.precio").exists());
    }

    @Test
    void obtenerProductoPorId_debeRetornar404SiNoExiste() throws Exception {
        mockMvc.perform(get("/productos/{idProducto}", 9999L).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.titulo").value("Producto no encontrado"));
    }
}
