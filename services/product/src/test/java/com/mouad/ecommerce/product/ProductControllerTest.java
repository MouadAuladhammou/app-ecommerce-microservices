package com.mouad.ecommerce.product;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

// NB: @ActiveProfiles("test") fonctionne bien dans "ProductControllerTest", car "ProductControllerTest" utilise @WebMvcTest qui charge un sous-ensemble du contexte Spring, généralement les contrôleurs et les composants liés au web
//   Par contre, s'il est utilisé dans "ProductServiceTest", Spring Boot charge le contexte d'application complet en utilisant les configurations spécifiques au profil "test", en ignorant les mocks initialisées dedans lors des traitements.
@ActiveProfiles("test") // Indiquer à Spring Boot d'activer le profil "test" lors de l'exécution de ces tests, même si le profil par défaut est "dev" (voir le fichier: application.yml). Cela entraînera le chargement des configurations de "application-test.yml"

// @WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class, OAuth2ResourceServerAutoConfiguration.class})
@WebMvcTest(ProductController.class) // Indique que vous testez le "ProductController". Cela configure un environnement de test limité à la couche web (les contrôleurs)

public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; // Utilisé pour simuler des requêtes HTTP

    @MockBean
    private ProductService productService; // Mock du service

    // Mock de "SimpMessagingTemplate" : Bien qu'il ne soit pas utilisé dans ces tests, mais il est nécessaire de mocker "SimpMessagingTemplate" car il est présent dans le contexte du contrôleur "ProductController"
    @MockBean
    private SimpMessagingTemplate messagingTemplate; // Mock de SimpMessagingTemplate

    @Autowired
    private ObjectMapper objectMapper; // Permet de convertir les objets en JSON

    @Value("${PROJECT_NAME}")
    private String projectName;

    @Value("${my-project.mode}")
    private String mode;

    @DisplayName("Test pour l'endpoint POST /api/v1/products")
    @Test
    void testCreateProduct() throws Exception {
        System.out.println("PROJECT_NAME: " + projectName);
        System.out.println("mode: " + mode);

        // Arrange
        ProductRequest request = new ProductRequest(
                99,
                "Product Name",
                "Product Description",
                10,
                new BigDecimal("99.99"),
                1
        );

        ProductResponse response = new ProductResponse(
                99,
                "Product Name",
                "Product Description",
                10,
                new BigDecimal("99.99"),
                1,
                "Category Name",
                "Category Description"
        );

        // Simulation du comportement du service:
        // On simule la réponse de la fonction "createProduct" dans "productService" afin de ne pas insérer les données réellement dans la base de données
        // Au lieu que ce contrôleur appelle le service réel "productService", il appelle un mock du service "ProductService" qui est annoté avec @MockBean et qui simule la méthode createProduct (en utilisant when(...)) afin d'obtenir une réponse simulée sans insérer réellement les données dans la base de données
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/products") // mockMvc.perform(...) => il simule des requêtes HTTP (comme POST, GET) vers les endpoints de contrôleur "ProductController". Les réponses sont ensuite vérifiées avec andExpect(...)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Convertit l'objet request en JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.description").value("Product Description"))
                .andExpect(jsonPath("$.availableQuantity").value(10))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryName").value("Category Name"))
                .andExpect(jsonPath("$.categoryDescription").value("Category Description"))
                .andDo(print()); // Affiche le résultat dans la console

        verify(productService).createProduct(any(ProductRequest.class)); // Vérifie que le service a été appelé
    }

    @DisplayName("Test pour l'endpoint GET /api/v1/products/{product-id}")
    @Test
    void testFindById() throws Exception {
        System.out.println("PROJECT_NAME: " + projectName);
        System.out.println("mode: " + mode);

        // Arrange
        ProductResponse response = new ProductResponse(
                99,
                "Product Name",
                "Product Description",
                10,
                new BigDecimal("99.99"),
                1,
                "Category Name",
                "Category Description"
        );

        // Simulation du comportement du service
        when(productService.findById(99)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products/{product-id}", 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(99))
                .andExpect(jsonPath("$.name").value("Product Name"))
                .andExpect(jsonPath("$.description").value("Product Description"))
                .andExpect(jsonPath("$.availableQuantity").value(10))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.categoryName").value("Category Name"))
                .andExpect(jsonPath("$.categoryDescription").value("Category Description"))
                .andDo(print());

        verify(productService).findById(99); // Vérifie que le service a été appelé
    }

    @DisplayName("Test pour l'endpoint GET /api/v1/products (findAll)")
    @Test
    void testFindAll() throws Exception {
        System.out.println("PROJECT_NAME: " + projectName);
        System.out.println("mode: " + mode);

        // Arrange
        List<ProductResponse> responses = List.of(
                new ProductResponse(99, "Product Name 1", "Product Description 1", 10, new BigDecimal("99.99"), 1, "Category Name 1", "Category Description 1"),
                new ProductResponse(999, "Product Name 2", "Product Description 2", 20, new BigDecimal("49.99"), 2, "Category Name 2", "Category Description 2")
        );

        // Simulation du comportement du service
        when(productService.findAll()).thenReturn(responses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(99))
                .andExpect(jsonPath("$[0].name").value("Product Name 1"))
                .andExpect(jsonPath("$[1].id").value(999))
                .andExpect(jsonPath("$[1].name").value("Product Name 2"))
                .andDo(print());

        verify(productService).findAll(); // Vérifie que le service a été appelé
    }

}
