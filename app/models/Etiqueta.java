package models;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import javax.persistence.*;

@Entity
public class Etiqueta {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	private String nombre;

	@ManyToOne
	@JoinColumn(name = "tableroId")
	private Tablero tablero;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="Etiqueta_Tarea")
	private Set<Tarea> tareas = new HashSet<Tarea>();

	public Etiqueta() {
	}

	public Etiqueta(Tablero tablero) {
		this.tablero = tablero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Tarea> getTareas() {
		return tareas;
	}

	public void setTareas(Set<Tarea> tareas) {
		this.tareas = tareas;
	}

	public Tablero getTablero() {
		return tablero;
	}

	public void setTablero(Tablero tablero) {
		this.tablero = tablero;
	}

	public void addTarea(Tarea tarea) {
		this.tareas.add(tarea);
	}

	public void eliminarTarea(Tarea tarea) {
		this.tareas.remove(tarea);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Etiqueta other = (Etiqueta) obj;
		if (id != null && other.id != null)
			return ((long) id == (long) other.id);
		else {
			if (nombre == null) {
				if (other.nombre != null)
					return false;
			} else if (!nombre.equals(other.nombre))
				return false;
		}
		return true;
	}
}
