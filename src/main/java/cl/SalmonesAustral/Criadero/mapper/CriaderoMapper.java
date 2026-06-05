package cl.SalmonesAustral.Criadero.mapper;

import cl.SalmonesAustral.Criadero.dto.CreateCriaderoRequest;
import cl.SalmonesAustral.Criadero.dto.UpdateCriaderoRequest;
import cl.SalmonesAustral.Criadero.model.Criadero;

public class CriaderoMapper {

    // POST
    public static Criadero toModel(CreateCriaderoRequest request) {
        return new Criadero(
                null, // ❌ ANTES DECÍA 0. AHORA ES null para que haga el INSERT (Nuevo registro)
                request.getNombre(),
                request.getUbicacion(),
                request.getRegion(),
                request.getCapacidadJaulas(),
                request.getEstado(),
                request.getFechaCreacion()
        );
    }

    // PUT
    public static Criadero toModel(Integer id, UpdateCriaderoRequest request) { // <-- Cambiado int a Integer
        return new Criadero(
                id, // ID pasado por el endpoint para hacer el UPDATE
                request.getNombre(),
                request.getUbicacion(),
                request.getRegion(),
                request.getCapacidadJaulas(),
                request.getEstado(),
                request.getFechaCreacion()
        );
    }
}
