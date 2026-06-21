package com.aa.reservas.service;

import com.aa.reservas.exception.BadRequestException;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.*;
import com.aa.reservas.repository.ClienteRepository;
import com.aa.reservas.repository.EmpleadoRepository;
import com.aa.reservas.repository.MesaRepository;
import com.aa.reservas.repository.ReservaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {
    @Mock ReservaRepository reservaRepository;
    @Mock ClienteRepository clienteRepository;
    @Mock MesaRepository mesaRepository;
    @Mock EmpleadoRepository empleadoRepository;
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void findById_devuelveReserva_siExiste() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(sample(true, 4, 4)));
        Reserva result = service().findById(1L);
        assertThat(result.getCodigo()).isEqualTo("RES-001");
    }

    @Test
    void findById_lanzaNotFound_siNoExiste() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service().findById(99L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void create_lanzaBadRequest_siMesaNoEsReservable() {
        Reserva reserva = sample(false, 4, 4);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(reserva.getCliente()));
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(reserva.getMesa()));
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(reserva.getEmpleado()));
        assertThatThrownBy(() -> service().create(reserva)).isInstanceOf(BadRequestException.class);
    }

    @Test
    void findAll_filtraPorClienteFechaConfirmada() {
        Reserva reserva = sample(true, 4, 4);
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));
        var result = service().findAll(1L, LocalDate.of(2026,1,15), true);
        assertThat(result).hasSize(1);
    }

    private ReservaService service() {
        return new ReservaService(reservaRepository, clienteRepository, mesaRepository, empleadoRepository, objectMapper);
    }

    private Reserva sample(boolean reservable, int personas, int capacidad) {
        Restaurante restaurante = new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now());
        Cliente cliente = new Cliente(1L, "Laura", "laura@email.com", "+34666000111", 5, 0f, true, LocalDate.now());
        Mesa mesa = new Mesa(1L, 1, capacidad, "Salón", reservable, 0f, LocalDate.now(), restaurante);
        Empleado empleado = new Empleado(1L, "Carlos", "carlos@bj.com", "Sala", 12f, true, LocalDate.now(), restaurante);
        return new Reserva(1L, "RES-001", LocalDate.of(2026,1,15), LocalTime.of(21,0), personas, 20f, true, "Cena", cliente, mesa, empleado);
    }
}
