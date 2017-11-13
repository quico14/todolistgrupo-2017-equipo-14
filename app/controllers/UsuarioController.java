package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;

import services.UsuarioService;
import models.Usuario;

import play.Logger;

import security.ActionAuthenticator;

import java.text.ParseException;


public class UsuarioController extends Controller {

  @Inject FormFactory formFactory;

  // Play injecta un usuarioService junto con todas las dependencias necesarias:
  // UsuarioRepository y JPAApi
  @Inject UsuarioService usuarioService;


  public Result saludo(String mensaje) {
     return ok(saludo.render("El mensaje que he recibido es: " + mensaje));
  }

  public Result formularioRegistro() {
     return ok(formRegistro.render(formFactory.form(Registro.class),""));
  }

  public Result registroUsuario() {
     Form<Registro> form = formFactory.form(Registro.class).bindFromRequest();
     if (form.hasErrors()) {
        return badRequest(formRegistro.render(form, "Hay errores en el formulario"));
     }
     Registro datosRegistro = form.get();
     if (usuarioService.findUsuarioPorLogin(datosRegistro.login) != null) {
        return badRequest(formRegistro.render(form, "Login ya existente: escoge otro"));
     }
     if (!datosRegistro.password.equals(datosRegistro.confirmacion)) {
        return badRequest(formRegistro.render(form, "No coinciden la contraseña y la confirmación"));
     }
     Usuario usuario = usuarioService.creaUsuario(datosRegistro.login, datosRegistro.email, datosRegistro.password);
     return redirect(controllers.routes.UsuarioController.formularioLogin());
  }

  public Result acercaDe(){
    return ok(acercaDe.render());
  }

  // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
  // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioUpdate(Long id) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != id) {
      return unauthorized("Lo siento, no estás autorizado");
    } else {
      Usuario usuario = usuarioService.findUsuarioPorId(id);
      System.out.println(usuario.getFechaNacimiento());
      return ok(formUpdate.render(formFactory.form(Update.class), "", id, usuario));
    }
  }

  // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
  // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
  @Security.Authenticated(ActionAuthenticator.class)
  public Result updateUsuario(Long id) throws ParseException {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    Usuario usuario = usuarioService.findUsuarioPorId(id);
    if (connectedUser != id) {
     return unauthorized("Lo siento, no estás autorizado");
    } else {
      Form<Update> form = formFactory.form(Update.class).bindFromRequest();
      if (form.hasErrors()) {
        return badRequest(formUpdate.render(form, "Hay errores en el formulario", id, usuario));
      }
      Update datosUpdate = form.get();
      if (usuarioService.findUsuarioPorLogin(datosUpdate.login) != null) {
        return badRequest(formUpdate.render(form, "Login ya existente: escoge otro", id, usuario));
      }
      if (!datosUpdate.password.equals(datosUpdate.confirmacion)) {
        return badRequest(formUpdate.render(form, "No coinciden la contraseña y la confirmación", id, usuario));
      }

      Usuario usuarioToUpdate = new Usuario(id, datosUpdate.login, datosUpdate.email,
      datosUpdate.password, datosUpdate.nombre, datosUpdate.apellidos, datosUpdate.fechaNacimiento);

      usuarioService.editaUsuario(usuarioToUpdate);

      return redirect(controllers.routes.UsuarioController.detalleUsuario(id));
    }
  }

  public Result formularioLogin() {
     return ok(formLogin.render(formFactory.form(Login.class),""));
  }

  public Result loginUsuario() {
     Form<Login> form = formFactory.form(Login.class).bindFromRequest();
     if (form.hasErrors()) {
        return badRequest(formLogin.render(form, "Hay errores en el formulario"));
     }
     Login login = form.get();
     Usuario usuario = usuarioService.login(login.username, login.password);
     if (usuario == null) {
        return notFound(formLogin.render(form, "Login y contraseña no existentes"));
     } else {
        // Añadimos el id del usuario a la clave `connected` de
        // la sesión de Play
        // https://www.playframework.com/documentation/2.5.x/JavaSessionFlash
        session("connected", usuario.getId().toString());
        return redirect(controllers.routes.GestionTareasController.listaTareas(usuario.getId()));
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

}
