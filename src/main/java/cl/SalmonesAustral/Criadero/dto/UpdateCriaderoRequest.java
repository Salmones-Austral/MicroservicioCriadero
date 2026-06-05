package cl.SalmonesAustral.Criadero.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateCriaderoRequest {

    @NotBlank(message = "El nombre del criadero no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotNull(message = "La capacidad de jaulas es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser de al menos 1 jaula")
    private Integer capacidadJaulas;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotBlank(message = "La fecha de creación es obligatoria")
    private String fechaCreacion;

    // Getters
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getRegion() { return region; }
    public Integer getCapacidadJaulas() { return capacidadJaulas; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setRegion(String region) { this.region = region; }
    public void setCapacidadJaulas(Integer capacidadJaulas) { this.capacidadJaulas = capacidadJaulas; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
