package com.aa.reservas.service;

import com.aa.reservas.exception.BadRequestException;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Mesa;
import com.aa.reservas.repository.MesaRepository;
import com.aa.reservas.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MesaService {
    private final MesaRepository mesaRepository;
    private final RestauranteRepository restauranteRepository;
    private final ObjectMapper objectMapper;

    public MesaService(MesaRepository mesaRepository, RestauranteRepository restauranteRepository, ObjectMapper objectMapper) {
        this.mesaRepository = mesaRepository;
        this.restauranteRepository = restauranteRepository;
        this.objectMapper = objectMapper;
    }

    public List<Mesa> findAll(Long restauranteId, Integer capacidadMinima, Boolean reservable) {
        return mesaRepository.findAll().stream()
            .filter(m -> restauranteId == null || (m.getRestaurante() != null && restauranteId.equals(m.getRestaurante().getId())))
            .filter(m -> capacidadMinima == null || m.getCapacidad() >= capacidadMinima)
            .filter(m -> reservable == null || m.isReservable() == reservable)
            .collect(Collectors.toList());
    }

    public Mesa findById(Long id) {
        return mesaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No existe mesa con id " + id));
    }

    public Mesa create(Mesa mesa) {
        mesa.setId(null);
        resolveRestaurante(mesa);
        return mesaRepository.save(mesa);
    }

    public Mesa update(Long id, Mesa mesa) {
        findById(id);
        mesa.setId(id);
        resolveRestaurante(mesa);
        return mesaRepository.save(mesa);
    }

    public Mesa patch(Long id, Map<String, Object> fields) {
        Mesa mesa = findById(id);
        PatchUtils.applyPatch(mesa, fields, objectMapper);
        resolveRestaurante(mesa);
        return mesaRepository.save(mesa);
    }

    public void delete(Long id) {
        Mesa mesa = findById(id);
        mesaRepository.delete(mesa);
    }

    public List<Mesa> findMesasDisponiblesJpql(Long restauranteId, int capacidadMinima) {
        return mesaRepository.findMesasDisponiblesJpql(restauranteId, capacidadMinima);
    }

    private void resolveRestaurante(Mesa mesa) {
        if (mesa.getRestaurante() == null || mesa.getRestaurante().getId() == null) {
            throw new BadRequestException("La mesa debe indicar restaurante.id");
        }
        mesa.setRestaurante(restauranteRepository.findById(mesa.getRestaurante().getId())
            .orElseThrow(() -> new NotFoundException("No existe restaurante con id " + mesa.getRestaurante().getId())));
    }
}
