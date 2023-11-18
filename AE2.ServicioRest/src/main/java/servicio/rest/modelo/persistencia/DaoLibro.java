package servicio.rest.modelo.persistencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import servicio.rest.modelo.entidad.Libro;

@Component
public class DaoLibro {

	public List<Libro> listaLibros;
	public int contador;

	public DaoLibro() {
		System.out.println("DaoLibro -> Creando la lista de Libros!");
		listaLibros = new ArrayList<Libro>();
		Libro l1 = new Libro(contador++, "Holly", "Plaza Janés", 9.6);
		Libro l2 = new Libro(contador++, "El Problema Final", "Alfaguara", 9.0);
		Libro l3 = new Libro(contador++, "El Cuco de Cristal", "Suma", 9.6);
		Libro l4 = new Libro(contador++, "La Paciente Silenciosa", "Debols!llo", 9.2);
		Libro l5 = new Libro(contador++, "Reina Roja", "B", 9.8);

		listaLibros.add(l1);
		listaLibros.add(l2);
		listaLibros.add(l3);
		listaLibros.add(l4);
		listaLibros.add(l5);

	}

	public Libro get(int posicion) {
		try {
			return listaLibros.get(posicion);
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("Libro fuera de rango");
			return null;
		}
	}

	public List<Libro> list() {
		return listaLibros;
	}

	public void add(Libro l) {
		
		for (Libro libro : listaLibros) {
			if (libro.getId() == l.getId() || libro.getTitulo().equals(l.getTitulo())) {
				// Si el ID o el título ya existen, devuelve false y no agrega el libro
				System.out.println("El libro ya esta dado de alta");
			}
		}

		// Si el ID y el título no existen, se agrega el libro a la lista
		l.setId(contador++);
		listaLibros.add(l);
		
	}
		

	public Libro delete(int posicion) {
		try {
			return listaLibros.remove(posicion);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Delete -> Libro fuera de rango");
			return null;
		}
	}

	public Libro update(Libro l) {
		try {
			Libro lAux = listaLibros.get(l.getId());
			lAux.setTitulo(l.getTitulo());
			lAux.setEditorial(l.getEditorial());
			lAux.setNota(l.getNota());

			return lAux;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("Update -> Persona fuera de rango");
			return null;
		}
	}

	public List<Libro> listByTitulo(String titulo) {
		List<Libro> listaLibrosAux = new ArrayList<Libro>();
		for (Libro l : listaLibros) {
			if (l.getTitulo().equalsIgnoreCase(titulo)) {
				listaLibrosAux.add(l);

			}
		}
		return listaLibrosAux;
	}
}
