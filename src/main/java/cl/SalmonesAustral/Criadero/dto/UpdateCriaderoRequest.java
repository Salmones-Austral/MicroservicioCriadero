package cl.SalmonesAustral.Criadero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * DTO para actualizar un Criadero (PUT)
 * No incluye ID porque se obtiene del path parameter
 */
public record UpdateCriaderoRequest(
        /* creamos usamos todas las variables de el modelo para que el maper las llame desde el dto
        habian errores como el estado siendo null y otras que eran int como capacidad maxima
        */

        @NotBlank(message = "El nombre no puede estar vacío")
        String nombre,

        @NotBlank(message = "La ubicacion no puede estar vacía")
        String ubicacion,

        @NotBlank(message = "La región no puede estar vacía")
        String region,

        @Positive(message = "La capacidad máxima debe ser mayor a 0")
        int capacidadJaulas,

        @NotBlank(message = "El estado activo es obligatorio")
        String estado,

        @NotBlank(message = "La fecha creacion no puede estar vacía")
        String fechaCreacion

) {}
