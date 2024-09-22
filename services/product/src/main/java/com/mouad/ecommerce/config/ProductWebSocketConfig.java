package com.mouad.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker // Active le support de messagerie WebSocket
public class ProductWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Configurer le broker de messages :
        //   Le broker de messages gère les messages sortants envoyés via WebSocket aux clients abonnés.
        //   Ici, on configure le broker (WebSocket) pour les destinations commençant par "/topic/products".
        config.enableSimpleBroker("/topic/products");

        // Configurer les destinations des messages entrants :
        //   Traiter tous les messages entrants dont la destination commence par "/app/products" que les clients (front-end) envoient au serveur via WebSocket.
        //   Exemple : Si un client envoie un message à la destination /app/products/get_product_details, il sera routé vers une méthode dans un contrôleur annotée avec @MessageMapping("/get_product_details")
        config.setApplicationDestinationPrefixes("/app/products");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Définir le point de terminaison pour les WebSockets
        registry.addEndpoint("/ws/product")
                .setAllowedOriginPatterns("*") // Autorise les requêtes cross-origin (CORS)
                .withSockJS(); // Optionnel : Active le support de SockJS pour les clients qui ne supportent pas WebSocket.
    }

}
