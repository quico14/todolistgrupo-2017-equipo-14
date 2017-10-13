package models;

import models.Usuario;

public class Tablero {
  private String nombre;
  private Usuario administrador;

  public Tablero(Usuario administrador, String nombre) {
     this.nombre = nombre;
     this.administrador = administrador;
  }

  public String getNombre() {
     return nombre;
  }

  public Usuario getAdministrador() {
       return administrador;
    }
}
