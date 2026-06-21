package com.aa.reservas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "El puesto es obligatorio")
    @Size(max = 80)
    private String puesto;

    @DecimalMin(value = "0.0", message = "El sueldo por hora no puede ser negativo")
    private float sueldoHora;

    private boolean activo;

    @PastOrPresent(message = "La fecha de contratación no puede estar en el futuro")
    private LocalDate fechaContratacion;

    @NotNull(message = "El restaurante es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Restaurante restaurante;

    public Empleado() {}

    public Empleado(Long id, String nombre, String email, String puesto, float sueldoHora, boolean activo, LocalDate fechaContratacion, Restaurante restaurante) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.puesto = puesto;
        this.sueldoHora = sueldoHora;
        this.activo = activo;
        this.fechaContratacion = fechaContratacion;
        this.restaurante = restaurante;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public float getSueldoHora() { return sueldoHora; }
    public void setSueldoHora(float sueldoHora) { this.sueldoHora = sueldoHora; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    public Restaurante getRestaurante() { return restaurante; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }
}
