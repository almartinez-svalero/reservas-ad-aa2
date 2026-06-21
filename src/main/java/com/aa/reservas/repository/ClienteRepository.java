package com.aa.reservas.repository;

import com.aa.reservas.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT DISTINCT c FROM Reserva r JOIN r.cliente c WHERE c.vip = true")
    List<Cliente> findClientesVipConReservasJpql();
}
