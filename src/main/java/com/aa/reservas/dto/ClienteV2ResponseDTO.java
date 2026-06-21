package com.aa.reservas.dto;

public record ClienteV2ResponseDTO(
    Long id,
    String nombre,
    String email,
    String telefonoContacto,
    int numeroVisitas,
    String nivel,
    boolean deudaPendiente
) {}
