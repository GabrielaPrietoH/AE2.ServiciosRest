package serviciorest.cliente;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import serviciorest.cliente.entidad.Libro;
import serviciorest.cliente.servicio.ServicioProxyLibro;
import serviciorest.cliente.servicio.ServicioProxyMensaje;

@SpringBootApplication
public class Application implements CommandLineRunner {


	@Autowired
	private ServicioProxyMensaje spm;

	@Autowired
	private ServicioProxyLibro spl;

	
	@Autowired
	private ApplicationContext context;


	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// Método main que lanza la aplicación
	public static void main(String[] args) {
		System.out.println("Cliente -> Cargando el contexto de Spring");
		SpringApplication.run(Application.class, args);

		
	}

	

	public void menu() {
		int opcion = 0;

		do {
			opcion = menuCrud();
			switch (opcion) {
			case 1:
				darAltaLibro();
				break;
			case 2:
				darBajaLibroxId();
				break;
			case 3:
				modificarLibroxId();
				break;
			case 4:
				obtenerLibroxId();
				break;
			case 5:
				listarTodosLosLibros();
				break;
			}
		} while (opcion != 6);
		System.out.println("FIN DE PROGRAMA");
		pararAplicacion();

	}// cierra void main

	public static int menuCrud() {
		Scanner leer = new Scanner(System.in);

		int opcion = 0;

		System.out.println("     ");
		System.out.println("------------------------------------------------------------------");
		System.out.println("**********MENU CRUD***********************************************");
		System.out.println("------------------------------------------------------------------");
		System.out.println("1. Dar de alta un libro");
		System.out.println("2. Dar de baja un libro por ID");
		System.out.println("3. Modificar un libro por ID");
		System.out.println("4. Obtener un libro por ID");
		System.out.println("5. Listar todos los libros");
		System.out.println("6. Salir");
		System.out.println("     ");
		System.out.print("Introduce una opcion: ");
		opcion = leer.nextInt();
		while (opcion < 1 || opcion > 6) {
			System.out.println("Introduce una opcion del 1 al 5, 6 para salir:");
			opcion = leer.nextInt();
		}

		return opcion;

	}// cierra primerMenu

	public void darAltaLibro() {
		Libro libro = new Libro();
		Scanner leer = new Scanner(System.in);
		String textoUsuario;
		int idUsuario;
		System.out.println("------------------------------------------------------------------");
		System.out.println("*********** ALTA LIBRO *******************************************");
		System.out.println("------------------------------------------------------------------");
		while (true) {
			System.out.print("Introduce Título:");
			textoUsuario = leer.nextLine();
			if (!textoUsuario.isEmpty()) {
				libro.setTitulo(textoUsuario);
				break;
			} else {
				System.out.println("El título no puede estar vacío. Por favor, inténtalo de nuevo.");
			}
		}

		while (true) {
			System.out.print("Introduce Editorial:");
			textoUsuario = leer.nextLine();
			if (!textoUsuario.isEmpty()) {
				libro.setEditorial(textoUsuario);
				break;
			} else {
				System.out.println("La editorial no puede estar vacía. Por favor, inténtalo de nuevo.");
			}
		}

		while (true) {
			System.out.print("Introduce Nota:");
			textoUsuario = leer.nextLine();
			if (!textoUsuario.isEmpty()) {
				libro.setNota(textoUsuario);
				break;
			} else {
				System.out.println("La nota no puede estar vacía. Por favor, inténtalo de nuevo.");
			}
		}

		while (true) {
			try {
				System.out.print("Introduce un ID:");
				idUsuario = leer.nextInt();
				libro.setId(idUsuario);
				break;
			} catch (NumberFormatException e) {
				System.out.println("El ID debe ser un número entero. Inténtalo de nuevo.");
			}
		}

		Libro lAlta = spl.alta(libro);
		System.out.println("run -> Libro dado de alta " + lAlta);
	}

	public void darBajaLibroxId() {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("*********** BAJA LIBRO *******************************************");
		System.out.println("------------------------------------------------------------------");

		// Solicitar al usuario el ID del libro a dar de baja
		System.out.print("Ingrese el ID del libro a dar de baja: ");
		int idLibro = scanner.nextInt();

		// Llamar al método para dar de baja el libro con el ID ingresado
		boolean borrada = spl.borrar(idLibro);

		// Mostrar el resultado
		System.out.println("Libro con ID " + idLibro + " borrado: " + borrada);
	}

	public void modificarLibroxId() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("------------------------------------------------------------------");
		System.out.println("*********** MODIFICAR LIBRO **************************************");
		System.out.println("------------------------------------------------------------------");
		// Solicitar al usuario el ID del libro a modificar
		System.out.print("Ingrese el ID del libro a modificar: ");
		int idLibro = scanner.nextInt();

		// Buscar el libro por su ID
		Libro libroAModificar = spl.obtener(idLibro);

		if (libroAModificar != null) {
			// Mostrar la información actual del libro
			System.out.println("Información actual del libro:");
			System.out.println(libroAModificar);

			// Solicitar al usuario los cambios deseados
			System.out.println("Ingrese los nuevos datos del libro:");

			System.out.print("Nuevo título: ");
			String nuevoTitulo = scanner.nextLine(); // Consumir la línea en blanco pendiente
			nuevoTitulo = scanner.nextLine(); // Leer el nuevo título

			System.out.print("Nueva editorial: ");
			String nuevaEditorial = scanner.nextLine();

			System.out.print("Nueva nota: ");
			String nuevaNota = scanner.nextLine();

			// Aplicar los cambios al libro
			libroAModificar.setTitulo(nuevoTitulo);
			libroAModificar.setEditorial(nuevaEditorial);
			libroAModificar.setNota(nuevaNota);

			// Llamar al método para modificar el libro en el sistema
			boolean modificada = spl.modificar(libroAModificar);
			System.out.println("¿Libro modificado? " + (modificada ? "Sí" : "No"));
		} else {
			System.out.println("El libro con ID " + idLibro + " no fue encontrado.");
		}
	}

	public void obtenerLibroxId() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("------------------------------------------------------------------");
		System.out.println("*********** OBTENER LIBRO ****************************************");
		System.out.println("------------------------------------------------------------------");
		System.out.print("Ingrese el ID del libro a obtener: ");
		int idLibro = scanner.nextInt();

		// Verificar si el libro existe en el sistema
		Libro libroObtenido = spl.obtener(idLibro);

		if (libroObtenido != null) {
			System.out.println("Información del libro con ID " + idLibro + ":");
			System.out.println(libroObtenido);
		} else {
			System.out.println("El libro con ID " + idLibro + " no fue encontrado.");
		}
	}

	public void listarTodosLosLibros() {
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("*********** LISTAR LIBROS ****************************************");
		System.out.println("------------------------------------------------------------------");
		
		List<Libro> listaLibros = spl.listar(null);

		// Usando un bucle for-each para imprimir cada libro en la lista
		for (Libro libro : listaLibros) {
			System.out.println(libro);
		}
	}

	public void run(String... args) throws Exception {

		menu();


	}

	public void pararAplicacion() {
		
		SpringApplication.exit(context, () -> 0);

	}
}