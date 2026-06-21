package com.aa.reservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+ ]{9,15}$", message = "El teléfono debe tener un formato válido")
    private String telefono;

    @Min(value = 0, message = "El número de visitas no puede ser negativo")
    private int numeroVisitas;

    @DecimalMin(value = "0.0", message = "El saldo pendiente no puede ser negativo")
    private float saldoPendiente;

    private boolean vip;

    @PastOrPresent(message = "La fecha de registro no puede estar en el futuro")
    private LocalDate fechaRegistro;

    public Cliente() {}

    public Cliente(Long id, String nombre, String email, String telefono, int numeroVisitas, float saldoPendiente, boolean vip, LocalDate fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.numeroVisitas = numeroVisitas;
        this.saldoPendiente = saldoPendiente;
        this.vip = vip;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public int getNumeroVisitas() { return numeroVisitas; }
    public void setNumeroVisitas(int numeroVisitas) { this.numeroVisitas = numeroVisitas; }
    public float getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(float saldoPendiente) { this.saldoPendiente = saldoPendiente; }
    public boolean isVip() { return vip; }
    public void setVip(boolean vip) { this.vip = vip; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
