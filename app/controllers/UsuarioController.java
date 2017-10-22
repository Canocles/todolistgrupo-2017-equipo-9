package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.Logger;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import services.UsuarioService;
import models.Usuario;
import security.ActionAuthenticator;

public class UsuarioController extends Controller {

   @Inject FormFactory formFactory;

   // Play injecta un usuarioService junto con todas las dependencias necesarias:
   // UsuarioRepository y JPAApi
   @Inject UsuarioService usuarioService;

   public Result saludo(String mensaje) {
      return ok(saludo.render("El mensaje que he recibido es: " + mensaje));
   }

   public Result formularioRegistro() {
      return ok(formRegistro.render(""));
   }

   public Result registroUsuario() {
      DynamicForm form = formFactory.form().bindFromRequest();
      String login = form.get("login");
      String email = form.get("email");
      String password = form.get("password");
      String confirmacion = form.get("confirmacion");

      if (usuarioService.findUsuarioPorLogin(login) != null) {
         return ok(formRegistro.render("Login ya existente: escoge otro"));
      }

      if (!password.equals(confirmacion)) {
         return ok(formRegistro.render("No coinciden la contraseña y la confirmación"));
      }
      Usuario usuario = usuarioService.creaUsuario(login, email, password);
      return redirect(controllers.routes.UsuarioController.formularioLogin());
   }

   public Result formularioLogin() {
      return ok(formLogin.render(""));
   }

   public Result loginUsuario() {
      DynamicForm form = formFactory.form().bindFromRequest();
      if (form.hasErrors()) {
         return badRequest(formLogin.render("Hay errores en el formulario"));
      }
      String login = form.get("login");
      String password = form.get("password");
      Usuario usuario = usuarioService.login(login, password);
      if (usuario == null) {
         return notFound(formLogin.render("Login y contraseña no existentes"));
      } else {
         // Añadimos el id del usuario a la clave `connected` de
         // la sesión de Play
         // https://www.playframework.com/documentation/2.5.x/JavaSessionFlash
         session("connected", usuario.getId().toString());
         return redirect(controllers.routes.UsuarioController.detalleUsuario(usuario.getId()));
      }
   }

   // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
   // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
   @Security.Authenticated(ActionAuthenticator.class)
   public Result logout() {
      String connectedUserStr = session("connected");
      session().remove("connected");
      return redirect(controllers.routes.UsuarioController.loginUsuario());
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result detalleUsuario(Long id) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != id) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
         Usuario usuario = usuarioService.findUsuarioPorId(id);
         if (usuario == null) {
            return notFound("Usuario no encontrado");
         } else {
            Logger.debug("Encontrado usuario " + usuario.getId() + ": " + usuario.getLogin());
            return ok(detalleUsuario.render(usuario));
         }
      }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result formularioEditaUsuario(Long id) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != id) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
       Usuario usuario = usuarioService.findUsuarioPorId(id);
       if (usuario == null) {
         return notFound("Usuario no encontrado");
       } else {
         return ok(formModificacionUsuario.render(usuario));
       }
     }
   }

   @Security.Authenticated(ActionAuthenticator.class)
   public Result grabaUsuarioModificado(Long id) {
     DynamicForm requestData = formFactory.form().bindFromRequest();
     String login = usuarioService.findUsuarioPorId(id).getLogin();
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     Date nuevaFechaNacimiento = null;
     try {
       nuevaFechaNacimiento = sdf.parse(requestData.get("fechaNacimiento"));
     } catch (ParseException ex) {
       System.out.println(ex);
     }
     String nuevoEmail = requestData.get("email");
     String nuevoNombre = requestData.get("nombre");
     String nuevosApellidos = requestData.get("apellidos");
     Usuario usuario = usuarioService.modificaUsuario(login, nuevoEmail, nuevoNombre, nuevosApellidos, nuevaFechaNacimiento);
     return redirect(controllers.routes.UsuarioController.detalleUsuario(usuario.getId()));
   }
}
