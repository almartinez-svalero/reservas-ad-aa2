package com.aa.reservas.repository;

import com.aa.reservas.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    @Query(value = "SELECT * FROM empleados WHERE restaurante_id = :restauranteId ORDER BY nombre", nativeQuery = true)
    List<Empleado> findByRestauranteNative(@Param("restauranteId") Long restauranteId);
}
