package serviciorest.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import serviciorest.cliente.entidad.Libro;
import serviciorest.cliente.servicio.ServicioProxyMensaje;
import serviciorest.cliente.servicio.ServicioProxyLibro;

@SpringBootApplication
public class Application implements CommandLineRunner{

	//Primero inyectaremos todos los objetos que necesitamos para
	//acceder a nuestro ServicioRest, el ServicioProxyPersona y el
	//ServicioProxyMensaje
	
	@Autowired
	private ServicioProxyMensaje spm;
	
	@Autowired
	private ServicioProxyLibro spl;
	
	//También necesitaremos acceder al contexto de Spring para parar
	//la aplicación, ya que esta app al ser una aplicación web se
	//lanzará en un Tomcat. De esta manera le decimos a Spring que
	//nos inyecte su propio contexto.
	@Autowired
	private ApplicationContext context;
	
	//En este método daremos de alta un objeto de tipo RestTemplate que será
	//el objeto más importante de esta aplicación. Será usado por los 
	//objetos ServicioProxy para hacer las peticiones HTTP a nuestro
	//servicio REST. 
	//Como no podemos anotar la clase RestTemplate para dar un objeto
	//de este tipo, porque no la hemos creado nosotros, usaremos la anotación 
	//@Bean para decirle a Spring que cuando arranque la app ejecute este 
	//método y meta el objeto devuelto dentro del contexto de Spring con ID 
	//"restTemplate" (el nombre del método)
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	//Método main que lanza la aplicación
	public static void main(String[] args) {
		System.out.println("Cliente -> Cargando el contexto de Spring");
		SpringApplication.run(Application.class, args);

		//Nótese que como este método es estático no podemos acceder
		//a los métodos dinámicos de la clase, como el "spp" o "spm"
		//Para solucionar esto, haremos que nuestra clase implemente
		//"CommandLineRunner" e implementaremos el método "run"
		//Cuando se acabe de arrancar el contexto, se llamará automáticamente
		//al método run
	}
	
	//Este método es dinámico por la tanto ya podemos acceder a los atributos
	//dinámicos (spm y spp respectivamente)
	@Override
	public void run(String... args) throws Exception {
		System.out.println("****** Arrancando el cliente REST ******");
		System.out.println("*************  MENSAJE *****************");
		String mensaje = spm.obtener("mensaje");
		System.out.println("run -> Mensaje: " + mensaje);
		
		System.out.println("***********  MENSAJE HTML **************");
		String mensajeHTML = spm.obtener("mensajeHTML");
		System.out.println("run -> Mensaje: " + mensajeHTML);
		
		System.out.println("*********** ALTA PERSONA ***************");
		Libro libro = new Libro();
		libro.setTitulo("La catedral del mar");
		libro.setEditorial("Fuji");
		libro.setNota("Barcelona edad media");
		
		Libro lAlta = spl.alta(libro);
		System.out.println("run -> Persona dada de alta " + lAlta);
		
		System.out.println("************ GET LIBRO ***************");
		libro = spl.obtener(lAlta.getId());
		System.out.println("run -> Libro con id 5: " + libro);
		
		System.out.println("************ GET LIBRO ERRONEA ***************");
		libro = spl.obtener(20);
		System.out.println("run -> Libro con id 20: " + libro);
		
		System.out.println("********* MODIFICAR LIBRO *************");	
		Libro lModificar = new Libro();
		lModificar.setId(lAlta.getId());
		lModificar.setTitulo("Los Pilares de la tierra");
		lModificar.setEditorial("Planeta de libros");
		lModificar.setNota("Las catedrales de Kindsbridge");
		boolean modificada = spl.modificar(lModificar);
		System.out.println("run -> persona modificada? " + modificada);
		
		System.out.println("********* MODIFICAR LIBRO ERRONEA*************");			
		lModificar.setTitulo("Forastera");
		lModificar.setEditorial("Libros Madrid");
		lModificar.setId(20);
		modificada = spl.modificar(lModificar);
		System.out.println("run -> persona modificada? " + modificada);
		
		System.out.println("********** BORRAR LIBROS **************");
		boolean borrada = spl.borrar(lAlta.getId());
		System.out.println("run -> Persona con id 5 borrada? " + borrada);	
		
		System.out.println("******** BORRAR LIBROS ERRONEA *******");
		borrada = spl.borrar(20);
		System.out.println("run -> Libro con id 20 borrado? " + borrada);		
		
		System.out.println("********** LISTAR LIBROS ***************");
		List<Libro> listaLibros = spl.listar(null);
		//Recorremos la lista y la imprimimos con funciones lambda
		//Tambien podríamos haber usado un for-each clásico de java
		listaLibros.forEach((v) -> System.out.println(v));

		System.out.println("******* LISTAR LIBROS POR TITULO *******");
		listaLibros = spl.listar("");
		listaLibros.forEach((v) -> System.out.println(v));
		
		System.out.println("******************************************");		
		System.out.println("******** Parando el cliente REST *********");	
		//Mandamos parar nuestra aplicación Spring Boot
		pararAplicacion();
	}
	
	public void pararAplicacion() {
		//Esta aplicacion levanta un servidor web, por lo que tenemos que dar 
		//la orden de pararlo cuando acabemos. Para ello usamos el método exit, 
		//de la clase SpringApplication, que necesita tanto el contexto de 
		//Spring como un objeto que implemente la interfaz ExitCodeGenerator. 
		//Podemos usar la función lambda "() -> 0" para simplificar 
		
		SpringApplication.exit(context, () -> 0);
		
		//Podemos hacerlo también de una manera clásica, es decir, creando
		//la clase anónima a partir de la interfaz. 
		//El código de abajo sería equivalente al de arriba
		//(pero mucho más largo)
		/*
		SpringApplication.exit(context, new ExitCodeGenerator() {
			
			@Override
			public int getExitCode() {
				return 0;
			}
		});*/
	}
}