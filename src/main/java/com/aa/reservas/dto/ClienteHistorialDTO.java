package com.aa.reservas.dto;

import java.util.List;

public record ClienteHistorialDTO(
    Long clienteId,
    String nombreCliente,
    String email,
    int totalReservas,
    List<ReservaResumenDTO> reservas
) {}
