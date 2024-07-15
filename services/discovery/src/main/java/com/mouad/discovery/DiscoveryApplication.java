package com.mouad.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

// @SpringBootApplication: Cette annotation est une combinaison de trois annotations essentielles pour une application Spring Boot : @Configuration, @EnableAutoConfiguration, et @ComponentScan.
//   @Configuration: Indique que la classe peut être utilisée par le conteneur Spring comme une source de définitions de beans.
//   @EnableAutoConfiguration: Indique à Spring Boot de commencer à ajouter des beans en fonction des dépendances ajoutées dans votre projet.
//   @ComponentScan: Indique à Spring de rechercher d'autres composants, configurations et services dans le package spécifié, permettant ainsi à Spring de trouver les contrôleurs.
@SpringBootApplication

// Active le serveur Eureka, ce qui transforme cette application en serveur Eureka pour la découverte de services.
// Grâce à l'annotation @EnableEurekaServer, Les autres microservices pourront s'enregistrer auprès de ce serveur pour être découverts par d'autres microservices.
@EnableEurekaServer
public class DiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryApplication.class, args);
	}

}
