package com.aa.reservas.controller;

import com.aa.reservas.model.Mesa;
import com.aa.reservas.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {
    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @GetMapping
    public List<Mesa> findAll(@RequestParam(required = false) Long restauranteId,
                              @RequestParam(required = false) Integer capacidadMinima,
                              @RequestParam(required = false) Boolean reservable) {
        return mesaService.findAll(restauranteId, capacidadMinima, reservable);
    }

    @GetMapping("/{id}")
    public Mesa findById(@PathVariable Long id) {
        return mesaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Mesa> create(@Valid @RequestBody Mesa mesa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mesaService.create(mesa));
    }

    @PutMapping("/{id}")
    public Mesa update(@PathVariable Long id, @Valid @RequestBody Mesa mesa) {
        return mesaService.update(id, mesa);
    }

    @PatchMapping("/{id}")
    public Mesa patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return mesaService.patch(id, fields);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        mesaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jpql/disponibles")
    public List<Mesa> findDisponiblesJpql(@RequestParam Long restauranteId, @RequestParam(defaultValue = "1") int capacidadMinima) {
        return mesaService.findMesasDisponiblesJpql(restauranteId, capacidadMinima);
    }
}
