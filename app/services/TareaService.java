package services;

import javax.inject.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;
import models.Tablero;
import models.TableroRepository;
import models.Columna;
import models.ColumnaRepository;
import models.Etiqueta;
import models.EtiquetaRepository;


public class TareaService {
    UsuarioRepository usuarioRepository;
    TareaRepository tareaRepository;
    TableroRepository tableroRepository;
    ColumnaRepository columnaRepository;
    EtiquetaRepository etiquetaRepository;

    private Usuario comprobarExistenciaUsuario (Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario == null) {
            throw new TareaServiceException("Usuario no existente");
        }
        return usuario;
    }

    private Tarea comprobarExistenciaTarea (Long idTarea) {
        Tarea tarea = tareaRepository.findById(idTarea);
        if (tarea == null) {
            throw new TareaServiceException("No existe la tarea");
        }
        return tarea;
    }

    private Tablero comprobarExistenciaTablero (Long idTablero) {
        Tablero tablero = tableroRepository.findById(idTablero);
        if (tablero == null) {
            throw new TareaServiceException("El tablero no existe");
        }
        return tablero;
    }

    private Date comprobarFechaLimite (Date fechaLimite) {
        if (fechaLimite != null && fechaLimite.before(new Date())) {
            throw new TareaServiceException("La fecha límite no puede ser anterior a la fecha actual");
        }
        return fechaLimite;
	}

    private Columna comprobarExistenciaColumna (Long idColumna) {
        Columna columna = columnaRepository.findById(idColumna);
        if (columna == null) {
            throw new TareaServiceException("La columna no existe");
        }
        return columna;
	}

	private Etiqueta comprobarExistenciaEtiqueta(Long idEtiqueta) {
		Etiqueta etiqueta = etiquetaRepository.findById(idEtiqueta);
        if (etiqueta == null) {
            throw new TareaServiceException("La etiqueta no existe");
        }
        return etiqueta;
	}


    @Inject
    public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository, 
    ColumnaRepository columnaRepository, TableroRepository tableroRepository, EtiquetaRepository etiquetaRepository ) {
        this.columnaRepository = columnaRepository;
        this.usuarioRepository = usuarioRepository;
        this.tareaRepository = tareaRepository;
        this.tableroRepository = tableroRepository;
        this.etiquetaRepository = etiquetaRepository;
    }

    // Devuelve la lista de tareas de un usuario, ordenadas por su id
    // (equivalente al orden de creación)
    public List<Tarea> allTareasUsuario(Long idUsuario) {
        Usuario usuario = comprobarExistenciaUsuario(idUsuario);
        Set<Tarea> tareas = usuario.getTareas();
        List<Tarea> lista = new ArrayList<Tarea>();
        for(Tarea tarea: tareas) {
          if(tarea.getTablero() == null)
            lista.add(tarea);
        }
        Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return lista;
    }

    public List<Tarea> allTareasTablero(Long idTablero) {
        Tablero tablero = comprobarExistenciaTablero(idTablero);
        Set<Tarea> tareas = tablero.getTareas();
        List<Tarea> lista = new ArrayList<Tarea>(tareas);
        Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
        return lista;
    }

    public Tarea nuevaTarea(Long idUsuario, String titulo, Date nuevaFechaLimite) {
        Usuario usuario = comprobarExistenciaUsuario(idUsuario);
        Date fechalimite = comprobarFechaLimite(nuevaFechaLimite);
        Tarea tarea = new Tarea(usuario, titulo, fechalimite, false);
        return tareaRepository.add(tarea);
    }

    public Tarea nuevaTareaTablero(Long idUsuario, String titulo, Date nuevaFechaLimite, Long idTablero) {
        Usuario usuario = comprobarExistenciaUsuario(idUsuario);
        Tablero tablero = comprobarExistenciaTablero(idTablero);
        Date fechalimite = comprobarFechaLimite(nuevaFechaLimite);
        Tarea tarea = new Tarea(usuario, titulo, fechalimite, false, tablero);
        return tareaRepository.add(tarea);
    }

    public Tarea obtenerTarea(Long idTarea) {
        return tareaRepository.findById(idTarea);
    }

    public Tarea modificaTarea(Long idTarea, String nuevoTitulo, Date nuevaFechaLimite) {
        Tarea tarea = comprobarExistenciaTarea(idTarea);
        tarea.setTitulo(nuevoTitulo);
        Date fechalimite = comprobarFechaLimite(nuevaFechaLimite);
        tarea.setFechaLimite(fechalimite);
        tarea = tareaRepository.update(tarea);
        return tarea;
    }

    public void borraTarea(Long idTarea) {
        Tarea tarea = comprobarExistenciaTarea(idTarea);
        tareaRepository.delete(idTarea);
    }

    public Tarea terminarTarea(Long idTarea) {
        Tarea tarea = comprobarExistenciaTarea(idTarea);
        tarea.setTerminada(true);
        return tareaRepository.done(tarea);
	}

	public Tarea quitarColumna(Long idTarea) {
		Tarea tarea = comprobarExistenciaTarea(idTarea);
		tarea.setColumna(null);
		return tareaRepository.update(tarea);
	}

	public Tarea asignarEtiqueta(Long idTarea, Long idEtiqueta) {
		Tarea tarea = comprobarExistenciaTarea(idTarea);
		Etiqueta etiqueta = comprobarExistenciaEtiqueta(idEtiqueta);
		etiqueta.getTareas().add(tarea);
		etiquetaRepository.update(etiqueta);
		tarea.getEtiquetas().add(etiqueta);
		return tarea;
	}

	public Tarea quitarEtiquetaTarea(Long idTarea, Long idEtiqueta) {
		Tarea tarea = comprobarExistenciaTarea(idTarea);
		Etiqueta etiqueta = comprobarExistenciaEtiqueta(idEtiqueta);
		etiqueta.getTareas().remove(tarea);
		etiquetaRepository.update(etiqueta);
		tarea.getEtiquetas().remove(etiqueta);
		return tarea;
	}
}
