package cl.SalmonesAustral.Criadero.service;

import cl.SalmonesAustral.Criadero.model.Criadero;
import cl.SalmonesAustral.Criadero.repository.CriaderoRepository;
import cl.SalmonesAustral.Criadero.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class CriaderoService {
    
    // Mejor práctica: variable final e inyección por constructor
    private final CriaderoRepository criaderoRepository;

    public CriaderoService(CriaderoRepository criaderoRepository) {
        this.criaderoRepository = criaderoRepository;
    }

    public List<Criadero> findAll() {
        return criaderoRepository.findAll();
    }

    public Criadero save(Criadero criadero) {
        return criaderoRepository.save(criadero);
    }

    public Optional<Criadero> findById(Integer id) {
        return criaderoRepository.findById(id);
    }

    public Criadero update(Criadero criadero) {
        if (!criaderoRepository.existsById(criadero.getId())) {
            throw new ResourceNotFoundException("Criadero no existe con id: " + criadero.getId());
        }
        return criaderoRepository.save(criadero);
    }

    public void delete(Integer id) {
        if (!criaderoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Criadero no existe con id: " + id);
        }
        criaderoRepository.deleteById(id);
    }

    public long count() {
        return criaderoRepository.count();
    }

    // CORREGIDO: Ahora sí busca en la Base de Datos
    public List<Criadero> findEstado(String estado) {
        return criaderoRepository.findByEstado(estado);
    }

    public List<Criadero> findByNombre(String nombre) {
        return criaderoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public long totalCriaderosCustom() {
        return criaderoRepository.totalCriaderos();
    }
}