package es.cristian.cliente.servicio;

import java.util.Arrays; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.*;

import es.cristian.cliente.entidad.Libro;

@Service
public class ServicioProxyLibro {

	public static final String URL = "http://localhost:8081/libros/";
	
	@Autowired
	private RestTemplate restTemplate;
	
	//Métodos implementados en el método run de Application, implementado
	//desde CommandLineRunner
	
	//Método para dar de alta
	public Libro alta(Libro l) {
		try {
			ResponseEntity<Libro> re = restTemplate.postForEntity(URL, l, Libro.class);
			System.out.println("alta -> Codigo de respuesta " + re.getStatusCode());
			return re.getBody();
		} catch (HttpClientErrorException e) {
			System.out.println("Alta: No se ha dado de alta el libro " + l);
			System.err.println("Alta: código de respuesta es: " + e.getStatusCode());
			return null;
		}
		
	}
	
	//Método para dar de baja un libro
	public boolean borrar(int id) {
		try {
			restTemplate.delete(URL + id);
			return true;
		} catch (HttpClientErrorException e) {
			System.out.println("Borrar: No se ha eliminado el libro con id: " + id);
			System.out.println("Borrar: código de respuesta: " + e.getStatusCode());
			return false;
		}
	}
	
	
	//Método para modificar un libro
	public boolean modificar(Libro l) {
		try {
			restTemplate.put(URL + l.getId(), l, Libro.class);
			return true;
		} catch (HttpClientErrorException e) {
			System.out.println("Modificar: no se ha modificado el libro con id: " + l.getId());
			System.out.println("Modificar: código de respuesta: " + e.getStatusCode());
			return false;
		}
	}
	
	//Método para Obtener un libro por ID
	/*
	 * 
	 */
	public Libro obtener(int id) {
		try {
		ResponseEntity<Libro> re = restTemplate.getForEntity(URL + id, Libro.class);
		HttpStatus hs = (HttpStatus) re.getStatusCode(); //Me obliga el IDE  hacer este casting.
		if(hs == HttpStatus.OK) {
			return re.getBody();
		}else {
			System.out.println("obtener: respeusta no efectuada");
			return null;
		}
		}catch(HttpClientErrorException e) {
			System.out.println("obtener: el libro no se ha encontrado, con id " + id);
			System.out.println("obtener: código de respuessta: " + e.getStatusCode());
			return null;
		}
	}
	
	//Método para listar todos los libros o por título
	public List<Libro> listar(String titulo){
		String queryParams = "";
		if(titulo != null) {
			queryParams += "?titulo=" + titulo;
		}
		
		try {
			ResponseEntity<Libro[]> response =
					restTemplate.getForEntity(URL + queryParams, Libro[].class);
			Libro[] arrayLibros = response.getBody();
			return Arrays.asList(arrayLibros);
			
		}catch(HttpClientErrorException e) {
			System.out.println("Listar: error al obtener el listado de libros");
			System.out.println("lISTAR: código de respuesta: " + e.getStatusCode());
			return null;
		}
	}
	
	
	
	
	
}
