package com.mouad.ecommerce.product;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.mouad.ecommerce.category.Category;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    // NB: On peut écrire les tests dans un service sans mocker "ProductMapper", en utilisant sa logique réelle, car il n'interagit pas avec des dépendances externes comme la base de données
    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    /*
    En prenant l'exemple de "ProductService" et "ProductRepository" dans le test ci-dessous: productService.findAll();
    - @InjectMocks injecte les mocks (c'est-à-dire les objets annotés avec @Mock) dans la classe à tester "ProductService". Alors "ProductService" utilise ici les mocks de "ProductMapper" et "ProductRepository" dans sa logique de traitement
    - Sans l'annotation @Mock sur "ProductRepository", Mockito n'injectera pas de mock dans le service "ProductService", et donc, l'implémentation réelle de "ProductRepository" sera utilisée dans ce service "ProductService".
    - Sans l'annotation @InjectMocks sur "ProductService", "ProductService" utilisera l'implémentation réelle de "ProductRepository", ce qui signifie que findAll() pourrait communiquer avec la base de données réelle.
    - NB : Sans utiliser un mock dans ce code (que ce soit avec @Mock ou @InjectMocks), tu verras probablement une exception "NullPointerException".
    */

    // NB: Lorsqu'un service (et son repository) sont mocké avec Mockito, les appels aux méthodes de ce service sont simulés, de sorte que les opérations telles que l'insertion de données dans une base de données ne sont pas réellement exécutées.
    // when()   : est utilisé pour simuler le comportement (d'un méthode) d'un objet mocké. Si utilisé avec un objet non mocké, cela entraînera probablement une exception NullPointerException.
    // assert() : Vérifie les données attendues par rapport aux données réelles renvoyées par la méthode testée.
    // verify() : vérifier les interactions avec un objet mocké. Il ne retourne rien, mais vérifie que certaines méthodes ont été appelées dans votre test.
    //   Exp: verify(repository).findAll() : S'assure que findAll() a bien été appelé dans le code du service dans le test.
    //   Cela permet de tester à la fois les interactions (via verify()) et la validité des résultats (via assert).

    @DisplayName("JUnit test for create product")
    @Test
    void testCreateProduct() {
        // Création de l'objet ProductRequest
        ProductRequest request = new ProductRequest(
                99,
                "Product Name",
                "Product Description",
                10,
                new BigDecimal("99.99"),
                1
        );

        // Création d'un nouvel objet Product simulé (mocked)
        Product product = new Product();
        product.setId(1);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setAvailableQuantity(10);
        product.setPrice(new BigDecimal("99.99"));
        Category category = new Category();
        category.setId(1);
        category.setName("Category Name");
        category.setDescription("Category Description");
        product.setCategory(category);

        // Création d'un produit enregistré simulé (mocked)
        Product savedProduct = new Product();
        savedProduct.setId(1); // Simulating that this is the product saved by the repository
        savedProduct.setName("Product Name");
        savedProduct.setDescription("Product Description");
        savedProduct.setAvailableQuantity(10);
        savedProduct.setPrice(new BigDecimal("99.99"));
        savedProduct.setCategory(category);

        // Création de l'objet ProductResponse simulé (mocked)
        ProductResponse expectedResponse = new ProductResponse(
                99,
                "Product Name",
                "Product Description",
                10,
                new BigDecimal("99.99"),
                1,
                "Category Name",
                "Category Description"
        );

        // Simulation du comportement du mapper et du repository
        when(mapper.toProduct(request)).thenReturn(product);
        when(repository.save(product)).thenReturn(savedProduct);
        when(mapper.toProductResponse(savedProduct)).thenReturn(expectedResponse);

        // Action : Appel de la méthode à tester
        ProductResponse actualResponse = productService.createProduct(request);

        // Assert: Vérification que la réponse réelle correspond à la réponse attendue
        assertEquals(expectedResponse, actualResponse);

        // Vérification que les méthodes du mapper et du repository ont été appelées comme prévu
        verify(mapper).toProduct(request);
        verify(repository).save(product);
        verify(mapper).toProductResponse(savedProduct);
    }

    @DisplayName("Test JUnit pour la méthode findById - Succès")
    @Test
    void testFindById_Success() {
        // Arrangement : Préparation de l'ID du produit et des objets nécessaires
        Integer productId = 99;

        // Création d'un produit (simulé)
        Product product = new Product();
        product.setId(99);
        product.setName("Nom du Produit");
        product.setDescription("Description du Produit");
        product.setAvailableQuantity(10);
        product.setPrice(new BigDecimal("99.99"));
        Category category = new Category();
        category.setId(1);
        category.setName("Nom de la Catégorie");
        product.setCategory(category);

        // Création de la réponse attendue (ProductResponse simulé)
        ProductResponse expectedResponse = new ProductResponse(
                99,
                "Nom du Produit",
                "Description du Produit",
                10,
                new BigDecimal("99.99"),
                1,
                "Nom de la Catégorie",
                "Description de la Catégorie"
        );

        // Simulation du comportement du repository et du mapper
        when(repository.findById(productId)).thenReturn(Optional.of(product));  // Simule la recherche du produit
        when(mapper.toProductResponse(product)).thenReturn(expectedResponse);   // Simule la transformation en réponse

        // Action : Appel de la méthode findById du service
        ProductResponse actualResponse = productService.findById(productId);

        // Assert : Comparaison de la réponse réelle avec la réponse attendue
        assertEquals(expectedResponse, actualResponse);

        // Vérification que le repository et le mapper ont bien été appelés
        verify(repository).findById(productId);  // Vérifie que findById a été appelé avec le bon ID
        verify(mapper).toProductResponse(product);  // Vérifie que toProductResponse a été appelé avec le bon produit
    }

    @DisplayName("Test JUnit pour la méthode findById - Produit non trouvé")
    @Test
    void testFindById_NotFound() {
        // Arrangement : Préparation de l'ID du produit à rechercher
        Integer productId = 99;

        // Simulation du comportement du repository : retour d'un Optional vide pour simuler un produit non trouvé
        when(repository.findById(productId)).thenReturn(Optional.empty());

        // Action et Vérification : Test que l'appel à findById lève une "EntityNotFoundException"
        assertThrows(EntityNotFoundException.class, () -> productService.findById(productId));

        // Vérification que la méthode findById du repository a bien été appelée avec le bon ID
        verify(repository).findById(productId);

        // Vérification qu'aucune interaction supplémentaire n'a eu lieu avec le mapper, car le produit n'existe pas
        verifyNoMoreInteractions(mapper);
    }


    @DisplayName("Test JUnit pour la méthode findAll")
    @Test
    void testFindAll() {
        // Arrangement : Préparation d'une liste de produits et des réponses attendues
        Category category = new Category();
        category.setId(1);
        category.setName("Category Name");
        category.setDescription("Category Description");
        List<Product> products = Arrays.asList(
                new Product(99, "Produit 1", "Description Produit 1", 10, new BigDecimal("99.99"), category),
                new Product(999, "Produit 2", "Description Produit 2", 5, new BigDecimal("49.99"), category)
        );

        // Réponses attendues pour chaque produit
        List<ProductResponse> expectedResponses = Arrays.asList(
                new ProductResponse(99, "Produit 1", "Description Produit 1", 10, new BigDecimal("99.99"), 1, "Category Name", "Category Description"),
                new ProductResponse(999, "Produit 2", "Description Produit 2", 5, new BigDecimal("49.99"), 1, "Category Name", "Category Description")
                // new ProductResponse(9999, "Produit 2", "Description Produit 2", 5, new BigDecimal("49.99"), 1, "Category Name", "Category Description")
        );

        // Simulation du comportement du repository et du mapper
        when(repository.findAll()).thenReturn(products);
        when(mapper.toProductResponse(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            // Retourne la "ProductResponse" simulée
            return new ProductResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getAvailableQuantity(),
                    product.getPrice(),
                    product.getCategory().getId(),
                    product.getCategory().getName(),
                    product.getCategory().getDescription()
            );
        });

        // Action : Appel de la méthode findAll du service
        List<ProductResponse> actualResponses = productService.findAll();

        // Assert : Comparaison du nombre de réponses attendues et obtenues
        assertEquals(expectedResponses.size(), actualResponses.size());

        // Vérification que la méthode findAll du repository a bien été appelée
        verify(repository).findAll();

        // Vérification que la méthode toProductResponse du mapper a été appelée pour chaque produit
        verify(mapper, times(products.size())).toProductResponse(any(Product.class));
    }

}
