package com.aa.reservas.controller;

import com.aa.reservas.config.JwtService;
import com.aa.reservas.exception.GlobalExceptionHandler;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.*;
import com.aa.reservas.service.RestauranteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestauranteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class RestauranteControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean RestauranteService service;
    @MockBean JwtService jwtService;

    @Test
    void findAll_devuelve200() throws Exception {
        when(service.findAll(any(), any(), any())).thenReturn(List.of(new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now())));
        mockMvc.perform(get("/api/restaurantes"))
            .andExpect(status().isOk());
    }

    @Test
    void findById_devuelve404_siNoExiste() throws Exception {
        when(service.findById(99L)).thenThrow(new NotFoundException("No existe"));
        mockMvc.perform(get("/api/restaurantes/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void create_devuelve400_siBodyNoValido() throws Exception {
        mockMvc.perform(post("/api/restaurantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"","direccion":"Calle","telefono":"+34976000001","aforoMaximo":80,"valoracionMedia":4.5,"activo":true,"fechaApertura":"2024-01-01"}
"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_devuelve201_siBodyValido() throws Exception {
        when(service.create(any(Restaurante.class))).thenReturn(new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now()));
        mockMvc.perform(post("/api/restaurantes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"BJ","direccion":"Calle","telefono":"+34976000001","aforoMaximo":80,"valoracionMedia":4.5,"activo":true,"fechaApertura":"2024-01-01"}
"""))
            .andExpect(status().isCreated());
    }

}

