package servidorrest.modelo.persistencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.BooleanArraySerializer;

import servidorrest.modelo.entidad.Libro;

/**
 * Patron DAO (Data Access Object), objeto que se encarga de hacer las consultas
 * a algun motor de persistencia (BBDD, Ficheros, etc). En este caso vamos a
 * simular que los datos estan guardados en una BBDD trabajando con una lista de
 * objetos cargada en memoria para simplificar el ejemplo. * Mediante la
 * anotacion @Component, damos de alta un unico objeto de esta clase dentro del
 * contexto de Spring, su ID sera el nombre de la case en notacion
 * lowerCamelCase
 * 
 */
@Component
public class DaoLibro {

	public List<Libro> listaLibros;
	public int contador;

	/**
	 * Cuando se cree el objeto dentro del contexto de Spring, se ejecutara su
	 * constructor, que creara los libros y las metera en una lista para que puedan
	 * ser consumidas por nuestros usuarios
	 */
	public DaoLibro() {
		System.out.println("DaoLibro -> Creando la lista de libros!");
		listaLibros = new ArrayList<Libro>();
		Libro l1 = new Libro(contador++, "El corazón de la piedra", "Nocturna", "En la Europa de los siglos XVI");																													// 0
		Libro l2 = new Libro(contador++, "Salmos de vísperas", "Obra social de Caja de Avila", "Manuscrito musical");																														// 1
		Libro l3 = new Libro(contador++, "La música en las catedrales españolas del Siglo de Oro", "Alianza Música",
				"Biografia de Victoria");
		Libro l4 = new Libro(contador++, "La polifonía clásica", "la ciudad de Dios", "Paleografia Musical");
		Libro l5 = new Libro(contador++, "El canto gregoriano", "Publicaciones Españolas", "Ocho modos eclesiasticos");
		listaLibros.add(l1);
		listaLibros.add(l2);
		listaLibros.add(l3);
		listaLibros.add(l4);
		listaLibros.add(l5);
	}

	/**
	 * Obtiene un libro de la lista por su ID.
	 *
	 * @param id El ID del libro que se quiere buscar.
	 * @return El libro correspondiente al ID proporcionado, o null si no se
	 *         encuentra.
	 */
	public Libro get(int id) {
		try {
			// Itera sobre la lista de libros para buscar el libro por su ID
			for (Libro libro : listaLibros) {
				if (libro.getId() == id) {
					// Si el ID coincide, devuelve el libro encontrado
					return libro;
				}
			}
		} catch (IndexOutOfBoundsException iobe) {
			// Maneja una excepción si ocurre un error de índice fuera de rango
			System.out.println("Libro fuera de rango");
		}
		// Si no se encuentra el libro, retorna null
		return null;
	}

	/**
	 * Obtiene la lista completa de libros.
	 *
	 * @return La lista completa de libros.
	 */
	public List<Libro> list() {
		return listaLibros;
	}

	/**
	 * 
	 * Agrega un libro a la lista, utilizando el ID proporcionado por el usuario,
	 * asegurándose de que el ID o el titulo no esté repetido en la lista existente.
	 *
	 * @param l El libro que se intenta agregar.
	 * @return true si el libro se agrega correctamente, false si el ID o título ya
	 *         existen en la lista.
	 */
	public boolean add(Libro l) {
		for (Libro libro : listaLibros) {
			if (libro.getId() == l.getId() || libro.getTitulo().equals(l.getTitulo())) {
				// Si el ID o el título ya existen, devuelve false y no agrega el libro
				return false;
			}
		}

		// Si el ID y el título no existen, se agrega el libro a la lista
		listaLibros.add(l);
		// Devuelve true para indicar que el libro se agregó correctamente
		return true;
	}

	/**
	 * Elimina un libro de la lista por su ID.
	 *
	 * @param id El ID del libro que se quiere eliminar.
	 * @return true si el libro se elimina correctamente, false si el libro no se
	 *         encuentra en la lista.
	 */
	public Boolean delete(int id) {
		try {
			for (Libro libro : listaLibros) {
				if (libro.getId() == id) {
					// Si el ID coincide, elimina el libro de la lista y devuelve true
					return listaLibros.remove(libro);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("delete -> Libro fuera de rango");
		}
		// Si el libro no se encuentra, devuelve false
		return false;
	}

	/**
	 * Actualiza un libro en la lista por su ID con los datos proporcionados.
	 *
	 * @param l El libro con los datos actualizados que se quiere actualizar en la
	 *          lista.
	 * @return El libro actualizado si se encuentra y actualiza correctamente, o
	 *         null si el libro no se encuentra en la lista.
	 */
	public Libro update(Libro l) {
		try {
			for (Libro libro : listaLibros) {
				if (libro.getId() == l.getId()) {
					// Si el ID coincide, actualiza los datos del libro y retorna el libro
					// actualizado
					libro.setTitulo(l.getTitulo());
					libro.setEditorial(l.getEditorial());
					libro.setNota(l.getNota());

					return libro;
				}
			}
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("update -> Libro fuera de rango");
		}
		// Si el libro no se encuentra, devuelve null
		return null;
	}

	/**
	 * Filtra la lista de libros por título.
	 *
	 * @param titulo El título por el cual se quiere filtrar la lista de libros.
	 * @return Una lista de libros que coinciden con el título proporcionado.
	 */
	public List<Libro> listByTitulo(String titulo) {
		List<Libro> listaLibrosAux = new ArrayList<>();
		for (Libro l : listaLibros) {
			if (l.getTitulo().equalsIgnoreCase(titulo)) {
				// Si el título coincide (ignorando mayúsculas y minúsculas), se agrega a la
				// lista auxiliar
				listaLibrosAux.add(l);
			}
		}
		return listaLibrosAux;
	}
}