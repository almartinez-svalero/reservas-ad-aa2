package com.aa.reservas.dto;

public record AuthResponseDTO(
    String tokenType,
    String accessToken,
    long expiresInMinutes
) {}
