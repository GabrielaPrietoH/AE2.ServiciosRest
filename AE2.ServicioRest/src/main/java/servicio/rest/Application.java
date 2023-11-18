package servicio.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		System.out.println("Servicio Rest -> Cargando el contexto de Spring...");
		SpringApplication.run(Application.class, args);

		System.out.println("Servicio Rest -> Contexto de Spring cargado!");
	}

}
