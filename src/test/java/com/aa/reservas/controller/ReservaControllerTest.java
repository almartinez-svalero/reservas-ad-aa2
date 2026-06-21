package com.aa.reservas.controller;

import com.aa.reservas.config.JwtService;
import com.aa.reservas.exception.GlobalExceptionHandler;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.*;
import com.aa.reservas.service.ReservaService;
import com.aa.reservas.dto.ReservaCreateDTO;

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

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ReservaControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean ReservaService service;
    @MockBean JwtService jwtService;

    @Test
    void findAll_devuelve200() throws Exception {
        when(service.findAll(any(), any(), any())).thenReturn(List.of(sampleReserva()));
        mockMvc.perform(get("/api/reservas"))
            .andExpect(status().isOk());
    }

    @Test
    void findById_devuelve404_siNoExiste() throws Exception {
        when(service.findById(99L)).thenThrow(new NotFoundException("No existe"));
        mockMvc.perform(get("/api/reservas/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void create_devuelve400_siBodyNoValido() throws Exception {
        mockMvc.perform(post("/api/reservas")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"codigo":"","fechaReserva":"2026-01-15","horaReserva":"21:00:00","numeroPersonas":4,"importeSenal":20,"confirmada":true,"observaciones":"Cena","cliente":{"id":1},"mesa":{"id":1},"empleado":{"id":1}}
"""))
            .andExpect(status().isBadRequest());
    }

    @Test
    void create_devuelve201_siBodyValido() throws Exception {
        when(service.create(any(Reserva.class))).thenReturn(sampleReserva());
        mockMvc.perform(post("/api/reservas")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
{"codigo":"RES-001","fechaReserva":"2026-01-15","horaReserva":"21:00:00","numeroPersonas":4,"importeSenal":20,"confirmada":true,"observaciones":"Cena","cliente":{"id":1},"mesa":{"id":1},"empleado":{"id":1}}
"""))
            .andExpect(status().isCreated());
    }

private Reserva sampleReserva() {
    Restaurante restaurante = new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now());
    Cliente cliente = new Cliente(1L, "Laura", "laura@email.com", "+34666000111", 5, 0f, true, LocalDate.now());
    Mesa mesa = new Mesa(1L, 1, 4, "SalÃ³n", true, 0f, LocalDate.now(), restaurante);
    Empleado empleado = new Empleado(1L, "Carlos", "carlos@bj.com", "Sala", 12.5f, true, LocalDate.now(), restaurante);
    return new Reserva(1L, "RES-001", LocalDate.of(2026,1,15), LocalTime.of(21,0), 4, 20f, true, "Cena", cliente, mesa, empleado);
}

}

