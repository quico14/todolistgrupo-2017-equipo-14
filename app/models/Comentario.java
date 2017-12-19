package models;

import models.Usuario;
import models.Tarea;
import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Comentario {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  Long id;
  private String descripcion;

  @ManyToOne
  @JoinColumn(name="creador")
  private Usuario creador;

  @ManyToOne
  @JoinColumn(name="tareaId")
  private Tarea tareaId;

  public Comentario() {}

  public Comentario(Usuario creador, Tarea tareaId, String descripcion) {
    this.descripcion = descripcion;
    this.creador = creador;
    this.tareaId = tareaId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Usuario getCreador() {
     return creador;
  }

  public void setCreador(Usuario creador) {
    this.creador = creador;
  }

  public Tarea getTareaId() {
    return tareaId;
  }

  public void setTareaId(Tarea tareaId) {
    this.tareaId = tareaId;
  }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = prime + ((descripcion == null) ? 0 : descripcion.hashCode());
      result = result + ((creador == null) ? 0 : creador.hashCode());
      result = result + ((tareaId == null) ? 0 : tareaId.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (getClass() != obj.getClass()) return false;
      Comentario other = (Comentario) obj;
      // Si tenemos los ID, comparamos por ID
      if (id != null && other.id != null)
         return ((long) id == (long) other.id);
      // sino comparamos por campos obligatorios
      else {
         if (descripcion == null) {
            if (other.descripcion != null) return false;
         } else if (!descripcion.equals(other.descripcion)) return false;
         if (creador == null) {
            if (other.creador != null) return false;
            else if (!creador.equals(other.creador)) return false;
         }
         if (tareaId == null) {
            if (other.tareaId != null) return false;
            else if (!tareaId.equals(other.tareaId)) return false;
         }
      }
      return true;
   }
}
