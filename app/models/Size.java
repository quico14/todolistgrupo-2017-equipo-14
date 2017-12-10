package models;

import models.Usuario;
import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Size {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  Long id;
  @ManyToMany(mappedBy="tareaSize", fetch=FetchType.EAGER)
  private Set<Tablero> tableros = new HashSet<Tablero>();
  private String nombre;

  public Size() {}

  public Size(String nombre) {
    this.nombre = nombre;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = prime + ((nombre == null) ? 0 : nombre.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (getClass() != obj.getClass()) return false;
      Size other = (Size) obj;
      // Si tenemos los ID, comparamos por ID
      if (id != null && other.id != null)
         return ((long) id == (long) other.id);
      // sino comparamos por campos obligatorios
      else {
         if (nombre == null) {
            if (other.nombre != null) return false;
         } else if (!nombre.equals(other.nombre)) return false;
      }
      return true;
   }
}
