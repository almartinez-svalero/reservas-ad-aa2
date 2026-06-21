package com.aa.reservas.controller;

import com.aa.reservas.config.JwtService;
import com.aa.reservas.exception.GlobalExceptionHandler;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.*;
import com.aa.reservas.service.MesaService;

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

@WebMvcTest(MesaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class MesaControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean MesaService service;
    @MockBean JwtService jwtService;

    @Test
    void findAll_devuelve200() throws Exception {
        when(service.findAll(any(), any(), any())).thenReturn(List.of(new Mesa(1L, 1, 4, "SalÃ³n", true, 0f, LocalDate.now(), new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now()))));
        mockMvc.perform(get("/api/mesas"))
            .andExpect(status().isOk());
    }

    @Test
    void findById_devuelve404_siNoExiste() throws Exception {
        when(service.findById(99L)).thenThrow(new NotFoundException("No existe"));
        mockMvc.perform(get("/api/mesas/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void create_devuelve400_siBodyNoValido() throws Exception {
        mockMvc.perform(post("/api/mesas")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"numero":1,"capacidad":0,"ubicacion":"SalÃ³n","reservable":true,"suplementoTerraza":0,"fechaAlta":"2024-01-01","restaurante":{"id":1}}
"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_devuelve201_siBodyValido() throws Exception {
        when(service.create(any(Mesa.class))).thenReturn(new Mesa(1L, 1, 4, "SalÃ³n", true, 0f, LocalDate.now(), new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now())));
        mockMvc.perform(post("/api/mesas")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"numero":1,"capacidad":4,"ubicacion":"SalÃ³n","reservable":true,"suplementoTerraza":0,"fechaAlta":"2024-01-01","restaurante":{"id":1}}
"""))
            .andExpect(status().isCreated());
    }

}

