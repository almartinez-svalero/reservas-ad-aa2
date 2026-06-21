package com.aa.reservas.service;

import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Mesa;
import com.aa.reservas.model.Restaurante;
import com.aa.reservas.repository.MesaRepository;
import com.aa.reservas.repository.RestauranteRepository;
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
class MesaServiceTest {
    @Mock MesaRepository mesaRepository;
    @Mock RestauranteRepository restauranteRepository;
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void findById_devuelveMesa_siExiste() {
        when(mesaRepository.findById(1L)).thenReturn(Optional.of(sample()));
        Mesa result = new MesaService(mesaRepository, restauranteRepository, objectMapper).findById(1L);
        assertThat(result.getCapacidad()).isEqualTo(4);
    }

    @Test
    void findById_lanzaNotFound_siNoExiste() {
        when(mesaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> new MesaService(mesaRepository, restauranteRepository, objectMapper).findById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findAll_filtraPorRestauranteCapacidadReservable() {
        when(mesaRepository.findAll()).thenReturn(List.of(sample()));
        var result = new MesaService(mesaRepository, restauranteRepository, objectMapper).findAll(1L, 2, true);
        assertThat(result).hasSize(1);
    }

    private Mesa sample() {
        Restaurante r = new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now());
        return new Mesa(1L, 1, 4, "Salón", true, 0f, LocalDate.now(), r);
    }
}
