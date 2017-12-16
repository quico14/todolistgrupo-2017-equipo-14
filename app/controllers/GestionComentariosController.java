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
  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioNuevoComentario(Long idTarea, Long idUsuario) {
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
          return ok(formNuevoComentario.render(usuario, tarea, "", ""));
        }
     }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result creaNuevoComentario(Long idTarea, Long idUsuario) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String nuevaDescripcion = requestData.get("descripcion");
        if (nuevaDescripcion.equals("")) {
           Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
           Tarea tarea = tareaService.obtenerTarea(idTarea);
           return badRequest(formNuevoComentario.render(usuario, tarea, "",
                              "La descripcion no puede estar vacía."));
        }
        comentarioService.nuevoComentario(idUsuario, idTarea, nuevaDescripcion);
        flash("aviso", "El comentario se ha grabado correctamente");
        return redirect(controllers.routes.GestionComentariosController.listaComentarios(idTarea, idUsuario));
     }
  }

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

  @Security.Authenticated(ActionAuthenticator.class)
  public Result borraComentario(Long idComentario) {
    Comentario comentario = comentarioService.findComentarioPorId(idComentario);
    if (comentario == null) {
      return notFound("Comentario no encontrado");
    } else {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != comentario.getCreador().getId()) {
        return unauthorized("Lo siento, no estás autorizado");
      } else {
        comentarioService.borraComentario(idComentario);
        flash("aviso", "Comentario borrado correctamente");
        return ok();
      }
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioEditaComentario(Long idComentario) {
    Comentario comentario = comentarioService.findComentarioPorId(idComentario);
    if (comentario == null) {
      return notFound("Comentario no encontrado");
    } else {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != comentario.getCreador().getId()) {
        return unauthorized("Lo siento, no estás autorizado");
      } else {
        return ok(formModificacionComentario.render(comentario.getCreador(), comentario.getTareaId(),
                  comentario.getId(), comentario.getDescripcion(), ""));
      }
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result grabaComentarioModificado(Long idComentario) {
    Comentario comentario = comentarioService.findComentarioPorId(idComentario);
    DynamicForm requestData = formFactory.form().bindFromRequest();
    String nuevaDescripcion = requestData.get("descripcion");
    Comentario comentarioModificado = comentarioService.modificaComentario(idComentario, nuevaDescripcion);
    return redirect(controllers.routes.GestionComentariosController.listaComentarios(comentarioModificado.getTareaId().getId(),
                      comentarioModificado.getCreador().getId()));
  }

}
