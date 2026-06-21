package com.aa.reservas.service;

import com.aa.reservas.exception.BadRequestException;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Empleado;
import com.aa.reservas.repository.EmpleadoRepository;
import com.aa.reservas.repository.RestauranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final RestauranteRepository restauranteRepository;
    private final ObjectMapper objectMapper;

    public EmpleadoService(EmpleadoRepository empleadoRepository, RestauranteRepository restauranteRepository, ObjectMapper objectMapper) {
        this.empleadoRepository = empleadoRepository;
        this.restauranteRepository = restauranteRepository;
        this.objectMapper = objectMapper;
    }

    public List<Empleado> findAll(Long restauranteId, String puesto, Boolean activo) {
        return empleadoRepository.findAll().stream()
            .filter(e -> restauranteId == null || (e.getRestaurante() != null && restauranteId.equals(e.getRestaurante().getId())))
            .filter(e -> puesto == null || e.getPuesto().toLowerCase().contains(puesto.toLowerCase()))
            .filter(e -> activo == null || e.isActivo() == activo)
            .collect(Collectors.toList());
    }

    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No existe empleado con id " + id));
    }

    public Empleado create(Empleado empleado) {
        empleado.setId(null);
        resolveRestaurante(empleado);
        return empleadoRepository.save(empleado);
    }

    public Empleado update(Long id, Empleado empleado) {
        findById(id);
        empleado.setId(id);
        resolveRestaurante(empleado);
        return empleadoRepository.save(empleado);
    }

    public Empleado patch(Long id, Map<String, Object> fields) {
        Empleado empleado = findById(id);
        PatchUtils.applyPatch(empleado, fields, objectMapper);
        resolveRestaurante(empleado);
        return empleadoRepository.save(empleado);
    }

    public void delete(Long id) {
        Empleado empleado = findById(id);
        empleadoRepository.delete(empleado);
    }

    public List<Empleado> findByRestauranteNative(Long restauranteId) {
        return empleadoRepository.findByRestauranteNative(restauranteId);
    }

    private void resolveRestaurante(Empleado empleado) {
        if (empleado.getRestaurante() == null || empleado.getRestaurante().getId() == null) {
            throw new BadRequestException("El empleado debe indicar restaurante.id");
        }
        empleado.setRestaurante(restauranteRepository.findById(empleado.getRestaurante().getId())
            .orElseThrow(() -> new NotFoundException("No existe restaurante con id " + empleado.getRestaurante().getId())));
    }
}
