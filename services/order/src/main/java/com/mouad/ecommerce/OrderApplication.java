package com.mouad.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // L'audit JPA permet de suivre et d'enregistrer automatiquement des informations sur les entités, telles que la date de création, la date de dernière modification (@CreatedDate et @LastModifiedDate)

// Active les clients Feign
@EnableFeignClients
// L'annotation @EnableFeignClients est utilisée pour activer les clients Feign dans une application Spring Boot.
//   Feign est une bibliothèque Java pour simplifier les appels HTTP à des services web. Elle permet de définir les clients HTTP de manière déclarative en utilisant des interfaces Java.
//   Quand vous utilisez @EnableFeignClients, Spring Boot va scanner votre projet à la recherche des interfaces annotées avec @FeignClient. (situé: app-ecommerce-microservices\services\order\src\main\java\com\mouad\ecommerce\customer\CustomerClient.java)
//     Ces interfaces sont ensuite implémentées automatiquement par Feign pour que vous puissiez faire des appels HTTP sans avoir à écrire du code HTTP spécifique (comme RestTemplate ou HttpClient).

// Voici un exemple complet et détaillé pour mieux comprendre comment et quand l'appel HTTP est fait en utilisant Feign:

// ******************** Structure d'application ******************** :
// 1 - Classe principale de l'application (OrderApplication.java) :
//     Cette classe démarre l'application (microservice "order-service") et active le client Feign.

// 4 - Contrôleur (OrderController.java).
//     Ce contrôleur expose une API REST pour créer une nouvelle commande(order).

// 3 - Service (OrderService.java).
//     Ce service utilise le client Feign pour vérifier l'existence d'un client(customer) avant de créer une commande(order). (la methode: createOrder())

// 2 - Client Feign (CustomerClient.java) pour appeler un service externe (customer-service).
//     Ce client Feign (interface CustomerClient) est utilisé pour faire des appels HTTP vers le microservice "customer-service".

// 3 - Contrôleur dans microservice "customer-service" (CustomerController.java).
//     Ce contrôleur expose une API REST pour récupérer les informations d'un client(customer).

// 5 - Fichier application.yml :
// 	   application:
//       config:
//		   customer-url: http://localhost:8222/api/v1/customers



// ******************** Quand et comment l'appel HTTP est-il effectué vers l'autre microservice "customer-service" ********************
// L'appel HTTP est effectué lorsque le contrôleur (OrderController.java) reçoit une requête POST à l'URL /api/v1/orders pour créer une nouvelle commande(order).
// Voici le flux détaillé :

// 1 - Requête HTTP entrante :
//     Une requête HTTP POST est envoyée à l'URL /api/v1/orders pour créer une nouvelle commande(order).

// 2 - Contrôleur (OrderController.java) :
//     La méthode "createOrder" de "OrderController" est appelée avec un objet "OrderRequest" comme paramètre.
//     Cette méthode appelle le service "OrderService" pour executer la méthode "createOrder(request)".

// 3 - Service (OrderService.java) :
//     OrderService utilise le client Feign (Interface CustomerClient.java) pour appeler la méthode "findCustomerById" avec l'ID du client(customer) extrait de paramètre "OrderRequest".
//     donc, customerClient.findCustomerById(request.customerId()) construit une requête HTTP GET à l'URL http://localhost:8222/api/v1/customers/{customer-id} (en remplaçant {customer-id} par l'ID du client(customer)).
//       CustomerClient fait l'appel HTTP GET vers d'autre microservice externe "customer-service" pour vérifier l'existence du client(customer).
//       Si le client(customer) est valide, la logique pour créer la commande(order) se poursuit dans "OrderService" (la methode createOrder()).



@SpringBootApplication
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
