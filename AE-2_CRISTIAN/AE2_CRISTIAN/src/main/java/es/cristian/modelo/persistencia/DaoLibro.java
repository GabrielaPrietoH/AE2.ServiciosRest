package es.cristian.modelo.persistencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import es.cristian.modelo.entidad.Libro;

/*Clase DaoLibro, 
 * 
 * Aquí se implementa la lógica de la entidad Libro.
 * @Component doy de alta un objeto de tipo DaoPersona dentro del contexto de Spring. 
 */
@Component	
public class DaoLibro {
	
	public List<Libro> listaLibros;
	public int contador;
	
	public DaoLibro() {
		
		System.out.println("DaoLibros: creando la lsita de libros almacenada");
		
		
		listaLibros = new ArrayList<Libro>();
		Libro l1 = new Libro(contador++, "El código Da Vinci","Planeta", 10);
		Libro l2 = new Libro(contador++, "Dragonlance","Alfaguara", 9);
		Libro l3 = new Libro(contador++, "Viaje al centro de la tierra","Akal", 10);
		Libro l4 = new Libro(contador++, "Canción de hielo y fuego","Santillana", 8);
		Libro l5 = new Libro(contador++, "Un mundo Feliz","Anagrama", 9);
		listaLibros.add(l1);
		listaLibros.add(l2);
		listaLibros.add(l3);
		listaLibros.add(l4);
		listaLibros.add(l5);
	}
		
		//Métodos
		
		/*
		 * Método get, devuelve un libro a partir de la posición en el array especificada
		 * @param posicion la posicion del arrya que buscamos
		 * @return la persona que ocupe en la posicion del array, null en caso de
		 * que no exista o se haya ido fuera de rango del array
		 */
		public Libro get(int posicion) {
			try {
				return listaLibros.get(posicion);
			} catch (IndexOutOfBoundsException iobe) {
				System.out.println("Libro fuera de rango");
				return null;
			}
		}
		/*
		 * Método que devuelve la lsita de libros.
		 * @return una lista con todas las personas del array
		 */
		public List<Libro> list(){
			return listaLibros;
		}
		/*
		 * Método para añadir un libro. 
		 * @param p la persona que queremos introducir
		
		 */
		/*
		 * public boolean add(Libro l) {
			
			for (Libro libro : listaLibros) {
				if (libro.getId() == l.getId() || libro.getTitulo().equals(l.getTitulo())) {
					// Si el ID o el título ya existen, devuelve false y no agrega el libro
					
				}
			}
			l.setId(contador++);
			listaLibros.add(l);
		}
		 */
				
		public boolean add(Libro l) {
			for (Libro libro : listaLibros) {
				if (libro.getId() == l.getId() || libro.getTitulo().equals(l.getTitulo())) {
					// Si el ID o el título ya existen, devuelve false y no agrega el libro
					return false;
				}
			}

			// Si el ID y el título no existen, se agrega el libro a la lista
			l.setId(contador++);
			listaLibros.add(l);
			// Devuelve true para indicar que el libro se agregó correctamente
			return true;
		}
		
		
		
		/*
		 * Método para elminar un libro de la lista.  @param posicion la posicion a borrar
		 * @return devolvemos la persona que hemos quitado del array, 
		 * o null en caso de que no exista.
		 */
		
		public Libro delete(int posicion) {
			try {
				return listaLibros.remove(posicion);
			} catch (IndexOutOfBoundsException  e) {
				System.out.println("delete: Libro fuera de rango");
				return null;
			}
			
		}
		
		/**
		 * Metodo que modifica un libro almacenado en un espacio del Array
		 * @param p contiene todos los datos que queremos modificar, pero 
		 * p.getId() contiene la posicion del array que queremos eliminar
		 * @return la persona modificada en caso de que exista, null en caso
		 * contrario
		 */
		public Libro update(Libro l) {
			
			try {
				
				//Creo una referencia que apunte al List y con los métodos del list voy modificando.
				Libro lAux = listaLibros.get(l.getId());
				lAux.setTitulo(l.getTitulo());
				lAux.setEditorial(l.getEditorial());
				lAux.setNota(l.getNota());
				
				return lAux;
				
			} catch (IndexOutOfBoundsException iobe) {
				System.out.println("update: Libro fuera de rango");
				return null;
			}
		}
		
		
}
