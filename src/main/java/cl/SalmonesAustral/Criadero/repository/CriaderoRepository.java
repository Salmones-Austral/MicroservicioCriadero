package cl.SalmonesAustral.Criadero.repository;

import cl.SalmonesAustral.Criadero.model.Criadero;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CriaderoRepository extends JpaRepository<Criadero, Integer> {

    // DESCOMENTADO: Lo necesitamos para el negocio
    List<Criadero> findByEstado(String estado);

    List<Criadero> findByNombreContainingIgnoreCase(String nombre);
    List<Criadero> findByRegion(String region);

    default long totalCriaderos() {
        return this.count();
    }

    @Query("SELECT c FROM Criadero c WHERE c.capacidadJaulas > :capacidad")
    List<Criadero> findCriaderosConAltaCapacidad(@Param("capacidad") int capacidad);

    @Query(value = "SELECT * FROM criaderos WHERE region = :region", nativeQuery = true)
    List<Criadero> selectPorRegion(@Param("region") String region);
}
