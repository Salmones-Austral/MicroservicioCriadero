package cl.SalmonesAustral.Criadero.controller;

import cl.SalmonesAustral.Criadero.model.Criadero;
import cl.SalmonesAustral.Criadero.service.CriaderoService;
import cl.SalmonesAustral.Criadero.exception.ResourceNotFoundException;

// IMPORTS NUEVOS PARA LOS DTOs Y EL MAPPER
import cl.SalmonesAustral.Criadero.dto.CreateCriaderoRequest;
import cl.SalmonesAustral.Criadero.dto.UpdateCriaderoRequest;
import cl.SalmonesAustral.Criadero.mapper.CriaderoMapper;

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

    @Autowired
    public CriaderoController(CriaderoService criaderoService, WebClient.Builder webClientBuilder) {
        this.criaderoService = criaderoService;
        this.webClient = webClientBuilder.build(); 
    }

    // ==========================================
    // CRUD BÁSICO CON MANEJO DE ERRORES Y DTOs
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Criadero>> listarCriaderos() {
        return ResponseEntity.ok(criaderoService.findAll());
    }

    @PostMapping
    // CAMBIO AQUÍ: Ahora recibe CreateCriaderoRequest
    public ResponseEntity<?> crearCriadero(@Valid @RequestBody CreateCriaderoRequest request, BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        // MAGIA DEL MAPPER: Convierte el DTO validado al modelo de la BD
        Criadero criadero = CriaderoMapper.toModel(request);
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
    // CAMBIO AQUÍ: Ahora recibe UpdateCriaderoRequest
    public ResponseEntity<?> actualizarCriadero(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateCriaderoRequest request, BindingResult result) {

        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errores.put(error.getField(), error.getDefaultMessage())
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
        }

        // MAGIA DEL MAPPER: Convierte el DTO validado al modelo de la BD
        Criadero criadero = CriaderoMapper.toModel(id, request);
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