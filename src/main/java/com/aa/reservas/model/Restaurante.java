package com.aa.reservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "restaurantes")
public class Restaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 180)
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+ ]{9,15}$", message = "El teléfono debe tener un formato válido")
    private String telefono;

    @Min(value = 1, message = "El aforo máximo debe ser mayor que 0")
    private int aforoMaximo;

    @DecimalMin(value = "0.0", message = "La valoración no puede ser negativa")
    @DecimalMax(value = "5.0", message = "La valoración no puede superar 5")
    private float valoracionMedia;

    private boolean activo;

    @PastOrPresent(message = "La fecha de apertura no puede estar en el futuro")
    private LocalDate fechaApertura;

    public Restaurante() {}

    public Restaurante(Long id, String nombre, String direccion, String telefono, int aforoMaximo, float valoracionMedia, boolean activo, LocalDate fechaApertura) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.aforoMaximo = aforoMaximo;
        this.valoracionMedia = valoracionMedia;
        this.activo = activo;
        this.fechaApertura = fechaApertura;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public int getAforoMaximo() { return aforoMaximo; }
    public void setAforoMaximo(int aforoMaximo) { this.aforoMaximo = aforoMaximo; }
    public float getValoracionMedia() { return valoracionMedia; }
    public void setValoracionMedia(float valoracionMedia) { this.valoracionMedia = valoracionMedia; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }
}
