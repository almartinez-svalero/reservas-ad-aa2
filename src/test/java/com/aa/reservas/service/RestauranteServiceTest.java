package com.aa.reservas.service;

import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Restaurante;
import com.aa.reservas.repository.EmpleadoRepository;
import com.aa.reservas.repository.MesaRepository;
import com.aa.reservas.repository.ReservaRepository;
import com.aa.reservas.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceTest {
    @Mock RestauranteRepository restauranteRepository;
    @Mock MesaRepository mesaRepository;
    @Mock EmpleadoRepository empleadoRepository;
    @Mock ReservaRepository reservaRepository;
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void findById_devuelveRestaurante_siExiste() {
        Restaurante r = sample();
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(r));
        Restaurante result = new RestauranteService(restauranteRepository, mesaRepository, empleadoRepository, reservaRepository, objectMapper).findById(1L);
        assertThat(result.getNombre()).isEqualTo("BJ Reservas Centro");
    }

    @Test
    void findById_lanzaNotFound_siNoExiste() {
        when(restauranteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> new RestauranteService(restauranteRepository, mesaRepository, empleadoRepository, reservaRepository, objectMapper).findById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findAll_filtraPorNombreActivoYAforo() {
        when(restauranteRepository.findAll()).thenReturn(List.of(sample()));
        var result = new RestauranteService(restauranteRepository, mesaRepository, empleadoRepository, reservaRepository, objectMapper).findAll("BJ", true, 50);
        assertThat(result).hasSize(1);
    }

    private Restaurante sample() {
        return new Restaurante(1L, "BJ Reservas Centro", "Calle Alfonso I 10", "+34976000001", 80, 4.5f, true, LocalDate.now());
    }
}
