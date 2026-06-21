package com.aa.reservas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "mesas")
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "El número de mesa debe ser mayor que 0")
    private int numero;

    @Min(value = 1, message = "La capacidad debe ser mayor que 0")
    private int capacidad;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 60)
    private String ubicacion;

    private boolean reservable;

    @DecimalMin(value = "0.0", message = "El suplemento no puede ser negativo")
    private float suplementoTerraza;

    @PastOrPresent(message = "La fecha de alta no puede estar en el futuro")
    private LocalDate fechaAlta;

    @NotNull(message = "El restaurante es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Restaurante restaurante;

    public Mesa() {}

    public Mesa(Long id, int numero, int capacidad, String ubicacion, boolean reservable, float suplementoTerraza, LocalDate fechaAlta, Restaurante restaurante) {
        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.ubicacion = ubicacion;
        this.reservable = reservable;
        this.suplementoTerraza = suplementoTerraza;
        this.fechaAlta = fechaAlta;
        this.restaurante = restaurante;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public boolean isReservable() { return reservable; }
    public void setReservable(boolean reservable) { this.reservable = reservable; }
    public float getSuplementoTerraza() { return suplementoTerraza; }
    public void setSuplementoTerraza(float suplementoTerraza) { this.suplementoTerraza = suplementoTerraza; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
}
