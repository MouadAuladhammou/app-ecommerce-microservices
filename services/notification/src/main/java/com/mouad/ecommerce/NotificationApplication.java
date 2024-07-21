package com.mouad.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication

/**
 * @EnableAsync: Active le traitement asynchrone dans l'application Spring.
 * Cela permet d'exécuter des méthodes en arrière-plan sans bloquer le thread principal.
 *
 * @Async: Marque les méthodes pour qu'elles soient exécutées de manière asynchrone.
 * Lorsqu'une méthode annotée avec @Async est appelée, elle s'exécute dans un thread séparé, permettant au ce thread appelant de continuer son exécution sans attendre la fin de la méthode asynchrone.
 * NB: Voir le fichier: app-ecommerce-microservices\services\notification\src\main\java\com\mouad\ecommerce\email\EmailService.java
 */
@EnableAsync

public class NotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationApplication.class, args);
	}

}
