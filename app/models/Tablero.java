package models;

import models.Usuario;
import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Tablero {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  Long id;
  private String nombre;

  @ManyToOne
  @JoinColumn(name="administradorId")
  private Usuario administrador;
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name="Persona_Tablero")
  private Set<Usuario> participantes = new HashSet<Usuario>();

  // Relación uno-a-muchos entre tablero y tarea
  @OneToMany(mappedBy="tablero", fetch=FetchType.EAGER)
  private Set<Tarea> tareas = new HashSet<Tarea>();

  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(name="Size_Tablero")
  private Set<Size> tareaSize = new HashSet<Size>();

  public Tablero() {}

  public Tablero(Usuario administrador, String nombre) {
    this.nombre = nombre;
    this.administrador = administrador;
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

  public Usuario getAdministrador() {
     return administrador;
  }

  public void setAdministrador(Usuario usuario) {
    this.administrador = administrador;
  }

  public Set<Usuario> getParticipantes() {
      return participantes;
   }

   public void setParticipantes(Set<Usuario> participantes) {
      this.participantes = participantes;
   }

   public List<Tarea> getTareas() {
     List<Tarea> list = new ArrayList<Tarea>(tareas);
      return list;
   }

   public void setTareas(List<Tarea> tareas) {
      this.tareas = new HashSet<Tarea>(tareas);
   }

   public List<Size> getTareaSize() {
     List<Size> list = new ArrayList<Size>(tareaSize);
      return list;
   }

   public void setTareaSize(List<Size> tareaSize) {
      this.tareaSize = new HashSet<Size>(tareaSize);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = prime + ((nombre == null) ? 0 : nombre.hashCode());
      result = result + ((administrador == null) ? 0 : administrador.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (getClass() != obj.getClass()) return false;
      Tablero other = (Tablero) obj;
      // Si tenemos los ID, comparamos por ID
      if (id != null && other.id != null)
         return ((long) id == (long) other.id);
      // sino comparamos por campos obligatorios
      else {
         if (nombre == null) {
            if (other.nombre != null) return false;
         } else if (!nombre.equals(other.nombre)) return false;
         if (administrador == null) {
            if (other.administrador != null) return false;
            else if (!administrador.equals(other.administrador)) return false;
         }
      }
      return true;
   }
}
