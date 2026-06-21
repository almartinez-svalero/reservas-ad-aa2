package com.aa.reservas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClienteV2RequestDTO(
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    String nombre,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    String email,

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[0-9+ ]{9,15}$", message = "El telefono debe tener un formato valido")
    String telefono,

    @Min(value = 0, message = "El numero de visitas no puede ser negativo")
    int numeroVisitas,

    @DecimalMin(value = "0.0", message = "El saldo pendiente no puede ser negativo")
    float saldoPendiente,

    boolean vip,

    @PastOrPresent(message = "La fecha de registro no puede estar en el futuro")
    LocalDate fechaRegistro
) {}
