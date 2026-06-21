package com.aa.reservas.dto;

public record RestauranteResumenDTO(
    Long restauranteId,
    String nombre,
    int totalMesas,
    int totalEmpleados,
    long totalReservasConfirmadas,
    int capacidadTotalMesas
) {}
