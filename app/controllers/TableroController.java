package controllers;

import play.mvc.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import views.html.*;

import javax.inject.*;

import java.util.List;

import security.ActionAuthenticator;

import models.Tablero;
import models.Usuario;
import services.TableroService;
import services.UsuarioService;

public class TableroController extends Controller {

  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject TableroService tableroService;

  @Security.Authenticated(ActionAuthenticator.class)
  public Result formNuevoTablero (Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
      return unauthorized("Lo siento, no estás autorizado");
    } else {
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      return ok(formNuevoTablero.render(usuario, formFactory.form(Tablero.class),""));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result crearNuevoTablero(Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
    if (connectedUser != idUsuario) {
      return unauthorized("Lo siento, no estás autorizado");
    } else {
      Form<Tablero> tableroForm = formFactory.form(Tablero.class).bindFromRequest();
      if (tableroForm.hasErrors()) {
        return badRequest(formNuevoTablero.render(usuario, formFactory.form(Tablero.class), "Hay errores en el formulario"));
      }
      Tablero tableroNuevo = tableroForm.get();
      List<Tablero> tableros = tableroService.obtenerTodosLosTableros();
      for (Tablero tablero: tableros) {
        System.out.println(tablero.getNombre() + " = " + tableroNuevo.getNombre());
        if (tablero.getNombre().equals(tableroNuevo.getNombre())) {
          return ok(formNuevoTablero.render(usuario, formFactory.form(Tablero.class),"El tablero ya existe"));
        }
      }
      Tablero tablero = tableroService.crearTableroUsuario(tableroNuevo.getNombre(), idUsuario);
      tableroService.anyadirParticipanteTablero(tablero.getId(), idUsuario);
      flash("aviso", "Se ha creado el tablero correctamente");
      return redirect(controllers.routes.TableroController.listarTableros(idUsuario));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listarTableros(Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      List<Tablero> administrados = tableroService.obtenerTablerosAdministradosUsuario(idUsuario);
      List<Tablero> participados = tableroService.obtenerTablerosParticipaUsuario(idUsuario);
      List<Tablero> noRelacionados = tableroService.obtenerTablerosNoParticipaNiAdministraUsuario(idUsuario);
      return ok(listarTableros.render(administrados, participados, noRelacionados, usuario, flash("aviso")));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result apuntarseATablero(Long idUsuario, Long idTablero) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      tableroService.anyadirParticipanteTablero(idTablero, idUsuario);
      flash("aviso", "Te has apuntado!!! Bienvenido!");
      return redirect(controllers.routes.TableroController.listarTableros(idUsuario));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result detalleTablero(Long idUsuario, Long idTablero) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      List<Tablero> participados = tableroService.obtenerTablerosParticipaUsuario(idUsuario);
      Tablero tablero = tableroService.obtenerDetalleDeTablero(idTablero);
      Boolean participa = false;
      // Necesario para poder mostrar o no el botón de apuntarse a un tablero.
      for (Tablero participante: participados) {
        if (participante.getId() == tablero.getId()) {
          participa = true;
        }
      }
      return ok(detalleTablero.render(tablero, usuario, participa, flash("aviso")));
    }
  }
}