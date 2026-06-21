package com.aa.reservas.service;

import com.aa.reservas.dto.RestauranteResumenDTO;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Restaurante;
import com.aa.reservas.model.Reserva;
import com.aa.reservas.repository.EmpleadoRepository;
import com.aa.reservas.repository.MesaRepository;
import com.aa.reservas.repository.ReservaRepository;
import com.aa.reservas.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestauranteService {
    private final RestauranteRepository restauranteRepository;
    private final MesaRepository mesaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ReservaRepository reservaRepository;
    private final ObjectMapper objectMapper;

    public RestauranteService(RestauranteRepository restauranteRepository, MesaRepository mesaRepository, EmpleadoRepository empleadoRepository, ReservaRepository reservaRepository, ObjectMapper objectMapper) {
        this.restauranteRepository = restauranteRepository;
        this.mesaRepository = mesaRepository;
        this.empleadoRepository = empleadoRepository;
        this.reservaRepository = reservaRepository;
        this.objectMapper = objectMapper;
    }

    public List<Restaurante> findAll(String nombre, Boolean activo, Integer aforoMinimo) {
        return restauranteRepository.findAll().stream()
            .filter(r -> nombre == null || r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(r -> activo == null || r.isActivo() == activo)
            .filter(r -> aforoMinimo == null || r.getAforoMaximo() >= aforoMinimo)
            .collect(Collectors.toList());
    }

    public Restaurante findById(Long id) {
        return restauranteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No existe restaurante con id " + id));
    }

    public Restaurante create(Restaurante restaurante) {
        restaurante.setId(null);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante update(Long id, Restaurante restaurante) {
        findById(id);
        restaurante.setId(id);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante patch(Long id, Map<String, Object> fields) {
        Restaurante restaurante = findById(id);
        PatchUtils.applyPatch(restaurante, fields, objectMapper);
        return restauranteRepository.save(restaurante);
    }

    public void delete(Long id) {
        Restaurante restaurante = findById(id);
        restauranteRepository.delete(restaurante);
    }

    public List<Restaurante> findRestaurantesActivosNative() {
        return restauranteRepository.findRestaurantesActivosNative();
    }

    public RestauranteResumenDTO getResumen(Long restauranteId) {
        Restaurante restaurante = findById(restauranteId);
        var mesas = mesaRepository.findAll().stream()
            .filter(m -> m.getRestaurante() != null && restauranteId.equals(m.getRestaurante().getId()))
            .toList();
        var empleados = empleadoRepository.findAll().stream()
            .filter(e -> e.getRestaurante() != null && restauranteId.equals(e.getRestaurante().getId()))
            .toList();
        List<Reserva> reservasConfirmadas = reservaRepository.findConfirmadasByRestauranteJpql(restauranteId);
        int capacidadTotal = mesas.stream().mapToInt(m -> m.getCapacidad()).sum();
        return new RestauranteResumenDTO(restaurante.getId(), restaurante.getNombre(), mesas.size(), empleados.size(), reservasConfirmadas.size(), capacidadTotal);
    }
}
