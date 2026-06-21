package com.aa.reservas.controller;

import com.aa.reservas.exception.GlobalExceptionHandler;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.*;
import com.aa.reservas.service.ClienteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(ClienteController.class)
@Import(GlobalExceptionHandler.class)
class ClienteControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ClienteService service;

    @Test
    void findAll_devuelve200() throws Exception {
        when(service.findAll(any(), any(), any())).thenReturn(List.of(new Cliente(1L, "Laura", "laura@email.com", "+34666000111", 5, 0f, true, LocalDate.now())));
        mockMvc.perform(get("/api/clientes"))
            .andExpect(status().isOk());
    }

    @Test
    void findById_devuelve404_siNoExiste() throws Exception {
        when(service.findById(99L)).thenThrow(new NotFoundException("No existe"));
        mockMvc.perform(get("/api/clientes/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void create_devuelve400_siBodyNoValido() throws Exception {
        mockMvc.perform(post("/api/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"Laura","email":"mal","telefono":"+34666000111","numeroVisitas":5,"saldoPendiente":0,"vip":true,"fechaRegistro":"2024-01-01"}
"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_devuelve201_siBodyValido() throws Exception {
        when(service.create(any(Cliente.class))).thenReturn(new Cliente(1L, "Laura", "laura@email.com", "+34666000111", 5, 0f, true, LocalDate.now()));
        mockMvc.perform(post("/api/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"Laura","email":"laura@email.com","telefono":"+34666000111","numeroVisitas":5,"saldoPendiente":0,"vip":true,"fechaRegistro":"2024-01-01"}
"""))
            .andExpect(status().isCreated());
    }

}

