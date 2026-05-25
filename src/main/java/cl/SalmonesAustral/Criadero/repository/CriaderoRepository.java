package cl.SalmonesAustral.Criadero.repository;

import cl.SalmonesAustral.Criadero.model.Criadero;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CriaderoRepository extends JpaRepository<Criadero, Integer> {

    
    // MÉTODOS DERIVADOS (SPRING DATA)
   

    // Criaderos activos
    List<Criadero> findByActivoTrue();

    // Buscar por nombre (contiene, ignore case)
    List<Criadero> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por región (útil en negocio salmonero)
    List<Criadero> findByRegion(String region);


    // MÉTODOS PERSONALIZADOS
   

    // Igual que tu ejemplo (default method)
    default long totalCriaderos() {
        return this.count();
    }

    // Query JPQL
    @Query("SELECT c FROM Criadero c WHERE c.capacidadMaxima > :capacidad")
    List<Criadero> findCriaderosConAltaCapacidad(@Param("capacidad") int capacidad);

    // Query nativa (como tu ejemplo de editorial)
    @Query(value = "SELECT * FROM criaderos WHERE region = :region", nativeQuery = true)
    List<Criadero> selectPorRegion(@Param("region") String region);

}
