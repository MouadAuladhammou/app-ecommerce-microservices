package com.mouad.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

// @SpringBootApplication: Cette annotation est une combinaison de trois annotations essentielles pour une application Spring Boot : @Configuration, @EnableAutoConfiguration, et @ComponentScan.
//   @Configuration: Indique que la classe peut être utilisée par le conteneur Spring comme une source de définitions de beans.
//   @EnableAutoConfiguration: Indique à Spring Boot de commencer à ajouter des beans en fonction des dépendances ajoutées dans votre projet.
//   @ComponentScan: Indique à Spring de rechercher d'autres composants, configurations et services dans le package spécifié, permettant ainsi à Spring de trouver les contrôleurs.
@SpringBootApplication

// @EnableConfigServer: Cette annotation active le serveur de configuration Spring Cloud. (Celui qui se charge de fournir les configurations aux microservices lors de leurs démarrages)
//   Elle transforme l'application en un serveur de configuration qui permet de centraliser la gestion des configurations des différentes applications microservices.
@EnableConfigServer

public class ConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerApplication.class, args);
	}

}
