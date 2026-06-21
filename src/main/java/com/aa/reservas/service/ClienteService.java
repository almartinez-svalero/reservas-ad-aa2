package com.aa.reservas.service;

import com.aa.reservas.dto.ClienteHistorialDTO;
import com.aa.reservas.dto.ReservaResumenDTO;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Cliente;
import com.aa.reservas.model.Reserva;
import com.aa.reservas.repository.ClienteRepository;
import com.aa.reservas.repository.ReservaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ReservaRepository reservaRepository;
    private final ObjectMapper objectMapper;

    public ClienteService(ClienteRepository clienteRepository, ReservaRepository reservaRepository, ObjectMapper objectMapper) {
        this.clienteRepository = clienteRepository;
        this.reservaRepository = reservaRepository;
        this.objectMapper = objectMapper;
    }

    public List<Cliente> findAll(String nombre, String email, Boolean vip) {
        return clienteRepository.findAll().stream()
            .filter(c -> nombre == null || c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(c -> email == null || c.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(c -> vip == null || c.isVip() == vip)
            .collect(Collectors.toList());
    }

    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No existe cliente con id " + id));
    }

    public Cliente create(Cliente cliente) {
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        findById(id);
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }

    public Cliente patch(Long id, Map<String, Object> fields) {
        Cliente cliente = findById(id);
        PatchUtils.applyPatch(cliente, fields, objectMapper);
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        Cliente cliente = findById(id);
        clienteRepository.delete(cliente);
    }

    public List<Cliente> findClientesVipConReservasJpql() {
        return clienteRepository.findClientesVipConReservasJpql();
    }

    public ClienteHistorialDTO getHistorial(Long clienteId) {
        Cliente cliente = findById(clienteId);
        List<ReservaResumenDTO> reservas = reservaRepository.findAll().stream()
            .filter(r -> r.getCliente() != null && clienteId.equals(r.getCliente().getId()))
            .map(this::toResumen)
            .toList();
        return new ClienteHistorialDTO(cliente.getId(), cliente.getNombre(), cliente.getEmail(), reservas.size(), reservas);
    }

    private ReservaResumenDTO toResumen(Reserva r) {
        return new ReservaResumenDTO(
            r.getId(),
            r.getCodigo(),
            r.getFechaReserva(),
            r.getHoraReserva(),
            r.getNumeroPersonas(),
            r.isConfirmada(),
            r.getCliente() != null ? r.getCliente().getNombre() : null,
            r.getMesa() != null ? r.getMesa().getNumero() : 0,
            r.getEmpleado() != null ? r.getEmpleado().getNombre() : null
        );
    }
}
