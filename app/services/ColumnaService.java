package services;

import javax.inject.*;

import java.util.ArrayList;
import java.util.List;

/*import java.util.Date;
import java.util.Set;
import java.util.Collections;
*/
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

	public List<Tarea> allTareasColumna(Long idColumna) {
		Columna columna = columnaRepository.findById(idColumna);
		if (columna == null) {
			throw new ColumnaServiceException("No existe la columna");
		}
		List<Tarea> lista = new ArrayList<Tarea>(columna.getTareas());
		return lista;
	}

	public List<Columna> allColumnasTablero(Long idTablero) {
		Tablero tablero = tableroRepository.findById(idTablero);
		if (tablero == null) {
			throw new ColumnaServiceException("Tablero no existente");
		}
		List<Columna> lista = new ArrayList<Columna>(tablero.getColumnas());
		return lista;
	}

	public Columna nuevaColumna(Long idTablero, String nombre) {
		Tablero tablero = tableroRepository.findById(idTablero);
		if (tablero == null) {
			throw new ColumnaServiceException("Tablero no existente");
		}
		Columna columna = new Columna(tablero, nombre);
		return columnaRepository.add(columna);
	}

	public Columna obtenerColumna(Long idColumna) {
		return columnaRepository.findById(idColumna);
	}

	public Columna modificaColumna(Long idColumna, String nuevoNombre) {
		Columna columna = columnaRepository.findById(idColumna);
		if (columna == null) {
			throw new ColumnaServiceException("No existe columna");
		}
		columna.setNombre(nuevoNombre);
		columna = columnaRepository.update(columna);
		return columna;
	}

	public Columna addTareaColumna(Long idColumna, Long idTarea) {
		Columna columna = columnaRepository.findById(idColumna);
		Tarea tarea = tareaRepository.findById(idTarea);
		if (columna == null) {
			throw new ColumnaServiceException("No existe la columna");
		}
		if (tarea == null) {
			throw new ColumnaServiceException("No existe la tarea");
		}
		columna.addTarea(tarea);
		return columnaRepository.update(columna);
	}

	public Columna borraTareaColumna(Long idColumna, Long idTarea) {
		Columna columna = columnaRepository.findById(idColumna);
		Tarea tarea = tareaRepository.findById(idTarea);
		if (columna == null) {
			throw new ColumnaServiceException("No existe la columna");
		}
		if (tarea == null) {
			throw new ColumnaServiceException("No existe la tarea");
		}
		columna.eliminarTarea(tarea);
		return columnaRepository.update(columna);
	}

	public void borraColumna(Long idColumna) {
		Columna columna = columnaRepository.findById(idColumna);
		if (columna == null)
			throw new ColumnaServiceException("No existe la columna");
		columnaRepository.delete(idColumna);
	}
}
