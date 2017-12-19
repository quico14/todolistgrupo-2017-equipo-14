package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import java.util.List;
import java.util.ArrayList;

import services.UsuarioService;
import services.TareaService;
import services.GrupoService;
import services.GrupoServiceException;
import models.Usuario;
import models.Tarea;
import models.Grupo;
import security.ActionAuthenticator;

public class GestionGruposController extends Controller {

  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject GrupoService grupoService;

  // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
  // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioNuevoGrupo(Long idUsuario) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        return ok(formNuevoGrupo.render(usuario, formFactory.form(Grupo.class),""));
     }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result creaNuevoGrupo(Long idUsuario) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        Form<Grupo> grupoForm = formFactory.form(Grupo.class).bindFromRequest();
        if (grupoForm.hasErrors()) {
           Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
           return badRequest(formNuevoGrupo.render(usuario, formFactory.form(Grupo.class), "Hay errores en el formulario"));
        }
        Grupo grupo = grupoForm.get();
        grupoService.nuevoGrupo(idUsuario, grupo.getNombre());
        flash("aviso", "El grupo se ha creado correctamente");
        return redirect(controllers.routes.GestionGruposController.listaGrupos(idUsuario));
     }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listaGrupos(Long idUsuario) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        List<Grupo> gruposAdmin = grupoService.allGruposUsuario(idUsuario);
        List<Grupo> gruposParticipa = grupoService.getGrupos(idUsuario);
        return ok(listaGrupos.render(gruposAdmin, gruposParticipa, usuario, aviso));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listaParticipantes(Long idGrupo) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     Grupo grupo = grupoService.findGrupoPorId(idGrupo);

     if (connectedUser != grupo.getAdministrador().getId()) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        return ok(listaParticipantes.render(new ArrayList<Usuario>(grupo.getParticipantes()), grupo.getAdministrador(), grupo, aviso));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result participarGrupo(Long idUsuario, Long idGrupo) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String username = requestData.get("nombreUsuario");
        Grupo grupo = grupoService.findGrupoPorId(idGrupo);
        try{
          grupoService.addParticipante(username, grupo);
          return redirect(controllers.routes.GestionGruposController.listaParticipantes(idGrupo));
        } catch (GrupoServiceException ex) {
          String mensaje = ex.getMessage();
          return ok(formParticiparGrupo.render(grupo.getAdministrador(),
          grupo.getId(),
          grupo.getNombre(),
          mensaje));
        }
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result detalleGrupo(Long idUsuario, Long idGrupo) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
        Grupo grupo = grupoService.findGrupoPorId(idGrupo);
        return ok(detalleGrupo.render(grupo, usuario));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result borraGrupo(Long idGrupo) {
     grupoService.deleteGrupo(idGrupo);
     flash("aviso", "Grupo borrado correctamente");
     return ok();
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result borraParticipante(Long idUsuario, Long idGrupo) {
     grupoService.removeParticipante(idUsuario, grupoService.findGrupoPorId(idGrupo));
     flash("aviso", "Participante borrado correctamente");
     return ok();
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result formularioEditaGrupo(Long idGrupo) {
    Grupo grupo = grupoService.findGrupoPorId(idGrupo);
    if (grupo == null) {
      return notFound("Grupo no encontrado");
    } else {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != grupo.getAdministrador().getId()) {
        return unauthorized("Lo siento, no estás autorizado");
      } else {
        return ok(formModificacionGrupo.render(grupo.getAdministrador(),
        grupo.getId(),
        grupo.getNombre(),
        ""));
      }
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result formParticiparGrupo(Long idGrupo) {
    Grupo grupo = grupoService.findGrupoPorId(idGrupo);
    if (grupo == null) {
      return notFound("Grupo no encontrado");
    } else {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != grupo.getAdministrador().getId()) {
        return unauthorized("Lo siento, no estás autorizado");
      } else {
        return ok(formParticiparGrupo.render(grupo.getAdministrador(),
        grupo.getId(),
        grupo.getNombre(),
        ""));
      }
    }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result grabaGrupoModificado(Long idGrupo){
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    Grupo grupo = grupoService.findGrupoPorId(idGrupo);

    if (connectedUser != grupo.getAdministrador().getId()) {
      return unauthorized("Lo siento, no estás autorizado");
    }

    DynamicForm requestData = formFactory.form().bindFromRequest();
    String nuevoTitulo = requestData.get("nombreGrupo");
    if(nuevoTitulo.equals("")) {
       String mensaje = "El nombre del grupo no puede quedar vacío";
       return ok(formModificacionGrupo.render(grupo.getAdministrador(),
       grupo.getId(),
       grupo.getNombre(),
       mensaje));
     }

    Grupo grupoModificado = grupoService.modificaGrupo(idGrupo, nuevoTitulo);
    return redirect(controllers.routes.GestionGruposController.listaGrupos(connectedUser));
  }

}
