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
import services.ComentarioService;
import models.Usuario;
import models.Tarea;
import models.Comentario;
import security.ActionAuthenticator;

public class GestionComentariosController extends Controller {

  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject TareaService tareaService;
  @Inject ComentarioService comentarioService;

  // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
  // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
  /*@Security.Authenticated(ActionAuthenticator.class)
  public Result formularioNuevoComentario(Long idUsuario, Long idTarea) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        Tarea tarea = tareaService.obtenerTarea(idTarea);
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
  }*/

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listaComentarios(Long idTarea, Long idUsuario) {
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    if (connectedUser != idUsuario) {
       return unauthorized("Lo siento, no estás autorizado");
    } else {
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        Tarea tarea = tareaService.obtenerTarea(idTarea);
        if (tarea == null) {
          return notFound("Tarea no encontrada");
        } else {
          String aviso = flash("aviso");
          List<Comentario> comentariosTarea = comentarioService.allComentarios(idTarea);
          return ok(listaComentarios.render(comentariosTarea, tarea, usuario, aviso));
        }
      }
  }

  /*@Security.Authenticated(ActionAuthenticator.class)
  public Result detalleTablero(Long idUsuario, Long idTablero) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        Tablero tablero = tableroService.findTableroPorId(idTablero);
        return ok(detalleTablero.render(tablero, usuario));
      }
  }*/

}
