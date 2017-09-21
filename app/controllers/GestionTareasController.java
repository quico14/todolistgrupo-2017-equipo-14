package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.Logger;

import services.UsuarioService;
import services.TareaService;
import models.Usuario;
import models.Tarea;
import security.ActionAuthenticator;

public class GestionTareasController extends Controller {

  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject TareaService tareaService;

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
  public Result creaNuevaTarea(Long idUsuario) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
        if (tareaForm.hasErrors()) {
           Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
           return badRequest(formNuevaTarea.render(usuario, formFactory.form(Tarea.class), "Hay errores en el formulario"));
        }
        Tarea tarea = tareaForm.get();
        tareaService.nuevaTarea(idUsuario, tarea.getTitulo());
        int numTareas = tareaService.allTareasUsuario(idUsuario).size();
        return ok(saludo.render("Total tareas: " + numTareas));
     }
  }
}
