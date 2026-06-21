package com.aa.reservas.controller;

import com.aa.reservas.dto.ReservaCreateDTO;
import com.aa.reservas.dto.ReservaResumenDTO;
import com.aa.reservas.model.Reserva;
import com.aa.reservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public List<Reserva> findAll(@RequestParam(required = false) Long clienteId,
                                 @RequestParam(required = false) LocalDate fechaReserva,
                                 @RequestParam(required = false) Boolean confirmada) {
        return reservaService.findAll(clienteId, fechaReserva, confirmada);
    }

    @GetMapping("/{id}")
    public Reserva findById(@PathVariable Long id) {
        return reservaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Reserva> create(@Valid @RequestBody Reserva reserva) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.create(reserva));
    }

    @PostMapping("/asignar")
    public ResponseEntity<ReservaResumenDTO> createFromDto(@Valid @RequestBody ReservaCreateDTO dto) {
        Reserva reserva = reservaService.createFromDto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.toResumen(reserva));
    }

    @PutMapping("/{id}")
    public Reserva update(@PathVariable Long id, @Valid @RequestBody Reserva reserva) {
        return reservaService.update(id, reserva);
    }

    @PatchMapping("/{id}")
    public Reserva patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return reservaService.patch(id, fields);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jpql/confirmadas-por-restaurante/{restauranteId}")
    public List<Reserva> findConfirmadasByRestauranteJpql(@PathVariable Long restauranteId) {
        return reservaService.findConfirmadasByRestauranteJpql(restauranteId);
    }

    @GetMapping("/native/por-dia")
    public List<Reserva> findReservasPorDiaNative(@RequestParam LocalDate fecha) {
        return reservaService.findReservasPorDiaNative(fecha);
    }
}
