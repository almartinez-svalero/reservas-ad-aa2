package com.aa.reservas.controller;

import com.aa.reservas.dto.ClienteV2RequestDTO;
import com.aa.reservas.dto.ClienteV2ResponseDTO;
import com.aa.reservas.dto.DeleteResponseDTO;
import com.aa.reservas.model.Cliente;
import com.aa.reservas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v2/clientes")
public class ClienteV2Controller {
    private final ClienteService clienteService;

    public ClienteV2Controller(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteV2ResponseDTO> findAll(@RequestParam(required = false) String nombre,
                                              @RequestParam(required = false) String email,
                                              @RequestParam(required = false) Boolean vip) {
        return clienteService.findAll(nombre, email, vip).stream()
            .map(this::toResponse)
            .toList();
    }

    @PostMapping
    public ResponseEntity<ClienteV2ResponseDTO> create(@Valid @RequestBody ClienteV2RequestDTO dto) {
        Cliente cliente = clienteService.create(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ClienteV2ResponseDTO update(@PathVariable Long id, @Valid @RequestBody ClienteV2RequestDTO dto) {
        return toResponse(clienteService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public DeleteResponseDTO delete(@PathVariable Long id) {
        clienteService.delete(id);
        return new DeleteResponseDTO(id, "cliente", "Cliente eliminado correctamente en API v2");
    }

    private Cliente toEntity(ClienteV2RequestDTO dto) {
        return new Cliente(
            null,
            dto.nombre(),
            dto.email(),
            dto.telefono(),
            dto.numeroVisitas(),
            dto.saldoPendiente(),
            dto.vip(),
            dto.fechaRegistro() != null ? dto.fechaRegistro() : LocalDate.now()
        );
    }

    private ClienteV2ResponseDTO toResponse(Cliente cliente) {
        return new ClienteV2ResponseDTO(
            cliente.getId(),
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTelefono(),
            cliente.getNumeroVisitas(),
            cliente.isVip() ? "VIP" : "ESTANDAR",
            cliente.getSaldoPendiente() > 0
        );
    }
}
