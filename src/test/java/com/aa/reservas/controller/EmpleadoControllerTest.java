package com.aa.reservas.controller;

import com.aa.reservas.exception.GlobalExceptionHandler;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.*;
import com.aa.reservas.service.EmpleadoService;

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

@WebMvcTest(EmpleadoController.class)
@Import(GlobalExceptionHandler.class)
class EmpleadoControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean EmpleadoService service;

    @Test
    void findAll_devuelve200() throws Exception {
        when(service.findAll(any(), any(), any())).thenReturn(List.of(new Empleado(1L, "Carlos", "carlos@bj.com", "Sala", 12.5f, true, LocalDate.now(), new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now()))));
        mockMvc.perform(get("/api/empleados"))
            .andExpect(status().isOk());
    }

    @Test
    void findById_devuelve404_siNoExiste() throws Exception {
        when(service.findById(99L)).thenThrow(new NotFoundException("No existe"));
        mockMvc.perform(get("/api/empleados/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void create_devuelve400_siBodyNoValido() throws Exception {
        mockMvc.perform(post("/api/empleados")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"Carlos","email":"mal","puesto":"Sala","sueldoHora":12.5,"activo":true,"fechaContratacion":"2024-01-01","restaurante":{"id":1}}
"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_devuelve201_siBodyValido() throws Exception {
        when(service.create(any(Empleado.class))).thenReturn(new Empleado(1L, "Carlos", "carlos@bj.com", "Sala", 12.5f, true, LocalDate.now(), new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now())));
        mockMvc.perform(post("/api/empleados")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"nombre":"Carlos","email":"carlos@bj.com","puesto":"Sala","sueldoHora":12.5,"activo":true,"fechaContratacion":"2024-01-01","restaurante":{"id":1}}
"""))
            .andExpect(status().isCreated());
    }

}

