package es.cristian.cliente;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

import org.springframework.context.ApplicationContext;


import es.cristian.cliente.entidad.Libro;
import es.cristian.cliente.servicio.ServicioProxyLibro;

@SpringBootApplication
public class Ae2CristianClienteApplication implements CommandLineRunner{
	
	@Lazy 
	@Autowired
	private ServicioProxyLibro spl;
	
	
	@Autowired
	private ApplicationContext context;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		System.out.println("Cliente -> Cargando el contexto de Spring");

		SpringApplication.run(Ae2CristianClienteApplication.class, args);
	}

		@Override
		public void run(String... args) throws Exception {
			
			Scanner  sc = new Scanner(System.in);
			
			System.out.println("------------ Arrancando el cliente REST -------");
			
			
			
			String texto = "";
			boolean continuar = true;
			do {
			System.out.println("------------ MENÚ -----------------------------");
			System.out.println("Elige una de las siguientes opciones:");
			System.out.println("1. Dar de alta un libro por ID.");
			System.out.println("2. Dar de baja un libro por ID.");
			System.out.println("3. Modificar un libro por ID.");
			System.out.println("4. Obtener un libro por ID.");
			System.out.println("5. Listar todos los libros.");
			System.out.println("6. Salir del programa.");
			
			texto = sc.nextLine();
			
			if(texto.equals("6")) {
				System.out.println("Has finalizado el programa.");
				continuar = false;
			}else if(texto.equals("1")){
				
				//Dar de alta un libro
				System.out.println("Alta de un nuevo libro: ");
				Libro libro = new Libro();
				System.out.println("Introduce el título del libro: ");
				String titulo = sc.nextLine();
				libro.setTitulo(titulo);
				System.out.println("Introduce la editorial del libro: ");
				String editorial = sc.nextLine();
				libro.setEditorial(editorial);
				
				do {
					System.out.println("Introduce una nota para el libro: ");
				try {
					double nota = sc.nextDouble();
					libro.setNota(nota);
					break;
				} catch (InputMismatchException  e) {
					System.out.println("La entrada no es un número válido, vuelve a intentarlo");				}
					sc.nextLine(); //limpia y descarta la entrada errónea, limpiando el buffer y permitiendo una nueva entrada.
				}while(true);
				
				Libro lAlta = spl.alta(libro);
				System.out.println("run: el libro dado de alta es: " + lAlta);
			}else if(texto.equals("2")) {
				//Dar de baja un libro por ID
				System.out.println("Vas a eliminar un libro del listado, elige uno según su id");
				List<Libro> listadoLibros = spl.listar(null);
				
				
				for(Libro libro: listadoLibros) {
					 System.out.println("ID: " + libro.getId() + ", Título: " + libro.getTitulo());
				}
				System.out.println("¿Cuál es tu elección?");
				String eleccion = sc.nextLine();
				
				//try { se puede añadir un try por si introducen un ID que no existe.
					int idLibro = Integer.parseInt(eleccion);
					boolean existe = false;
					for(Libro libro: listadoLibros){
						if(idLibro == libro.getId()) {
							boolean borrada = spl.borrar(idLibro);
				            System.out.println("run: El libro con ID " + idLibro + " ha sido eliminado: " + borrada);
				            existe = true;
				            break;
					}
					if(!existe) {
						System.out.println("El ID introducido no exsite");
					}
					
				}
			}else if(texto.equals("3")) {
				//Modificar un libro por ID
				
				System.out.println("Vas a modificar un libro del listado, elige uno según su id");
				List<Libro> listadoLibros = spl.listar(null);
				
				
				for(Libro libro: listadoLibros) {
					 System.out.println("ID: " + libro.getId() + ", Título: " + libro.getTitulo());
				}
				System.out.println("¿Cuál es tu elección?");
				String eleccion = sc.nextLine();
				
				try {
					int idLibro = Integer.parseInt(eleccion);
					boolean existe = false;
					for(Libro libro: listadoLibros){
						if(libro.getId() == idLibro) {
							existe = true;
							
							libro = spl.obtener(idLibro);
							System.out.println("Introduce un nuevo título del libro: ");
							libro.setTitulo(sc.nextLine());
							System.out.println("Introduce la editorial del libro: ");
							libro.setEditorial(sc.nextLine());
							System.out.println("Introduce una nota para el libro: ");
							libro.setNota(Double.parseDouble(sc.nextLine()));
							
							boolean modificada = spl.modificar(libro);
							System.out.println("run: libro modificado: " + modificada);
							break;
							
						}
					if(!existe) {
						System.out.println("No se encontró ningún libro con este ID");
					}
				}}catch(NumberFormatException e) {
					System.out.println("Introduce un ID válido, por favor.");
						
				
				}
			}else if(texto.equals("4")) {

				//Obtener un libro por ID
				List<Libro> listadoLibros = spl.listar(null);
				
				System.out.println("Vas a modificar un libro del listado, elige uno según su id");

				for(Libro libro: listadoLibros) {
					 System.out.println("ID: " + libro.getId() + ", Título: " + libro.getTitulo());
				}
				System.out.println("¿Cuál es tu elección?");
				String eleccion = sc.nextLine();
				
				try {
					int idLibro = Integer.parseInt(eleccion);
					boolean existe = false;
					for(Libro libros: listadoLibros) {
						if(libros.getId() == idLibro) {
							existe = true;
							break;
						}
					}
					if(existe) {
						Libro libroObtener = spl.obtener(idLibro);
						System.out.println("El libro obtenido es: " + libroObtener);
					}else {
						System.out.println("no se enocntró ningún libro con este id");
					}
				}catch(NumberFormatException e) {
					System.out.println("Introduce un ID válido, por favor.");
				}
			}else if(texto.equals("5")) {
				//Listar todos los libros
				List<Libro> listaLibros = spl.listar(null);
				System.out.println("Vas a listar todos los libros");
				listaLibros.forEach(libro -> System.out.println(libro));
				
			}
				
			
			
			
			
			
			
			
			
		}while(continuar);
			
	pararAplicacion();

}
		public void pararAplicacion() {
			int exitCode = SpringApplication.exit(context, () -> 0);
			System.exit(exitCode);
			/*
			 * int exitCode = SpringApplication.exit(context, new ExitCodeGenerator() {
				
				@Override
				public int getExitCode() {
					return 0;
				}
			});
			 */
			
			
			
		}
}
