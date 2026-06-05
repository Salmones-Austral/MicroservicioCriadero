package cl.SalmonesAustral.Criadero.mapper;

import cl.SalmonesAustral.Criadero.dto.CreateCriaderoRequest;
import cl.SalmonesAustral.Criadero.dto.UpdateCriaderoRequest;
import cl.SalmonesAustral.Criadero.model.Criadero;

public class CriaderoMapper {

    // POST
    public static Criadero toModel(CreateCriaderoRequest request) {
        return new Criadero(
                0, // ID generado por la BD
                request.getNombre(),
                request.getUbicacion(),
                request.getRegion(),
                request.getCapacidadJaulas(),
                request.getEstado(),
                request.getFechaCreacion()
        );
    }

    // PUT
    public static Criadero toModel(int id, UpdateCriaderoRequest request) {
        return new Criadero(
                id, // ID pasado por el endpoint
                request.getNombre(),
                request.getUbicacion(),
                request.getRegion(),
                request.getCapacidadJaulas(),
                request.getEstado(),
                request.getFechaCreacion()
        );
    }
}
