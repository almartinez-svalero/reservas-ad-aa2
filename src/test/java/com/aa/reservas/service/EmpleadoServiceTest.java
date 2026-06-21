package com.aa.reservas.service;

import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Empleado;
import com.aa.reservas.model.Restaurante;
import com.aa.reservas.repository.EmpleadoRepository;
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
class EmpleadoServiceTest {
    @Mock EmpleadoRepository empleadoRepository;
    @Mock RestauranteRepository restauranteRepository;
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void findById_devuelveEmpleado_siExiste() {
        when(empleadoRepository.findById(1L)).thenReturn(Optional.of(sample()));
        Empleado result = new EmpleadoService(empleadoRepository, restauranteRepository, objectMapper).findById(1L);
        assertThat(result.getPuesto()).contains("sala");
    }

    @Test
    void findById_lanzaNotFound_siNoExiste() {
        when(empleadoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> new EmpleadoService(empleadoRepository, restauranteRepository, objectMapper).findById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findAll_filtraPorRestaurantePuestoActivo() {
        when(empleadoRepository.findAll()).thenReturn(List.of(sample()));
        var result = new EmpleadoService(empleadoRepository, restauranteRepository, objectMapper).findAll(1L, "sala", true);
        assertThat(result).hasSize(1);
    }

    private Empleado sample() {
        Restaurante r = new Restaurante(1L, "BJ", "Calle", "+34976000001", 80, 4.5f, true, LocalDate.now());
        return new Empleado(1L, "Carlos Ruiz", "carlos@bj.com", "Encargado de sala", 12.5f, true, LocalDate.now(), r);
    }
}
