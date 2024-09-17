package com.mouad.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity // Active la sécurité WebFlux dans l'application Spring, ce qui est nécessaire pour configurer la sécurité des applications réactives.
public class SecurityConfig {

    // Ce code configure la sécurité de l'application Spring WebFlux en désactivant CSRF,
    //   en permettant l'accès non authentifié à certaines routes (/eureka/**),
    //   en exigeant l'authentification pour toutes les autres requêtes,
    //   et en utilisant des tokens JWT pour l'authentification OAuth2.
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Désactive la protection CSRF (Cross-Site Request Forgery)
                .authorizeExchange(exchange -> exchange // Configure les règles d'autorisation pour les échanges (requêtes) HTTP.
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/eureka/**").permitAll() // Permet l'accès non authentifié à toutes les requêtes dont le chemin commence par "/eureka/".
                        .anyExchange().authenticated() // Exige l'authentification pour toutes les autres requêtes. Toute autre route non spécifiée explicitement nécessite que l'utilisateur soit authentifié pour y accéder.
                ).oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // Configure l'application pour utiliser OAuth2 Resource Server avec JWT (JSON Web Tokens) pour l'authentification. Cela signifie que l'application va vérifier et valider les tokens JWT dans les requêtes entrantes.
        return serverHttpSecurity.build(); // Construit et retourne l'objet "SecurityWebFilterChain" configuré, qui est utilisé par Spring Security pour gérer la sécurité des requêtes HTTP entrantes.
    }

}
