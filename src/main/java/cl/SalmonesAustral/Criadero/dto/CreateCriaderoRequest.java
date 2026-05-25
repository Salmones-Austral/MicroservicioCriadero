package cl.SalmonesAustral.Criadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para crear un nuevo Criadero (POST)
 * No incluye ID porque se genera automáticamente
 */
public record CreateCriaderoRequest(

        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "La región no puede estar vacía")
        String region,

        @Positive(message = "La capacidad máxima debe ser mayor a 0")
        int capacidadMaxima,

        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo

) {}
