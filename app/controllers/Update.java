package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import play.data.format.*;

import play.data.validation.Constraints;

// Usamos los constraints para que se validen automáticamente
// en el formulario: https://www.playframework.com/documentation/2.5.x/JavaForms
public class Update {
  @Constraints.Required
  public String login;
  @Constraints.Required
  public String email;
  public String password;
  public String confirmacion;
  public String nombre;
  public String apellidos;
  @Formats.DateTime(pattern="yyyy-MM-dd") // para el formulario
  public Date fechaNacimiento;
}
