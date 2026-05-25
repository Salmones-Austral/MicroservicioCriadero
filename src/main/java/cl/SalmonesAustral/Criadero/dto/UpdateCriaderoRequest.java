package cl.SalmonesAustral.Criadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.Validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para actualizar un Criadero (PUT)
 * No incluye ID porque se obtiene del path parameter
 */
public record UpdateCriaderoRequest(

        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "La región no puede estar vacía")
        String region,

        @Positive(message = "La capacidad máxima debe ser mayor a 0")
        int capacidadMaxima,

        @NotNull(message = "El estado activo es obligatorio")
        Boolean activo

) {}
