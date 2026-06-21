package com.aa.reservas.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReservaCreateDTO(
    @NotBlank String codigo,
    @NotNull LocalDate fechaReserva,
    @NotNull LocalTime horaReserva,
    @Min(1) int numeroPersonas,
    @DecimalMin("0.0") float importeSenal,
    boolean confirmada,
    @Size(max = 250) String observaciones,
    @NotNull Long clienteId,
    @NotNull Long mesaId,
    @NotNull Long empleadoId
) {}
