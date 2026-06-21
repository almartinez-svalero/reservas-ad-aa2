package com.aa.reservas.repository;

import com.aa.reservas.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    @Query("SELECT m FROM Mesa m WHERE m.restaurante.id = :restauranteId AND m.reservable = true AND m.capacidad >= :capacidadMinima")
    List<Mesa> findMesasDisponiblesJpql(@Param("restauranteId") Long restauranteId, @Param("capacidadMinima") int capacidadMinima);
}
