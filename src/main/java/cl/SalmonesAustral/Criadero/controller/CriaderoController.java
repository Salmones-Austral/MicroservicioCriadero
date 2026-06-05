package cl.SalmonesAustral.Criadero.controller;

import cl.SalmonesAustral.Criadero.model.Criadero;
import cl.SalmonesAustral.Criadero.service.CriaderoService;
import cl.SalmonesAustral.Criadero.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/criaderos")
public class CriaderoController {

    private final CriaderoService criaderoService;
    private final WebClient webClient;

    // Inyección por constructor (Best Practice - Solucionado para evitar NullPointerException)
    @Autowired
    public CriaderoController(CriaderoService criaderoService, WebClient.Builder webClientBuilder) {
        this.criaderoService = criaderoService;
        // Es mejor inyectar el Builder y construirlo aquí o en una clase @Configuration
        this.webClient = webClientBuilder.build(); 
    }

    // ==========================================
    // CRUD BÁSICO CON MANEJO DE ERRORES
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Criadero>> listarCriaderos() {
        return ResponseEntity.ok(criaderoService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> crearCriadero(@Valid @RequestBody Criadero criadero, BindingResult result) {
        // Manejo de errores de validación capturados desde el RequestBody
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

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
    public ResponseEntity<?> actualizarCriadero(
            @PathVariable Integer id,
            @Valid @RequestBody Criadero criadero, BindingResult result) {

        // Manejo de errores de validación en la actualización
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        criadero.setId(id); // aseguramos consistencia
        Criadero actualizado = criaderoService.update(criadero);

        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCriadero(@PathVariable Integer id) {
        criaderoService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    @GetMapping("/total")
    public ResponseEntity<Long> totalCriaderos() {
        return ResponseEntity.ok(criaderoService.count());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Criadero>> obtenerEstado() {
        // Asumo que findEstado recibe un String, puedes ajustarlo según tu Service
        return ResponseEntity.ok(criaderoService.findEstado("Activo")); 
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Criadero>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(criaderoService.findByNombre(nombre));
    }

    // ==========================================
    // RELACIÓN CON JAULAS (MICROSERVICIO)
    // ==========================================

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

    // ==========================================
    // COMUNICACIÓN ENTRE MICROSERVICIOS
    // ==========================================

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