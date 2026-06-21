package com.aa.reservas.service;

import com.aa.reservas.dto.ReservaCreateDTO;
import com.aa.reservas.dto.ReservaResumenDTO;
import com.aa.reservas.exception.BadRequestException;
import com.aa.reservas.exception.NotFoundException;
import com.aa.reservas.model.Empleado;
import com.aa.reservas.model.Mesa;
import com.aa.reservas.model.Reserva;
import com.aa.reservas.repository.ClienteRepository;
import com.aa.reservas.repository.EmpleadoRepository;
import com.aa.reservas.repository.MesaRepository;
import com.aa.reservas.repository.ReservaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ObjectMapper objectMapper;

    public ReservaService(ReservaRepository reservaRepository, ClienteRepository clienteRepository, MesaRepository mesaRepository, EmpleadoRepository empleadoRepository, ObjectMapper objectMapper) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.mesaRepository = mesaRepository;
        this.empleadoRepository = empleadoRepository;
        this.objectMapper = objectMapper;
    }

    public List<Reserva> findAll(Long clienteId, LocalDate fechaReserva, Boolean confirmada) {
        return reservaRepository.findAll().stream()
            .filter(r -> clienteId == null || (r.getCliente() != null && clienteId.equals(r.getCliente().getId())))
            .filter(r -> fechaReserva == null || fechaReserva.equals(r.getFechaReserva()))
            .filter(r -> confirmada == null || r.isConfirmada() == confirmada)
            .collect(Collectors.toList());
    }

    public Reserva findById(Long id) {
        return reservaRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("No existe reserva con id " + id));
    }

    public Reserva create(Reserva reserva) {
        reserva.setId(null);
        resolveRelations(reserva);
        validateMesa(reserva);
        return reservaRepository.save(reserva);
    }

    public Reserva createFromDto(ReservaCreateDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setCodigo(dto.codigo());
        reserva.setFechaReserva(dto.fechaReserva());
        reserva.setHoraReserva(dto.horaReserva());
        reserva.setNumeroPersonas(dto.numeroPersonas());
        reserva.setImporteSenal(dto.importeSenal());
        reserva.setConfirmada(dto.confirmada());
        reserva.setObservaciones(dto.observaciones());
        reserva.setCliente(clienteRepository.findById(dto.clienteId())
            .orElseThrow(() -> new NotFoundException("No existe cliente con id " + dto.clienteId())));
        Mesa mesa = mesaRepository.findById(dto.mesaId())
            .orElseThrow(() -> new NotFoundException("No existe mesa con id " + dto.mesaId()));
        reserva.setMesa(mesa);
        Empleado empleado = empleadoRepository.findById(dto.empleadoId())
            .orElseThrow(() -> new NotFoundException("No existe empleado con id " + dto.empleadoId()));
        reserva.setEmpleado(empleado);
        validateMesa(reserva);
        return reservaRepository.save(reserva);
    }

    public Reserva update(Long id, Reserva reserva) {
        findById(id);
        reserva.setId(id);
        resolveRelations(reserva);
        validateMesa(reserva);
        return reservaRepository.save(reserva);
    }

    public Reserva patch(Long id, Map<String, Object> fields) {
        Reserva reserva = findById(id);
        PatchUtils.applyPatch(reserva, fields, objectMapper);
        resolveRelations(reserva);
        validateMesa(reserva);
        return reservaRepository.save(reserva);
    }

    public void delete(Long id) {
        Reserva reserva = findById(id);
        reservaRepository.delete(reserva);
    }

    public List<Reserva> findConfirmadasByRestauranteJpql(Long restauranteId) {
        return reservaRepository.findConfirmadasByRestauranteJpql(restauranteId);
    }

    public List<Reserva> findReservasPorDiaNative(LocalDate fecha) {
        return reservaRepository.findReservasPorDiaNative(fecha);
    }

    public ReservaResumenDTO toResumen(Reserva r) {
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

    private void resolveRelations(Reserva reserva) {
        if (reserva.getCliente() == null || reserva.getCliente().getId() == null) {
            throw new BadRequestException("La reserva debe indicar cliente.id");
        }
        if (reserva.getMesa() == null || reserva.getMesa().getId() == null) {
            throw new BadRequestException("La reserva debe indicar mesa.id");
        }
        if (reserva.getEmpleado() == null || reserva.getEmpleado().getId() == null) {
            throw new BadRequestException("La reserva debe indicar empleado.id");
        }
        reserva.setCliente(clienteRepository.findById(reserva.getCliente().getId())
            .orElseThrow(() -> new NotFoundException("No existe cliente con id " + reserva.getCliente().getId())));
        reserva.setMesa(mesaRepository.findById(reserva.getMesa().getId())
            .orElseThrow(() -> new NotFoundException("No existe mesa con id " + reserva.getMesa().getId())));
        reserva.setEmpleado(empleadoRepository.findById(reserva.getEmpleado().getId())
            .orElseThrow(() -> new NotFoundException("No existe empleado con id " + reserva.getEmpleado().getId())));
    }

    private void validateMesa(Reserva reserva) {
        if (!reserva.getMesa().isReservable()) {
            throw new BadRequestException("La mesa seleccionada no está marcada como reservable");
        }
        if (reserva.getNumeroPersonas() > reserva.getMesa().getCapacidad()) {
            throw new BadRequestException("El número de personas supera la capacidad de la mesa");
        }
    }
}
