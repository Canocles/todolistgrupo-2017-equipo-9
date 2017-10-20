package controllers;

import play.mvc.*;

import views.html.*;

import javax.inject.*;

import java.util.List;

import security.ActionAuthenticator;

import models.Tablero;
import models.Usuario;
import services.TableroService;
import services.UsuarioService;

public class TableroController extends Controller {

  @Inject UsuarioService usuarioService;
  @Inject TableroService tableroService;

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listarTablerosAdministrados(Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
      String aviso = flash("Lista de Tableros administrados");
      Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
      List<Tablero> tableros = tableroService.allTablerosUsuario(idUsuario);
      return ok(listarTablerosAdministrados.render(tableros, usuario, aviso));
    }
  }
}
