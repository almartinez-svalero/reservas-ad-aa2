package com.aa.reservas.service;

import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Cliente;
import com.aa.reservas.repository.ClienteRepository;
import com.aa.reservas.repository.ReservaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock ClienteRepository clienteRepository;
    @Mock ReservaRepository reservaRepository;
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void findById_devuelveCliente_siExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(sample()));
        Cliente result = new ClienteService(clienteRepository, reservaRepository, objectMapper).findById(1L);
        assertThat(result.getEmail()).contains("email.com");
    }

    @Test
    void findById_lanzaNotFound_siNoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> new ClienteService(clienteRepository, reservaRepository, objectMapper).findById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findAll_filtraPorVip() {
        when(clienteRepository.findAll()).thenReturn(List.of(sample()));
        var result = new ClienteService(clienteRepository, reservaRepository, objectMapper).findAll("Laura", "email", true);
        assertThat(result).hasSize(1);
    }

    private Cliente sample() {
        return new Cliente(1L, "Laura Gómez", "laura.gomez@email.com", "+34666000111", 5, 0f, true, LocalDate.now());
    }
}
