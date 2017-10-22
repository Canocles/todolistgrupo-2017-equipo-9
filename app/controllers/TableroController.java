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
    if (connectedUser != idUsuario) {
      return unauthorized("Lo siento, no estás autorizado");
    } else {
      Form<Tablero> tableroForm = formFactory.form(Tablero.class).bindFromRequest();
      if (tableroForm.hasErrors()) {
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        return badRequest(formNuevoTablero.render(usuario, formFactory.form(Tablero.class), "Hay errores en el formulario"));
      }
      Tablero tablero = tableroForm.get();
      tableroService.crearTableroUsuario(tablero.getNombre(), idUsuario);
      flash("aviso", "El tablero se ha guardado correctamente");
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
      String aviso = flash("Lista de Tableros");
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      List<Tablero> administrados = tableroService.obtenerTablerosAdministradosUsuario(idUsuario);
      List<Tablero> participados = tableroService.obtenerTablerosParticipaUsuario(idUsuario);
      List<Tablero> noRelacionados = tableroService.obtenerTablerosNoParticipaNiAdministraUsuario(idUsuario);
      return ok(listarTableros.render(administrados, participados, noRelacionados, usuario, aviso));
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
      Tablero tablero = tableroService.obtenerDetalleDeTablero(idTablero);
      return ok(detalleTablero.render(tablero, usuario));
    }
  }
}
