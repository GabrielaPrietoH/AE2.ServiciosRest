package servidorrest.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import servidorrest.modelo.entidad.Libro;
import servidorrest.modelo.persistencia.DaoLibro;

/**
 * En este ejemplo vamos a realizar un CRUD completo contra la entidad Libro.
 */

@RestController
public class ControladorLibro {

	// La anotacion @Autowired lo usamos para hacer inyecciones de dependencias de
	// objetos dados de alta dentro del contexto de Spring.
	// Cuando se cree este objeto (ControladorLibro) dentro del contexto de Spring,
	// mediante esta anotacion le diremos que inyecte un objeto
	// de tipo DaoLibro a la referencia "daoLibro", por lo que el objeto Controlador
	// libro quedará perfectamente formado.

	@Autowired
	private DaoLibro daoLibro;

	/**
	 * Obtiene un libro específico según su ID. Este método utiliza @GetMapping para
	 * mapear las peticiones GET a la URL /libros/{id}, donde {id} es un marcador de
	 * posición para el ID del libro que se está solicitando. Utiliza @PathVariable
	 * para obtener ese id de la URL y realizar una búsqueda en una supuesta base de
	 * datos o almacén (daoLibro.get(id)). Si encuentra el libro, devuelve una
	 * respuesta con el libro y un estado 200 OK. En caso contrario, si el libro no
	 * existe, devuelve un estado 404 NOT FOUND. El resultado se envía como un
	 * objeto ResponseEntity que contiene el libro y el estado correspondiente de la
	 * respuesta HTTP. La URL para acceder a este metodo sería:
	 * "http://localhost:8080/personas/ID"
	 * 
	 * @param id El ID del libro a buscar.
	 * @return ResponseEntity con el libro correspondiente si se encuentra, o un
	 *         HttpStatus.NOT_FOUND si no se encuentra.
	 */
	@GetMapping(path = "libros/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Libro> getLibro(@PathVariable("id") int id) {
		System.out.println("Buscando Libro con id: " + id);

		// Busca el libro en la base de datos o almacén de datos
		Libro l = daoLibro.get(id);

		if (l != null) {
			// Si se encuentra el libro, devuelve una respuesta con el libro y estado 200 OK
			return new ResponseEntity<>(l, HttpStatus.OK);
		} else {
			// Si no se encuentra el libro, devuelve un estado 404 NOT FOUND
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Método que maneja las peticiones POST para agregar un nuevo libro. Esta
	 * documentación describe el propósito del método, qué tipo de petición maneja
	 * (POST), el patrón de URL que espera (/libros), el cuerpo que espera (Libro en
	 * formato JSON), y explica los posibles resultados (devolver el libro y un
	 * estado CREATED si se agrega correctamente o devolver un estado BAD REQUEST si
	 * no se puede agregar). La URL para acceder a este metodo sería:
	 * //"http://localhost:8090/personas" y el metodo a usar seria POST
	 * 
	 * @param l El objeto Libro que se va a agregar a la base de datos.
	 * @return ResponseEntity con el libro agregado y un estado 201 CREATED si se
	 *         agrega correctamente, o un estado 400 BAD REQUEST si no se puede
	 *         agregar.
	 */
	@PostMapping(path = "libros", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Libro> altaLibro(@RequestBody Libro l) {
		// Intenta agregar el libro a la base de datos y guarda el resultado
		boolean agregado = daoLibro.add(l);

		// Verifica si el libro se agregó correctamente
		if (agregado) {
			// Si se agregó correctamente, devuelve el libro y un estado 201 CREATED
			return new ResponseEntity<>(l, HttpStatus.CREATED);
		} else {
			// Si no se puede agregar el libro, devuelve un estado 400 BAD REQUEST
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Método que maneja peticiones GET para listar libros. También indica que puede
	 * filtrar la lista de libros según el título proporcionado como parámetro
	 * opcional en la URL. Si no se proporciona un título, devuelve la lista
	 * completa de libros. Retorna una lista de libros con un estado 200 OK si se
	 * encuentra algún resultado, o una lista vacía si no hay resultados. La URL
	 * para acceder a este metodo en caso de querer todas los libros sería:
	 * "http://localhost:8080/personas" y el metodo a usar seria GET
	 * 
	 * @param titulo (Opcional) El título por el cual se desea filtrar la lista de
	 *               libros.
	 * @return ResponseEntity con la lista de libros correspondiente, con estado 200
	 *         OK si se encuentra algún resultado, o una lista vacía si no hay
	 *         resultados.
	 */
	@GetMapping(path = "libros", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Libro>> listarLibros(@RequestParam(name = "titulo", required = false) String titulo) {
		List<Libro> listarLibros = null;

		// Si no se proporciona un título, devuelve la lista completa de libros
		if (titulo == null) {
			listarLibros = daoLibro.list();
		} else {
			// Si se proporciona un título, filtra la lista por ese título
			listarLibros = daoLibro.listByTitulo(titulo);
		}

		// Retorna la lista de libros encontrada, con un estado 200 OK
		return new ResponseEntity<>(listarLibros, HttpStatus.OK);
	}

	/**
	 * Método que maneja las peticiones PUT para modificar un libro existente.
	 * método maneja peticiones PUT para modificar un libro existente. Recibe el id
	 * del libro en la URL y un objeto Libro con los nuevos datos a actualizar.
	 * Intenta actualizar el libro con los nuevos datos proporcionados y retorna un
	 * estado 200 OK si la modificación se realiza correctamente, o un estado 404
	 * NOT FOUND si el libro no se encuentra en la base de datos. La URL para
	 * acceder a este metodo sería:"http://localhost:8080/personas/ID" y el metodo a
	 * usar seria PUT
	 * 
	 * @param id El ID del libro que se va a modificar.
	 * @param l  El objeto Libro con los nuevos datos para actualizar.
	 * @return ResponseEntity con estado 200 OK si la modificación se realiza
	 *         correctamente, o estado 404 NOT FOUND si el libro no se encuentra.
	 */
	@PutMapping(path = "libros/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Libro> modificarLibro(@PathVariable("id") int id, @RequestBody Libro l) {
		System.out.println("ID a modificar: " + id);
		System.out.println("Datos a modificar: " + l);

		// Establece el ID del libro con el valor proporcionado en la URL
		l.setId(id);

		// Intenta actualizar el libro con los nuevos datos
		Libro lUpdate = daoLibro.update(l);

		// Verifica si el libro se actualizó correctamente
		if (lUpdate != null) {
			// Si se actualizó correctamente, devuelve un estado 200 OK
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			// Si el libro no se encuentra, devuelve un estado 404 NOT FOUND
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Método que maneja las peticiones DELETE para eliminar un libro por su ID.
	 * Recibe el id del libro en la URL y procede a intentar eliminar el libro
	 * correspondiente. Retorna un estado 200 OK si el libro se elimina
	 * correctamente o un estado 404 NOT FOUND si el libro no se encuentra en la
	 * base de datos. La URL para acceder a este metodo
	 * sería:"http://localhost:8080/libros/ID" y el metodo a usar seria DELETE
	 * 
	 * @param id El ID del libro que se va a eliminar.
	 * @return ResponseEntity con estado 200 OK si el libro se elimina
	 *         correctamente, o estado 404 NOT FOUND si el libro no se encuentra.
	 */
	@DeleteMapping(path = "libros/{id}")
	public ResponseEntity<Libro> borrarLibro(@PathVariable("id") int id) {
		System.out.println("ID a borrar: " + id);

		// Intenta eliminar el libro con el ID proporcionado
		Boolean lDelete = daoLibro.delete(id);

		// Verifica si el libro se eliminó correctamente
		if (lDelete) {
			// Si se elimina correctamente, devuelve un estado 200 OK
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			// Si el libro no se encuentra, devuelve un estado 404 NOT FOUND
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
