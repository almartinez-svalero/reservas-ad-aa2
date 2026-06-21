package com.aa.reservas.controller;

import com.aa.reservas.model.Empleado;
import com.aa.reservas.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public List<Empleado> findAll(@RequestParam(required = false) Long restauranteId,
                                  @RequestParam(required = false) String puesto,
                                  @RequestParam(required = false) Boolean activo) {
        return empleadoService.findAll(restauranteId, puesto, activo);
    }

    @GetMapping("/{id}")
    public Empleado findById(@PathVariable Long id) {
        return empleadoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Empleado> create(@Valid @RequestBody Empleado empleado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.create(empleado));
    }

    @PutMapping("/{id}")
    public Empleado update(@PathVariable Long id, @Valid @RequestBody Empleado empleado) {
        return empleadoService.update(id, empleado);
    }

    @PatchMapping("/{id}")
    public Empleado patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return empleadoService.patch(id, fields);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/native/por-restaurante/{restauranteId}")
    public List<Empleado> findByRestauranteNative(@PathVariable Long restauranteId) {
        return empleadoService.findByRestauranteNative(restauranteId);
    }
}
