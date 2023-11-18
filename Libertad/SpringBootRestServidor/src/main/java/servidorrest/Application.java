package servidorrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import servidorrest.Application;

/**Con esta clase iniciamos la aplicacion, con la anotación @SpringBootApplication le decimos a spring
 * Que busque anotaciones de spring para dar de alta objetos e inyectar dependencias.
 * Al tratarse de un aplicacion web se autoconfigura un tomcat(servidor de aplicaciones que sirve para almacenar paginas web.
 * Buscara metodos dentro de de esta clase anotados con @Bean para dar de alta objetos en el contexto de Spring.
*/
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		System.out.println("Servicio Rest -> Cargando el contexto de Spring...");
		//Mediante el metodo "run" arrancamos el contexto de Spring  y damos de alta todos los obejtos que hayamos configurado
		//Asi como sus dependencias
		SpringApplication.run(Application.class, args);
		System.out.println("Servicio Rest -> COntexto de Spring cargado!");
	}

}
