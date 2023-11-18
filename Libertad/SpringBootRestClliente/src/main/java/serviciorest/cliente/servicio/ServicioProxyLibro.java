package serviciorest.cliente.servicio;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import serviciorest.cliente.entidad.Libro;

//Con esta anotación damos de alta un objeto de tipo
//ServicioProxyLibro dentro del contexto de Spring
@Service
public class ServicioProxyLibro {

	//La URL base del servicio REST de libros
	public static final String URL = "http://localhost:8081/libros/";
	
	//Inyectamos el objeto de tipo RestTemplate que nos ayudará
	//a hacer las peticiones HTTP al servicio REST
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Método que obtiene un libro del servicio REST a partir de un id
	 * En caso de que el id no exita arrojaria una expcepción que se captura
	 * para sacar el codigo de respuesta
	 * 
	 * @param id que queremos obtener
	 * @return retorna el libro que estamos buscando, null en caso de que el libro no se encuentre en el servidor (devuelva 404) o haya habido algún
	 * otro error.
	 */
	public Libro obtener(int id){
		try {
			//Como el servicio trabaja con objetos ResponseEntity, nosotros 
			//tambien podemos hacerlo en el cliente
			//Ej http://localhost:8080/libros/1 GET
			ResponseEntity<Libro> re = restTemplate.getForEntity(URL + id, Libro.class);
			HttpStatusCode hs= re.getStatusCode();
			if(hs == HttpStatus.OK) {	
				//Si el libro existe, el libro viene en formato JSON en el body
				//Al ser el objeto ResponseEntity de tipo Libro, al obtener el 
				//body me lo convierte automaticamente a tipo Libro
				//(Spring utiliza librerías por debajo para pasar de JSON a objeto)
				return re.getBody();
			}else {
				System.out.println("obtener -> Respuesta no contemplada");
				return null;
			}
		}catch (HttpClientErrorException e) {//Errores 4XX
			System.out.println("obtener -> El libro NO se ha encontrado, id: " + id);
		    System.out.println("obtener -> Codigo de respuesta: " + e.getStatusCode());
		    return null;
		}
	}
	
	/**
	 * Método que da de alta un libro en el servicio REST
	 * 
	 * @param l el libro que vamos a dar de alta
	 * @return el libro con el id actualizado que se ha dado de alta en el
	 * servicio REST. Null en caso de que no se haya podido dar de alta
	 */
	public Libro alta(Libro l){
		try {
			//Para hacer un post de una entidad usamos el metodo postForEntity
			//El primer parametro la URL
			//El segundo parametros el libro que ira en body
			//El tercer parametro el objeto que esperamos que nos envie el servidor
			ResponseEntity<Libro> re = restTemplate.postForEntity(URL, l, Libro.class);
			System.out.println("alta -> Codigo de respuesta " + re.getStatusCode());
			return re.getBody();
		} catch (HttpClientErrorException e) {//Errores 4XX
			System.out.println("alta -> El libro NO se ha dado de alta, id: " + l);
		    System.out.println("alta -> Codigo de respuesta: " + e.getStatusCode());
		    return null;
		}
	}
	
	/**
	 * 
	 * Modifica un libro en el servicio REST
	 * 
	 * @param l el libro que queremos modificar, se hara a partir del 
	 * id por lo que tiene que estar relleno.
	 * @return true en caso de que se haya podido modificar el libro. 
	 * false en caso contrario.
	 */
	public boolean modificar(Libro l){
		try {
			//El metodo put de Spring no devuelve nada
			//si no da error se ha dado de alta y si no daria una 
			//excepcion
			restTemplate.put(URL + l.getId(), l, Libro.class);
			return true;
		} catch (HttpClientErrorException e) {
			System.out.println("modificar -> La persona NO se ha modificado, id: " + l.getId());
		    System.out.println("modificar -> Codigo de respuesta: " + e.getStatusCode());
		    return false;
		}
	}
	
	/**
	 * 
	 * Borra un libro en el servicio REST
	 * 
	 * @param id el id del libro que queremos borrar.
	 * @return true en caso de que se haya podido borrar el libro. 
	 * false en caso contrario.
	 */
	public boolean borrar(int id){
		try {
			//El metodo delete tampoco devuelve nada, por lo que si no 
			//ha podido borrar el id, daría un excepcion
			//Ej http://localhost:8090/libros/1 DELETE
			restTemplate.delete(URL + id);
			return true;
		} catch (HttpClientErrorException e) {
			System.out.println("borrar -> La persona NO se ha borrar, id: " + id);
		    System.out.println("borrar -> Codigo de respuesta: " + e.getStatusCode());
		    return false;
		}
	}
	
	/**
	 * Metodo que devuelve tods los libros o todos los libros filtradas
	 * por titulo del web service
	 * 
	 * @param titulo en caso de ser distinto de null, devolvera el listado
	 * filtrado por el titulo que le hayamos pasado en este parametro. En caso
	 * de que sea null, el listado de llos libros sera completo
	 * @return el listado de los libros segun el parametro de entrada o 
	 * null en caso de algun error con el servicio REST
	 */
	public List<Libro> listar(String titulo){
		String queryParams = "";		
		if(titulo != null) {
			queryParams += "?titulo=" + titulo;
		}
		
		try {
			//Ej http://localhost:80/titulo?titulo="El corazón de la piedra"GET
			ResponseEntity<Libro[]> response =
					  restTemplate.getForEntity(URL + queryParams,Libro[].class);
			Libro[] arrayLibros = response.getBody();
			return Arrays.asList(arrayLibros);//convertimos el array en un ArrayList
		} catch (HttpClientErrorException e) {
			System.out.println("listar -> Error al obtener la lista de libros");
		    System.out.println("listar -> Codigo de respuesta: " + e.getStatusCode());
		    return null;
		}
	}
}