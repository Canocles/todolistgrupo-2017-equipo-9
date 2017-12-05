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


public class TareaService {
   UsuarioRepository usuarioRepository;
   TareaRepository tareaRepository;
   TableroRepository tableroRepository;

   @Inject
   public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository, TableroRepository tableroRepository) {
      this.usuarioRepository = usuarioRepository;
      this.tareaRepository = tareaRepository;
      this.tableroRepository = tableroRepository;
   }

   // Devuelve la lista de tareas de un usuario, ordenadas por su id
   // (equivalente al orden de creación)
   public List<Tarea> allTareasUsuario(Long idUsuario) {
      Usuario usuario = usuarioRepository.findById(idUsuario);
      if (usuario == null) {
         throw new TareaServiceException("Usuario no existente");
      }
      Set<Tarea> tareas = usuario.getTareas();
      List<Tarea> lista = new ArrayList<Tarea>(tareas);
      Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
      return lista;
   }

   public List<Tarea> allTareasTablero(Long idTablero) {
       Tablero tablero = tableroRepository.findById(idTablero);
       if (tablero == null) {
           throw new TareaServiceException("El tablero no existe");
       }
       Set<Tarea> tareas = tablero.getTareas();
       List<Tarea> lista = new ArrayList<Tarea>(tareas);
       Collections.sort(lista, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
       return lista;
   }

   public Tarea nuevaTarea(Long idUsuario, String titulo, Date nuevaFechaLimite) {
      Usuario usuario = usuarioRepository.findById(idUsuario);
      if (usuario == null) {
         throw new TareaServiceException("Usuario no existente");
      }
      if (nuevaFechaLimite != null && nuevaFechaLimite.before(new Date())) {
         throw new TareaServiceException("La fecha límite no puede ser anterior a la fecha actual");
      }
      Tarea tarea = new Tarea(usuario, titulo, nuevaFechaLimite, false);
      return tareaRepository.add(tarea);
   }

   public Tarea nuevaTareaTablero(Long idUsuario, String titulo, Date nuevaFechaLimite, Long idTablero) {
       Usuario usuario = usuarioRepository.findById(idUsuario);
       Tablero tablero = tableroRepository.findById(idTablero);
       if (usuario == null) {
           throw new TareaServiceException("El usuario no existente");
       }
       if (tablero == null) {
           throw new TareaServiceException("El tablero no existe");
       }
       if (nuevaFechaLimite != null && nuevaFechaLimite.before(new Date())) {
           throw new TareaServiceException("La fecha límite no puede ser anterior a la fecha actual");
       }
       Tarea tarea = new Tarea(usuario, titulo, nuevaFechaLimite, false, tablero);
       return tareaRepository.add(tarea);
   } 

   public Tarea obtenerTarea(Long idTarea) {
      return tareaRepository.findById(idTarea);
   }

   public Tarea modificaTarea(Long idTarea, String nuevoTitulo, Date nuevaFechaLimite) {
      Tarea tarea = tareaRepository.findById(idTarea);
      if (tarea == null)
           throw new TareaServiceException("No existe tarea");
      tarea.setTitulo(nuevoTitulo);
      if (nuevaFechaLimite != null && nuevaFechaLimite.before(new Date())) {
         throw new TareaServiceException("La fecha límite no puede ser anterior a la fecha actual");
      }
      tarea.setFechaLimite(nuevaFechaLimite);
      tarea = tareaRepository.update(tarea);
      return tarea;
   }

   public void borraTarea(Long idTarea) {
      Tarea tarea = tareaRepository.findById(idTarea);
      if (tarea == null)
           throw new TareaServiceException("No existe la tarea");
      tareaRepository.delete(idTarea);
   }

   public Tarea terminarTarea(Long idTarea) {
       Tarea tarea = tareaRepository.findById(idTarea);
       if (tarea == null) {
           throw new TareaServiceException("No existe la tarea");
       }
       tarea.setTerminada(true);
       return tareaRepository.done(tarea);
   }
}
