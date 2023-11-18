package es.cristian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ae2CristianApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ae2CristianApplication.class, args);
		System.out.println("Servicio Rest -> Contexto de Spring cargado!");
	}	

}
