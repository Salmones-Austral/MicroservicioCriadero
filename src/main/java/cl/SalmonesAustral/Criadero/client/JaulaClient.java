package cl.SalmonesAustral.Criadero.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class JaulaClient {

    private static final Logger logger = LoggerFactory.getLogger(JaulaClient.class);
    private final WebClient webClient;

    public JaulaClient(WebClient.Builder webClientBuilder) {
        // En producción, usa el nombre registrado en Eureka, ej: "http://servicio-jaula"
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build(); 
    }

    public boolean tieneJaulasActivas(Long criaderoId) {
        logger.info("Llamando a microservicio jaula para verificar criadero ID: {}", criaderoId);
        
        return webClient.get()
                .uri("/api/v1/jaulas/criadero/{id}/activas", criaderoId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> {
                    // Cumple IE 2.3.1: Manejo de excepciones en flujos remotos
                    logger.error("Error al comunicarse con el microservicio de Jaula: {}", e.getMessage());
                    return Mono.just(false); 
                })
                .block(); // Bloqueo temporal sincrónico 
    }
}
