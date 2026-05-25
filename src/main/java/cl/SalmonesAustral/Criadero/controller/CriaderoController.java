package cl.SalmonesAustral.Criadero.controller;

import cl.SalmonesAustral.Criadero.model.Criadero;
import cl.SalmonesAustral.Criadero.service.CriaderoService;
import cl.SalmonesAustral.Criadero.exception.ResourceNotFoundException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/v1/criaderos")
public class CriaderoController {
    private final CriaderoService criaderoService;
    private final WebClient webClient;

    // Inyección por constructor (best practice)
    public CriaderoController(CriaderoService criaderoService, WebClient webClient) {
        this.criaderoService = criaderoService;
        this.webClient = webClient;
    }

    // CRUD BÁSICO
 

    @GetMapping
    public ResponseEntity<List<Criadero>> listarCriaderos() {
        return ResponseEntity.ok(criaderoService.findAll());
    }

    @PostMapping
    public ResponseEntity<Criadero> crearCriadero(@Valid @RequestBody Criadero criadero) {
        Criadero nuevo = criaderoService.save(criadero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Criadero> obtenerPorId(@PathVariable Integer id) {
        Criadero criadero = criaderoService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Criadero no encontrado con id: " + id));

        return ResponseEntity.ok(criadero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Criadero> actualizarCriadero(
            @PathVariable Integer id,
            @Valid @RequestBody Criadero criadero) {

        criadero.setId(id); // aseguramos consistencia
        Criadero actualizado = criaderoService.update(criadero);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCriadero(@PathVariable Integer id) {
        criaderoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    
    // MÉTODOS DE NEGOCIO
    

    @GetMapping("/total")
    public ResponseEntity<Long> totalCriaderos() {
        return ResponseEntity.ok(criaderoService.count());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Criadero>> obtenerActivos() {
        return ResponseEntity.ok(criaderoService.findActivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Criadero>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(criaderoService.findByNombre(nombre));
    }

    // 
    // RELACIÓN CON JAULAS (MICROSERVICIO)
    //

    @GetMapping("/{id}/jaulas")
    public ResponseEntity<List<Object>> obtenerJaulasDeCriadero(@PathVariable Long id) {

        // Llamada a microservicio de Jaulas
        List<Object> jaulas = webClient.get()
                .uri("http://localhost:8081/api/v1/jaulas/criadero/{id}", id)
                .retrieve()
                .bodyToFlux(Object.class)
                .collectList()
                .block();

        return ResponseEntity.ok(jaulas);
    }

    @GetMapping("/{id}/total-jaulas")
    public ResponseEntity<Integer> totalJaulas(@PathVariable Long id) {

        Integer total = webClient.get()
                .uri("http://localhost:8081/api/v1/jaulas/criadero/{id}/total", id)
                .retrieve()
                .bodyToMono(Integer.class)
                .block();

        return ResponseEntity.ok(total);
    }

    //
    // COMUNICACIÓN ENTRE MICROSERVICIOS
    // 

    @GetMapping("/notificar")
    public ResponseEntity<String> notificarJaulas(@RequestParam String mensaje) {

        String respuesta = webClient.get()
                .uri("http://localhost:8081/api/v1/jaulas/notificar?mensaje={mensaje}", mensaje)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/recibir")
    public ResponseEntity<String> recibirMensaje(@RequestParam String mensaje) {
        System.out.println("📩 Mensaje recibido desde microservicio Jaulas: " + mensaje);
        return ResponseEntity.ok("Criadero recibió: " + mensaje);
    }
}
