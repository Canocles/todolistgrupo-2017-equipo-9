package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import services.UsuarioService;
import services.TareaService;
import models.Usuario;
import models.Tarea;
import models.Tablero;
import services.TableroService;
import security.ActionAuthenticator;

public class GestionTareasController extends Controller {

   @Inject FormFactory formFactory;
   @Inject UsuarioService usuarioService;
   @Inject TareaService tareaService;
   @Inject TableroService tableroService;

   // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
   // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioNuevaTarea(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         return ok(formNuevaTarea.render(usuario, formFactory.form(Tarea.class),""));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioNuevaTareaTablero(Long idUsuario, Long idTablero) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Tablero tablero = tableroService.obtenerDetalleDeTablero(idTablero);
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         return ok(formNuevaTareaTablero.render(tablero, usuario, formFactory.form(Tarea.class),""));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result creaNuevaTarea(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
         if (tareaForm.hasErrors()) {
            return badRequest(formNuevaTarea.render(usuario, formFactory.form(Tarea.class), "La fecha no tiene el formato correcto"));
         }
         Tarea tarea = tareaForm.get();
				 try {
					 tareaService.nuevaTarea(idUsuario, tarea.getTitulo(), tarea.getFechaLimite());
				 }
				 catch (Exception e) {
					 flash("aviso", e.getMessage());
					 return ok(formNuevaTarea.render(usuario, formFactory.form(Tarea.class), e.getMessage()));
				 }
         flash("aviso", "La tarea se ha grabado correctamente");
         return redirect(controllers.routes.GestionTareasController.listaTareas(idUsuario));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result creaNuevaTareaTablero(Long idUsuario, Long idTablero) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      Tablero tablero = tableroService.obtenerDetalleDeTablero(idTablero);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
         if (tareaForm.hasErrors()) {
            return badRequest(formNuevaTareaTablero.render(tablero, usuario, formFactory.form(Tarea.class), "La fecha no tiene el formato correcto"));
         }
         Tarea tarea = tareaForm.get();
				 try {
          tareaService.nuevaTareaTablero(idUsuario, tarea.getTitulo(), tarea.getFechaLimite(), idTablero);
				 }
				 catch (Exception e) {
					 flash("aviso", e.getMessage());
					 return ok(formNuevaTareaTablero.render(tablero, usuario, formFactory.form(Tarea.class), e.getMessage()));
				 }
         flash("aviso", "La tarea se ha grabado correctamente");
         return redirect(controllers.routes.TableroController.detalleTablero(idUsuario, idTablero));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result listaTareas(Long idUsuario) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         String aviso = flash("aviso");
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         List<Tarea> tareas = tareaService.allTareasUsuario(idUsuario);
         return ok(listaTareas.render(tareas, usuario, aviso));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result listaTareasTablero(Long idUsuario, Long idTablero) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         String aviso = flash("aviso");
         Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
         Tablero tablero = tableroService.obtenerDetalleDeTablero(idTablero);
         List<Tarea> tareas = tareaService.allTareasTablero(idTablero);
         return ok(listaTareas.render(tareas, usuario, aviso));
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioEditaTarea(Long idTarea) {
      Tarea tarea = tareaService.obtenerTarea(idTarea);
      if (tarea == null) {
         return notFound("Tarea no encontrada");
      } else {
         String connectedUserStr = session("connected");
         Long connectedUser =  Long.valueOf(connectedUserStr);
         if (connectedUser != tarea.getUsuario().getId()) {
            return unauthorized("Lo siento, no estás autorizado");
         } else {
            return ok(formModificacionTarea.render(tarea.getUsuario().getId(),
            tarea.getId(),
						tarea.getTitulo(),
            tarea.getFechaLimiteString(),
            ""
						));
         }
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result grabaTareaModificada(Long idTarea) {
      DynamicForm requestData = formFactory.form().bindFromRequest();
      String nuevoTitulo = requestData.get("titulo");
			Date nuevaFechaLimite = null;
			if(!requestData.get("fechaLimite").equals("")){
				try {
					nuevaFechaLimite = new SimpleDateFormat("dd-MM-yyyy").parse(requestData.get("fechaLimite"));
				}
				catch (Exception e) {
					nuevaFechaLimite = null;
				}
			}
			Tarea tarea = tareaService.obtenerTarea(idTarea);
			try {
				tarea = tareaService.modificaTarea(idTarea, nuevoTitulo, nuevaFechaLimite);
			}
			catch (Exception e) {

			}
      return redirect(controllers.routes.GestionTareasController.listaTareas(tarea.getUsuario().getId()));
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result borraTarea(Long idTarea) {
      tareaService.borraTarea(idTarea);
      flash("aviso", "Tarea borrada correctamente");
      return ok();
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result terminarTarea(Long idTarea) {
      tareaService.terminarTarea(idTarea);
      Tarea tarea = tareaService.obtenerTarea(idTarea);
      flash("aviso", "Tarea terminada");
      return redirect(controllers.routes.GestionTareasController.listaTareas(tarea.getUsuario().getId()));
  }
}
