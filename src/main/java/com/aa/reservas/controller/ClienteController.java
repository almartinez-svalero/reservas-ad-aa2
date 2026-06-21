package com.aa.reservas.controller;

import com.aa.reservas.dto.ClienteHistorialDTO;
import com.aa.reservas.model.Cliente;
import com.aa.reservas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> findAll(@RequestParam(required = false) String nombre,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) Boolean vip) {
        return clienteService.findAll(nombre, email, vip);
    }

    @GetMapping("/{id}")
    public Cliente findById(@PathVariable Long id) {
        return clienteService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Cliente> create(@Valid @RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.create(cliente));
    }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return clienteService.update(id, cliente);
    }

    @PatchMapping("/{id}")
    public Cliente patch(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        return clienteService.patch(id, fields);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jpql/vip-con-reservas")
    public List<Cliente> findVipConReservasJpql() {
        return clienteService.findClientesVipConReservasJpql();
    }

    @GetMapping("/{id}/historial")
    public ClienteHistorialDTO getHistorial(@PathVariable Long id) {
        return clienteService.getHistorial(id);
    }
}
