package com.aa.reservas.controller;

import com.aa.reservas.dto.RestauranteResumenDTO;
import com.aa.reservas.model.Restaurante;
import com.aa.reservas.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @GetMapping
    public List<Restaurante> findAll(@RequestParam(required = false) String nombre,
                                      @RequestParam(required = false) Boolean activo,
                                      @RequestParam(required = false) Integer aforoMinimo) {
        return restauranteService.findAll(nombre, activo, aforoMinimo);
    }

    @GetMapping("/{id}")
    public Restaurante findById(@PathVariable Long id) {
        return restauranteService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Restaurante> create(@Valid @RequestBody Restaurante restaurante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteService.create(restaurante));
    }

    @PutMapping("/{id}")
    public Restaurante update(@PathVariable Long id, @Valid @RequestBody Restaurante restaurante) {
        return restauranteService.update(id, restaurante);
    }

    @PatchMapping("/{id}")
    public Restaurante patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return restauranteService.patch(id, fields);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restauranteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/native/activos")
    public List<Restaurante> findActivosNative() {
        return restauranteService.findRestaurantesActivosNative();
    }

    @GetMapping("/{id}/resumen")
    public RestauranteResumenDTO getResumen(@PathVariable Long id) {
        return restauranteService.getResumen(id);
    }
}
