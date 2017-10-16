package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import java.util.List;

import services.UsuarioService;
import services.TareaService;
import services.TableroService;
import models.Usuario;
import models.Tarea;
import models.Tablero;
import security.ActionAuthenticator;

public class GestionTablerosController extends Controller {

  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject TareaService tareaService;
  @Inject TableroService tableroService;

  // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
  // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioNuevoTablero(Long idUsuario) {
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
  public Result creaNuevoTablero(Long idUsuario) {
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
        tableroService.nuevoTablero(idUsuario, tablero.getNombre());
        flash("aviso", "El tablero se ha creado correctamente");
        return redirect(controllers.routes.GestionTablerosController.listaTableros(idUsuario));
     }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listaTableros(Long idUsuario) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        List<Tablero> tableros = tableroService.allTablerosUsuario(idUsuario);
        return ok(listaTableros.render(tableros, usuario, aviso));
      }
  }
  //
  // @Security.Authenticated(ActionAuthenticator.class)
  // public Result formularioEditaTarea(Long idTarea) {
  //   Tarea tarea = tareaService.obtenerTarea(idTarea);
  //   if (tarea == null) {
  //     return notFound("Tarea no encontrada");
  //   } else {
  //     String connectedUserStr = session("connected");
  //     Long connectedUser =  Long.valueOf(connectedUserStr);
  //     if (connectedUser != tarea.getUsuario().getId()) {
  //       return unauthorized("Lo siento, no estás autorizado");
  //     } else {
  //       return ok(formModificacionTarea.render(tarea.getUsuario().getId(),
  //       tarea.getId(),
  //       tarea.getTitulo(),
  //       ""));
  //     }
  //   }
  // }
  //
  // @Security.Authenticated(ActionAuthenticator.class)
  // public Result grabaTareaModificada(Long idTarea) {
  //    DynamicForm requestData = formFactory.form().bindFromRequest();
  //    String nuevoTitulo = requestData.get("titulo");
  //    Tarea tarea = tareaService.modificaTarea(idTarea, nuevoTitulo);
  //    return redirect(controllers.routes.GestionTareasController.listaTareas(tarea.getUsuario().getId()));
  // }
  //
  // @Security.Authenticated(ActionAuthenticator.class)
  // public Result borraTarea(Long idTarea) {
  //    tareaService.borraTarea(idTarea);
  //    flash("aviso", "Tarea borrada correctamente");
  //    return ok();
  // }
}
