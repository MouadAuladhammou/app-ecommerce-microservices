package com.mouad.ecommerce.product;

import com.mouad.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    private static final String EXTERNAL_API_URL = "https://jsonplaceholder.typicode.com/users";

    @Autowired
    private RestTemplate restTemplate;

    @CachePut(value = "products", key = "#result.id")
    @CacheEvict(value = "allProducts", allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        var savedProduct = repository.save(product);
        return mapper.toProductResponse(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse findById(Integer id) {
        return repository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID:: " + id));
    }

    // NB: Ordre actuel (Cache avant Circuit Breaker): ce qui réduit le nombre d'appels au circuit breaker.
    //     en revancehe, Peut masquer des problèmes dans la méthode réelle, car certains appels ne sont pas surveillés par le circuit breaker
    // @Cacheable(value = "allProducts")
    // Ici on utilise un CircuitBreaker avec une fonction interne
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallbackGetData")
    public List<ProductResponse> findAll() {
        // Forcer une exception pour déclencher le Circuit Breaker
        // throw new RuntimeException("Erreur simulée pour tester le Circuit Breaker");

        return repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> fallbackGetData(Throwable throwable) {
        ProductResponse fallbackProduct = new ProductResponse(
                null, // id
                null, // name
                "Service temporairement indisponible. Veuillez réessayer plus tard", // description
                0.0, // availableQuantity
                BigDecimal.ZERO, // price
                null, // categoryId
                null, // categoryName
                null  // categoryDescription
        );
        return Collections.singletonList(fallbackProduct);
    }

    // Ici on utilise un CircuitBreaker avec une API externe
    @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallbackGetUsers")
    public List<UserResponse> getAllUsers() {
        UserResponse[] users = restTemplate.getForObject(EXTERNAL_API_URL, UserResponse[].class);
        return users != null ? Arrays.asList(users) : Collections.emptyList();
    }
    public List<UserResponse> fallbackGetUsers(Throwable throwable) {
        UserResponse fallbackUser = new UserResponse();
        fallbackUser.setId(null);
        fallbackUser.setName("Service temporairement indisponible. Veuillez réessayer plus tard.");
        return Collections.singletonList(fallbackUser);
    }

    @Caching(
        evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "allProducts", allEntries = true)
        }
    )
    public void delete(Long id) {
        // Logique pour supprimer l'entité
    }

    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        var storedProducts = repository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }
        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();
        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);
            if (product.getAvailableQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID:: " + productRequest.productId());
            }
            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            purchasedProducts.add(mapper.toproductPurchaseResponse(product, productRequest.quantity()));
        }
        return purchasedProducts;
    }
}
