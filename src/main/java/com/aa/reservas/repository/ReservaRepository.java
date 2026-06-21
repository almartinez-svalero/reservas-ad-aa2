package com.aa.reservas.repository;

import com.aa.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE r.mesa.restaurante.id = :restauranteId AND r.confirmada = true")
    List<Reserva> findConfirmadasByRestauranteJpql(@Param("restauranteId") Long restauranteId);

    @Query(value = "SELECT * FROM reservas WHERE fecha_reserva = :fecha ORDER BY hora_reserva", nativeQuery = true)
    List<Reserva> findReservasPorDiaNative(@Param("fecha") LocalDate fecha);
}
