package com.mouad.ecommerce.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

// Cela sert uniquement à charger les données de la table "product" en utilisant notre service. (NB : assurez-vous d'avoir déjà inséré manuellement les catégories appropriées)
// Spring Boot ici détectera automatiquement les implémentations de "CommandLineRunner" et les exécutera après l'initialisation du contexte de l'application.
// les produits spécifiés seront insérés dans la base de données lors du démarrage de l'application.

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // Créer et insérer des données de test
        ProductRequest product1 = new ProductRequest(
                null,
                "Product 1",
                "Description 1",
                10.0,
                BigDecimal.valueOf(100.0),
                1
        );

        ProductRequest product2 = new ProductRequest(
                null,
                "Product 2",
                "Description 2",
                20.0, BigDecimal.valueOf(200),
                2
        );

        productService.createProduct(product1);
        productService.createProduct(product2);
    }
}
