package com.mouad.ecommerce.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor

// NB: Ceci est déjà initialisé dans la configuration "securityWebFilterChain" pour la sécurité.
// @EnabledMethodSecurity(prePostEnabled = true) // Active la sécurité au niveau des méthodes avec @PreAuthorize et @PostAuthorize.

public class ProductController {
    private final ProductService productService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${PROJECT_NAME}")
    private String projectName;

    @Value("${my-project.mode}")
    private String mode;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        System.out.println("PROJECT_NAME: " + projectName);
        System.out.println("mode: " + mode);

        ProductResponse productResponse = productService.createProduct(request);

        // Créer une notification à envoyer
        ProductNotification notification = new ProductNotification();
        notification.setMessage("Un nouveau produit a été ajouté !");
        notification.setProduct(productResponse);

        // Envoyer la notification aux abonnés sur le topic "/topic/products/product_notif"
        // NB: "SimpMessagingTemplate" est utilisé pour envoyer des messages via WebSocket à un topic spécifique (dans notre le topic est "/topic/products/product_notif").
        messagingTemplate.convertAndSend("/topic/products/product_notif", notification);

        return ResponseEntity.ok(productResponse);
    }

    // Traiter les requêtes provenant de WebSocket sur la route "/app/products/get_product_details"
    @MessageMapping("/get_product_details")
    public void  notifyProduct(ProductRequest request) {
        // Récupérer le produit
        ProductResponse productResponse = productService.findById(request.id());

        // Créer une notification à envoyer
        ProductNotification notification = new ProductNotification();
        notification.setMessage("Voici les informations sur ce produit " + productResponse.name());
        notification.setProduct(productResponse);

        // Envoyer la notification aux abonnés sur le topic "/topic/products/product_notif"
        messagingTemplate.convertAndSend("/topic/products/product_notif", notification);
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(@RequestBody List<ProductPurchaseRequest> request) {
        return ResponseEntity.ok(productService.purchaseProducts(request));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable("product-id") Integer productId) {
        System.out.println("PROJECT_NAME: " + projectName);
        System.out.println("mode: " + mode);
        return ResponseEntity.ok(productService.findById(productId));
    }

    // @PreAuthorize("hasAutority('user')")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        System.out.println("PROJECT_NAME: " + projectName);
        System.out.println("mode: " + mode);
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/users")
    public List<UserResponse> getUsers() {
        return productService.getAllUsers();
    }
}
