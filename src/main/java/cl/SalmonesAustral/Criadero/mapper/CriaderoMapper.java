package cl.SalmonesAustral.Criadero.mapper;

import cl.SalmonesAustral.Criadero.dto.CreateCriaderoRequest;
import cl.SalmonesAustral.Criadero.dto.UpdateCriaderoRequest;
import cl.SalmonesAustral.Criadero.model.Criadero;

/**
 * Mapper para convertir DTOs a modelo de dominio (Criadero)
 * Sigue separación de responsabilidades (DTO → Modelo)
 */
public class CriaderoMapper {

    /**
     * Convierte CreateCriaderoRequest a Criadero (POST)
     * El ID se genera automáticamente
     */
    public static Criadero toModel(CreateCriaderoRequest request) {
        return new Criadero(
                0L, // ID temporal (lo genera la BD)
                request.nombre(),
                request.region(),
                request.capacidadMaxima(),
                request.activo()
        );
    }

    /**
     * Convierte UpdateCriaderoRequest a Criadero (PUT)
     * El ID viene del path
     */
    public static Criadero toModel(Long id, UpdateCriaderoRequest request) {
        return new Criadero(
                id, // ID real desde el endpoint
                request.nombre(),
                request.region(),
                request.capacidadMaxima(),
                request.activo()
        );
    }
}
