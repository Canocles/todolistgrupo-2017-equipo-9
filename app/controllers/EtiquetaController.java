package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Tablero;
import models.Etiqueta;
import services.EtiquetaService;
import services.TableroService;
import security.ActionAuthenticator;

public class EtiquetaController extends Controller {
  
  @Inject FormFactory formFactory;
  @Inject TableroService tableroService;
  @Inject EtiquetaService etiquetaService;

  @Security.Authenticated(ActionAuthenticator.class)
  public Result crearEtiqueta(Long idUsuario, Long idTablero) {
    DynamicForm requestData = formFactory.form().bindFromRequest();
    String nombre = requestData.get("etiquetaNombre");
    String color = requestData.get("etiquetaColor");
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
      return unauthorized("Lo siento, no estás autorizado");
    } 
    else {
      etiquetaService.crearEtiquetaTablero(idTablero, nombre, color);
      return redirect(controllers.routes.TableroController.detalleTablero(idUsuario, idTablero));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result actualizarEtiqueta(Long idEtiqueta) {
    Etiqueta etiqueta = etiquetaService.getEtiqueta(idEtiqueta);
    DynamicForm requestData = formFactory.form().bindFromRequest();
    Long idTablero = etiqueta.getTablero().getId();
    Long idUsuario = etiqueta.getTablero().getAdministrador().getId();
    String nombre = requestData.get("etiquetaNombre");
    String color = requestData.get("etiquetaColor");
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
    	return unauthorized("Lo siento, no estás autorizado");
    } else {
    	etiquetaService.modificarEtiquetaTablero(idEtiqueta, nombre, color);
    	return redirect(controllers.routes.TableroController.detalleTablero(idUsuario, idTablero));
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result eliminarEtiqueta(Long idEtiqueta) {
    Etiqueta etiqueta = etiquetaService.getEtiqueta(idEtiqueta);
    Long idTablero = etiqueta.getTablero().getId();
    Long idUsuario = etiqueta.getTablero().getAdministrador().getId();
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
    	return unauthorized("Lo siento, no estás autorizado");
    } else {
      etiquetaService.eliminarEtiquetaTablero(idEtiqueta);
      return redirect(controllers.routes.TableroController.detalleTablero(idUsuario, idTablero));
    }
  }
}