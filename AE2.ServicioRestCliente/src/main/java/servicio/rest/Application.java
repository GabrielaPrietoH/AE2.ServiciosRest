package servicio.rest;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

import servicio.rest.cliente.entidad.Libro;
import servicio.rest.cliente.servicio.ServicioProxyLibro;
import servicio.rest.cliente.servicio.ServicioProxyMensaje;

@SpringBootApplication
public class Application implements CommandLineRunner{
    
	@Lazy
	@Autowired
	private ServicioProxyMensaje spm;
	
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
		SpringApplication.run(Application.class, args);
		
	}


	@Override
	public void run(String... args) throws Exception {
		
		try (Scanner sc = new Scanner(System.in)) {
			int opcion = 0;
			Libro libro = new Libro();
			
			     do {
			    System.out.println(" Menu de opciones: ");
			 	System.out.println("1. Dar de alta un libro \n2. Dar de baja un libro por ID\n3. Modificar un libro por ID \n4. Obtener un libro por ID \n5. Listar todos los libros\n6. Salir");
			    System.out.println("Elige una opción");
			    opcion = Integer.parseInt(sc.nextLine());
			    int op1 = 0;
			 
				try {
				switch(opcion){
				

				case 1: 
	
					System.out.println("-------- ALTA LIBRO ---------");
					
					System.out.println("Escribe el Título: ");
					libro.setTitulo((sc.nextLine()));
					System.out.println("Escribe la Editorial: ");
					libro.setEditorial((sc.nextLine()));
					System.out.println("Ponle una nota: ");
					libro.setNota((Double.parseDouble(sc.nextLine())));
					Libro lAlta = spl.alta(libro);
					System.out.println("run -> Libro dado de alta " +lAlta);
					
				break;
				
				case 2:
					System.out.println("-------- BORRAR PERSONAS ----------");
					System.out.println("Dime la id: ");
					op1 = Integer.parseInt(sc.nextLine());
					boolean lDelete = spl.borrar(op1);
					System.out.println("run -> Libro con id " + op1 + " borrada? " + lDelete);
					
				break;
				
				case 3:
					System.out.println("-------- MODIFICAR LIBRO ----------");
					Libro lModificar = new Libro();
					System.out.println("Esbribe la ID: ");
					op1 = Integer.parseInt(sc.nextLine());
					lModificar.setId(op1);
					System.out.println("Escribe el Título: ");
					lModificar.setTitulo((sc.nextLine()));
					System.out.println("Escribe la Editorial: ");
					lModificar.setEditorial((sc.nextLine()));
					System.out.println("Ponle una nota: ");
					lModificar.setNota((Double.parseDouble(sc.nextLine())));
					boolean modificado = spl.modificar(lModificar);
					System.out.println("run -> Se ha modificado? " + modificado);
				break;
				
				case 4:
					System.out.println("--------- GET LIBRO ---------");
					System.out.println("Dime la id: ");
					op1 = Integer.parseInt(sc.nextLine());
					if(op1 != -1) {
					Libro libro2 = spl.obtener(op1);
					System.out.println("run -> Libro con id " + op1 + " : " + libro2);
					}
					
				break;
				
				case 5:
					System.out.println("---------- LISTAR LIBROS --------");
					List<Libro> listaLibros = spl.listar(libro);
					listaLibros.forEach((v) -> System.out.println(v));
				break;
				
				case 6:
				
					pararAplicacion();
				}
					
				}catch(Exception e){
					System.out.println("Uoop! Error!");
				}
			
					
				}	while(opcion != 6);
		}
			
		}
	



	private void pararAplicacion() {
		
		System.out.println("******************************************");		
		System.out.println("******** Parando el cliente REST *********");
		SpringApplication.exit(context, () -> 0);
		
	}

}
