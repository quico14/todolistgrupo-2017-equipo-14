package controllers;

import play.mvc.*;

import views.html.*;
import javax.inject.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;

import services.UsuarioService;
import models.Usuario;
import models.Tarea;

import play.Logger;
import java.util.List;
import java.util.ArrayList;

import security.ActionAuthenticator;

import java.text.ParseException;


public class UsuarioController extends Controller {

  @Inject FormFactory formFactory;

  // Play injecta un usuarioService junto con todas las dependencias necesarias:
  // UsuarioRepository y JPAApi
  @Inject UsuarioService usuarioService;


  public Result saludo(String mensaje) {
    Usuario usuario = new Usuario();
     return ok(saludo.render("El mensaje que he recibido es: " + mensaje, usuario));
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result calendario(Long idUsuario) {
     Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
     List <Tarea> tareas = usuario.getTareas();
     /*List <String> titulos = new ArrayList();
     for(int i=0; i<tareas.size(); i++){
       titulos.add(tareas[i].getTitulo());
     }*/
     return ok(calendario.render(usuario, tareas/*, titulos*/));
  }

  public Result calendarioTareas(Long idUsuario, String mescal, String añocal){
    Logger.debug("mescal: " + mescal);
    int  mescalAux = Integer.parseInt(mescal) +1;
    int añocalAux = Integer.parseInt(añocal);
    int mescalSiguiente = mescalAux + 1;
    Logger.debug("mescalSiguiente: " + mescalSiguiente);
    if(mescalAux > 12){
      mescalAux = 1;
    }
    if(mescalSiguiente > 12){
      mescalSiguiente = 1;
    }
    Usuario usuario = usuarioService.findUsuarioPorId(idUsuario);
    List <Tarea> tareas = usuario.getTareas();
    Usuario user = new Usuario();
    List <Tarea> tareasAux = new ArrayList();
    for(Tarea tarea: tareas){
      Logger.debug("fecha: " + tarea.getFechaLimite());
      String[] arrayFecha = tarea.getFechaLimite().toString().split(" ");
      Logger.debug(arrayFecha[0]);
      String[] array = arrayFecha[0].split("-");
      String añoTarea = array[0];
      String mesTarea = array[1];
      String diaTarea = array[2];
      Logger.debug("añoTarea: " + añoTarea);
      Logger.debug("mesTarea: " + mesTarea);
      Logger.debug("diaTarea: " + diaTarea);
      if((Integer.parseInt(mesTarea) == mescalAux && Integer.parseInt(añoTarea) == añocalAux && Integer.parseInt(mesTarea) == mescalAux)){
        Logger.debug("años buenos");
        Tarea tareaAux = new Tarea();
        tareaAux.setTitulo(tarea.getTitulo());
        tareaAux.setFechaLimite(tarea.getFechaLimite());
        tareasAux.add(tareaAux);
      }
    }
    return ok(Json.toJson(tareasAux));
  }

  public Result formularioRegistro() {
    Usuario usuario = new Usuario();
     return ok(formRegistro.render(formFactory.form(Registro.class),usuario,""));
  }

  public Result registroUsuario() {
    Usuario usuario2 = new Usuario();
     Form<Registro> form = formFactory.form(Registro.class).bindFromRequest();
     if (form.hasErrors()) {
        return badRequest(formRegistro.render(form, usuario2, "Hay errores en el formulario"));
     }
     Registro datosRegistro = form.get();
     if (usuarioService.findUsuarioPorLogin(datosRegistro.login) != null) {
        return badRequest(formRegistro.render(form, usuario2, "Login ya existente: escoge otro"));
     }
     if (!datosRegistro.password.equals(datosRegistro.confirmacion)) {
        return badRequest(formRegistro.render(form,usuario2, "No coinciden la contraseña y la confirmación"));
     }
     Usuario usuario = usuarioService.creaUsuario(datosRegistro.login, datosRegistro.email, datosRegistro.password);
     return redirect(controllers.routes.UsuarioController.formularioLogin());
  }

  @Security.Authenticated(ActionAuthenticator.class)
  public Result acercaDe(){
    String connectedUserStr = session("connected");
    Long connectedUser =  Long.valueOf(connectedUserStr);
    Usuario usuario = usuarioService.findUsuarioPorId(connectedUser);
    return ok(acercaDe.render(usuario));
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
      Update datosUpdate = form.get();
      datosUpdate.fechaNacimiento.toString();
      if (form.hasErrors()) {
        return badRequest(formUpdate.render(form, "Hay errores en el formulario", id, usuario));
      }

      if (usuarioService.findUsuarioPorLogin(datosUpdate.login) != null && usuarioService.findUsuarioPorLogin(datosUpdate.login).getId() != connectedUser) {
        return badRequest(formUpdate.render(form, "Login ya existente: escoge otro", id, usuario));
      }
      if (!datosUpdate.password.equals(datosUpdate.confirmacion)) {
        return badRequest(formUpdate.render(form, "No coinciden la contraseña y la confirmación", id, usuario));
      }

      Usuario usuarioToUpdate = new Usuario(id, datosUpdate.login, datosUpdate.email,
      datosUpdate.password, datosUpdate.nombre, datosUpdate.apellidos, datosUpdate.fechaNacimiento);

      usuarioService.editaUsuario(usuarioToUpdate, id);

      return redirect(controllers.routes.UsuarioController.detalleUsuario(id));
    }
  }

  public Result formularioLogin() {
    Usuario usuario = new Usuario();
     return ok(formLogin.render(formFactory.form(Login.class),usuario, ""));
  }

  public Result loginUsuario() {
    Usuario usuario2 = new Usuario();
     Form<Login> form = formFactory.form(Login.class).bindFromRequest();
     if (form.hasErrors()) {
        return badRequest(formLogin.render(form,usuario2, "Hay errores en el formulario"));
     }
     Login login = form.get();
     Usuario usuario = usuarioService.login(login.username, login.password);
     if (usuario == null) {
        return notFound(formLogin.render(form,usuario2, "Login y contraseña no existentes"));
     } else {
        // Añadimos el id del usuario a la clave `connected` de
        // la sesión de Play
        // https://www.playframework.com/documentation/2.5.x/JavaSessionFlash
        session("connected", usuario.getId().toString());
        return redirect(controllers.routes.GestionTareasController.listaTareas(usuario.getId(), false));
     }
  }

  // Comprobamos si hay alguien logeado con @Security.Authenticated(ActionAuthenticator.class)
  // https://alexgaribay.com/2014/06/15/authentication-in-play-framework-using-java/
  @Security.Authenticated(ActionAuthenticator.class)
  public Result logout() {
    String connectedUserStr = session("connected");
    session().remove("connected");
    return redirect(controllers.routes.UsuarioController.formularioLogin());
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
