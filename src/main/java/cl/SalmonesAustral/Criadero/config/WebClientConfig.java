package cl.SalmonesAustral.Criadero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {return WebClient.builder();}
    // Cliente para microservicio JAULAS
  
    @Bean
    public WebClient jaulasWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8081/api/v1/jaulas")
                .build();
    }


    // Cliente externo (ejemplo tipo PokeAPI)
   
 
}
