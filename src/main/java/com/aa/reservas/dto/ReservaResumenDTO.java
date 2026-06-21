package com.aa.reservas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaResumenDTO(
    Long id,
    String codigo,
    LocalDate fechaReserva,
    LocalTime horaReserva,
    int numeroPersonas,
    boolean confirmada,
    String nombreCliente,
    int numeroMesa,
    String empleadoResponsable
) {}
