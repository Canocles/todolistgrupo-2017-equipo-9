package services;

import javax.inject.*;

import java.util.ArrayList;
import java.util.List;

import services.ColumnaServiceException;

import models.Usuario;
import models.UsuarioRepository;
import models.Tablero;
import models.TableroRepository;
import models.Columna;
import models.ColumnaRepository;
import models.Tarea;
import models.TareaRepository;

public class ColumnaService {
	UsuarioRepository usuarioRepository;
	TareaRepository tareaRepository;
	TableroRepository tableroRepository;
	ColumnaRepository columnaRepository;

	@Inject
	public ColumnaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository,
			TableroRepository tableroRepository, ColumnaRepository columnaRepository) {
		this.usuarioRepository = usuarioRepository;
		this.tareaRepository = tareaRepository;
		this.tableroRepository = tableroRepository;
		this.columnaRepository = columnaRepository;
	}

	private Columna getColumna(Long idColumna) {
		Columna columna = columnaRepository.findById(idColumna);
		if (columna == null) {
			throw new ColumnaServiceException("No existe la columna");
		}
		return columna;
	}

	private Tablero getTablero(Long idTablero) {
		Tablero tablero = tableroRepository.findById(idTablero);
		if (tablero == null) {
			throw new ColumnaServiceException("No existe el tablero");
		}
		return  tablero;
	}

	private Tarea getTarea(Long idTarea) {
		Tarea tarea = tareaRepository.findById(idTarea);
		if (tarea == null) {
			throw new ColumnaServiceException("No existe la tarea");
		}
		return tarea;
	}

	public List<Tarea> allTareasColumna(Long idColumna) {
		Columna columna = getColumna(idColumna);
		List<Tarea> lista = new ArrayList<Tarea>(columna.getTareas());
		return lista;
	}

	public List<Columna> allColumnasTablero(Long idTablero) {
		Tablero tablero = getTablero(idTablero);
		List<Columna> lista = new ArrayList<Columna>(tablero.getColumnas());
		return lista;
	}

	public Columna nuevaColumna(Long idTablero, String nombre) {
		Tablero tablero = getTablero(idTablero);
		Columna columna = new Columna(tablero, nombre);
		return columnaRepository.add(columna);
	}

	public Columna obtenerColumna(Long idColumna) {
		return getColumna(idColumna);
	}

	public Columna modificaColumna(Long idColumna, String nuevoNombre) {
		Columna columna = getColumna(idColumna);
		columna.setNombre(nuevoNombre);
		return columnaRepository.update(columna);
	}

	public Columna addTareaColumna(Long idColumna, Long idTarea) {
		if(idColumna == null) {
			Tarea tarea = getTarea(idTarea);
			tarea.setColumna(null);
			return null;
		}

		Columna columna = getColumna(idColumna);
		Tarea tarea = getTarea(idTarea);
		tarea.setColumna(columna);
		tareaRepository.update(tarea);
		columna.getTareas().add(tarea);
		return columna;
	}

	public Columna borraTareaColumna(Long idColumna, Long idTarea) {
		Columna columna = getColumna(idColumna);
		Tarea tarea = getTarea(idTarea);
		columna.eliminarTarea(tarea);
		return columnaRepository.update(columna);
	}

	public void borraColumna(Long idColumna) {
		Columna columna = getColumna(idColumna);
		columnaRepository.delete(idColumna);
	}
}
