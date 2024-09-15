package com.mouad.ecommerce.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

// on va créer une interface annotée avec @FeignClient. Cette interface définira les méthodes pour interagir avec un autre service web.
// NB : la classe pour FeignClient doit toujours être de type "interface".
@FeignClient(
    name = "customer-service",
    url = "${application.config.customer-url}"
)
public interface CustomerClient {
    // Cela doit être le même lien que celui dans le contrôleur "CustomerController" => app-ecommerce-microservices\services\customer\src\main\java\com\mouad\ecommerce\customer\CustomerController.java
    @GetMapping("/{customer-id}")
    Optional<CustomerResponse> findCustomerById(@PathVariable("customer-id") String customerId);

}
