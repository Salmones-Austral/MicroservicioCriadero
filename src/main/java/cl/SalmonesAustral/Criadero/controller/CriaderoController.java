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

    @Autowired                                                                   // Le dice a Spring que inyecte automáticamente las dependencias aquí
    public CriaderoController(CriaderoService criaderoService, WebClient.Builder webClientBuilder) { // Constructor del controlador
        this.criaderoService = criaderoService;                                  // Asigna el servicio de BD a la variable de esta clase
        this.webClient = webClientBuilder.build();                               // Construye y deja listo el cliente HTTP para hacer llamadas de red
    }

    // ==========================================
    // CRUD BÁSICO CON MANEJO DE ERRORES Y DTOs
    // ==========================================

    @GetMapping                                                                  // Responde a peticiones GET en la ruta base (ej: /api/criaderos)
    public ResponseEntity<List<Criadero>> listarCriaderos() {                    // Método que devuelve una lista de Criaderos
        return ResponseEntity.ok(criaderoService.findAll());                     // Busca todos en la BD y los devuelve con un HTTP 200 OK
    }

    @PostMapping                                                                 // Responde a peticiones POST (para crear)
    // CAMBIO AQUÍ: Ahora recibe CreateCriaderoRequest
    public ResponseEntity<?> crearCriadero(@Valid @RequestBody CreateCriaderoRequest request, BindingResult result) { // Recibe el JSON, lo valida (@Valid) y guarda el resultado de esa validación
        
        if (result.hasErrors()) {                                                // Pregunta: ¿El JSON venía con errores de validación?
            Map<String, String> errores = new HashMap<>();                       // Crea un diccionario vacío para guardar los errores
            result.getFieldErrors().forEach(error ->                             // Recorre cada error encontrado en los campos
                errores.put(error.getField(), error.getDefaultMessage())         // Guarda el nombre del campo malo y su mensaje de error
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);        // Devuelve la lista de errores con un HTTP 400 (Bad Request)
        }

        // MAGIA DEL MAPPER: Convierte el DTO validado al modelo de la BD
        Criadero criadero = CriaderoMapper.toModel(request);                     // Transforma el "Request" (DTO) a la entidad "Criadero" real
        Criadero nuevo = criaderoService.save(criadero);                         // Guarda la entidad en la base de datos
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);            // Devuelve el objeto creado con un HTTP 201 (Created)
    }

    @GetMapping("/{id}")                                                         // Responde a peticiones GET buscando por ID
    public ResponseEntity<Criadero> obtenerPorId(@PathVariable Integer id) {     // Atrapa el ID de la URL
        Criadero criadero = criaderoService.findById(id)                         // Busca en la BD por ese ID
                .orElseThrow(() -> new ResourceNotFoundException("Criadero no encontrado con id: " + id)); // Si no está, lanza una excepción (HTTP 404)

        return ResponseEntity.ok(criadero);                                      // Si lo encuentra, lo devuelve con HTTP 200 OK
    }

    @PutMapping("/{id}")                                                         // Responde a peticiones PUT (para actualizar un ID existente)
    // CAMBIO AQUÍ: Ahora recibe UpdateCriaderoRequest
    public ResponseEntity<?> actualizarCriadero(
            @PathVariable Integer id,                                            // Extrae el ID de la URL que queremos actualizar
            @Valid @RequestBody UpdateCriaderoRequest request, BindingResult result) { // Valida el JSON con los datos nuevos

        if (result.hasErrors()) {                                                // Revisa si la validación falló
            Map<String, String> errores = new HashMap<>();                       // Crea el diccionario de errores
            result.getFieldErrors().forEach(error ->                             // Recorre los errores
                errores.put(error.getField(), error.getDefaultMessage())         // Extrae campo y mensaje
            );
            return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);        // Frena la operación y devuelve HTTP 400
        }

        // MAGIA DEL MAPPER: Convierte el DTO validado al modelo de la BD
        Criadero criadero = CriaderoMapper.toModel(id, request);                 // Transforma el DTO nuevo y le asigna el ID existente
        Criadero actualizado = criaderoService.update(criadero);                 // Manda a actualizar el registro a la base de datos

        return ResponseEntity.ok(actualizado);                                   // Devuelve el objeto ya actualizado con HTTP 200
    }

    @DeleteMapping("/{id}")                                                      // Responde a peticiones DELETE por ID
    public ResponseEntity<Void> eliminarCriadero(@PathVariable Integer id) {     // Extrae el ID a borrar
        criaderoService.delete(id);                                              // Ejecuta el borrado en la base de datos
        return ResponseEntity.noContent().build();                               // Devuelve HTTP 204 (No Content), indicando que se borró con éxito
    }
    
    // ==========================================
    // MÉTODOS DE NEGOCIO
    // ==========================================

    @GetMapping("/total")                                                        // Ruta extra para conteos
    public ResponseEntity<Long> totalCriaderos() {                               // Retorna un número
        return ResponseEntity.ok(criaderoService.count());                       // Cuenta los registros en BD y los devuelve
    }

    @GetMapping("/activos")                                                      // Ruta para filtrar
    public ResponseEntity<List<Criadero>> obtenerEstado() {                      // Retorna lista filtrada
        return ResponseEntity.ok(criaderoService.findEstado("Activo"));          // Llama al servicio para buscar solo los "Activos"
    }

    @GetMapping("/buscar")                                                       // Ruta para buscador
    public ResponseEntity<List<Criadero>> buscarPorNombre(@RequestParam String nombre) { // Lee el parámetro "nombre" de la URL
        return ResponseEntity.ok(criaderoService.findByNombre(nombre));          // Busca coincidencias en BD y las devuelve
    }

    // ==========================================
    // RELACIÓN CON JAULAS (MICROSERVICIO)
    // ==========================================

    @GetMapping("/{id}/jaulas")                                                  // Ruta que vincula este criadero con sus jaulas
    public ResponseEntity<List<Object>> obtenerJaulasDeCriadero(@PathVariable Long id) { // Toma el ID del criadero
        List<Object> jaulas = webClient.get()                                    // Inicia petición a otro servidor
                .uri("http://localhost:8081/api/v1/jaulas/criadero/{id}", id)    // Construye la URL hacia MS Jaulas
                .retrieve()                                                      // Ejecuta la llamada HTTP
                .bodyToFlux(Object.class)                                        // Espera una lista de objetos JSON
                .collectList()                                                   // Convierte el Flux (reactivo) a una Lista Java normal
                .block();                                                        // Conexión Síncrona: Espera la respuesta antes de avanzar

        return ResponseEntity.ok(jaulas);                                        // Devuelve la lista obtenida
    }

    @GetMapping("/{id}/total-jaulas")                                            // Ruta para saber la cantidad de jaulas
    public ResponseEntity<Integer> totalJaulas(@PathVariable Long id) {          // Toma el ID
        Integer total = webClient.get()                                          // Inicia petición HTTP
                .uri("http://localhost:8081/api/v1/jaulas/criadero/{id}/total", id) // Apunta al endpoint contador de MS Jaulas
                .retrieve()                                                      // Ejecuta
                .bodyToMono(Integer.class)                                       // Espera un único dato entero (Mono)
                .block();                                                        // Conexión Síncrona: frena y espera

        return ResponseEntity.ok(total);                                         // Devuelve el número total
    }

    // ==========================================
    // COMUNICACIÓN ENTRE MICROSERVICIOS
    // ==========================================

    @GetMapping("/notificar")                                                    // Ruta para enviar mensajes
    public ResponseEntity<String> notificarJaulas(@RequestParam String mensaje) { // Toma un texto de la URL
        String respuesta = webClient.get()                                       // Inicia petición HTTP
                .uri("http://localhost:8081/api/v1/jaulas/notificar?mensaje={mensaje}", mensaje) // Envía el texto al MS Jaulas
                .retrieve()                                                      // Ejecuta
                .bodyToMono(String.class)                                        // Espera un texto de respuesta
                .block();                                                        // Espera la respuesta

        return ResponseEntity.ok(respuesta);                                     // Muestra la respuesta en pantalla
    }

    @GetMapping("/recibir")                                                      // Ruta buzón de entrada
    public ResponseEntity<String> recibirMensaje(@RequestParam String mensaje) { // Atrapa un texto que le envían
        System.out.println("📩 Mensaje recibido desde microservicio Jaulas: " + mensaje); // Lo imprime en consola (logs locales)
        return ResponseEntity.ok("Criadero recibió: " + mensaje);                // Acuse de recibo para el que lo mandó
    }
}