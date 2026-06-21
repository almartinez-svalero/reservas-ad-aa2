package com.aa.reservas.repository;

import com.aa.reservas.model.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    @Query(value = "SELECT * FROM restaurantes WHERE activo = true ORDER BY nombre", nativeQuery = true)
    List<Restaurante> findRestaurantesActivosNative();
}
