package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.data.DynamicForm;
import play.Logger;

import java.util.List;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;

import services.UsuarioService;
import services.TareaService;
import services.TableroService;
import services.SizeService;
import models.Usuario;
import models.Size;
import models.Tarea;
import models.Tablero;
import security.ActionAuthenticator;

public class GestionTablerosController extends Controller {

  @Inject FormFactory formFactory;
  @Inject UsuarioService usuarioService;
  @Inject TareaService tareaService;
  @Inject TableroService tableroService;
  @Inject SizeService sizeService;

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
  public Result formularioNuevoSize(Long idTablero) {
     String connectedUserStr = session("connected");
     Tablero tablero = tableroService.findTableroPorId(idTablero);
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != tablero.getAdministrador().getId()) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        return ok(formNuevoSize.render(tablero.getAdministrador(), formFactory.form(Size.class), tablero, ""));
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
        List<Tablero> tablerosAdmin = tableroService.allTablerosUsuario(idUsuario);
        List<Tablero> tablerosParticipa = tableroService.getTableros(idUsuario);
        List<Tablero> tablerosSinRelacion = tableroService.getTablerosSinRelacion(idUsuario);
        return ok(listaTableros.render(tablerosAdmin, tablerosParticipa, tablerosSinRelacion, usuario, aviso));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listaSizes(Long idTablero) {
      String connectedUserStr = session("connected");
      Tablero tablero = tableroService.findTableroPorId(idTablero);
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != tablero.getAdministrador().getId()) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
        String aviso = flash("aviso");
        return ok(listaSizes.render(tablero.getTareaSize(), tablero, tablero.getAdministrador(), aviso));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result listaSizesJson(Long idUsuario, Long idTablero) {
      String connectedUserStr = session("connected");
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != idUsuario) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
        String aviso = flash("aviso");
        ObjectNode result = Json.newObject();
        Tablero tablero = tableroService.findTableroPorId(idTablero);
        List <Size> sizes = tablero.getTareaSize();
        return ok(Json.toJson(sizes));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result borraSize(Long idTablero, String nombreSize) {

    tableroService.removeTareaSize(sizeService.findPorNombre(nombreSize), tableroService.findTableroPorId(idTablero));
    flash("aviso", "Tamaño borrado correctamente");
    return ok();
  }



  @Security.Authenticated(ActionAuthenticator.class)
  public Result creaNuevoSize(Long idTablero) {
      String connectedUserStr = session("connected");
      Tablero tablero = tableroService.findTableroPorId(idTablero);
      Long connectedUser =  Long.valueOf(connectedUserStr);
      if (connectedUser != tablero.getAdministrador().getId()) {
         return unauthorized("Lo siento, no estás autorizado");
      } else {
        Form<Size> sizeForm = formFactory.form(Size.class).bindFromRequest();
        if (sizeForm.hasErrors()) {
           return badRequest(formNuevoSize.render(tablero.getAdministrador(), formFactory.form(Size.class), tablero, "Hay errores en el formulario"));
        }
        Size size = sizeForm.get();
        tableroService.addTareaSize(size.getNombre(), tablero);
        flash("aviso", "El tamaño se ha creado correctamente");
        return redirect(controllers.routes.GestionTablerosController.listaSizes(tablero.getId()));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result participarTablero(Long idUsuario, Long idTablero) {
     String connectedUserStr = session("connected");
     Long connectedUser =  Long.valueOf(connectedUserStr);
     if (connectedUser != idUsuario) {
        return unauthorized("Lo siento, no estás autorizado");
     } else {
        String aviso = flash("aviso");
        Tablero tablero = tableroService.findTableroPorId(idTablero);
        tableroService.addParticipante(idUsuario, tablero);

        return redirect(controllers.routes.GestionTablerosController.listaTableros(idUsuario));
      }
  }

  @Security.Authenticated(ActionAuthenticator.class)
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
  }

}
