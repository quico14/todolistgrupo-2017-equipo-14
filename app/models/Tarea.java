package models;

import java.util.Date;
import javax.persistence.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import play.data.format.*;

@Entity
public class Tarea {
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String titulo;
  private boolean terminada;
  @Formats.DateTime(pattern="dd-MM-yyyy HH:mm:ss") // para el formulario
  private Date fechaCreacion;
  @Formats.DateTime(pattern="dd-MM-yyyy") // para el formulario
  private Date fechaLimite;
  // Relación muchos-a-uno entre tareas y usuario
  @ManyToOne
  // Nombre de la columna en la BD que guarda físicamente
  // el ID del usuario con el que está asociado una tarea
  @JoinColumn(name="usuarioId")
  public Usuario usuario;
  public String size;

  // Relación muchos-a-uno entre tareas y tablero
  @ManyToOne
  // Nombre de la columna en la BD que guarda físicamente
  // el ID del tablero con el que está asociado una tarea
  @JoinColumn(name="tableroId")
  public Tablero tablero;

  public Tarea() {}

  public Tarea(Usuario usuario, String titulo) {
     this.usuario = usuario;
     this.titulo = titulo;
     this.fechaCreacion = new Date();
  }

  // Getters y setters necesarios para JPA

  public Long getId() {
     return id;
  }

  public void setId(Long id) {
     this.id = id;
  }

  public String getTitulo() {
     return titulo;
  }

  public void setTitulo(String titulo) {
     this.titulo = titulo;
  }

  public boolean getTerminada() {
     return terminada;
  }

  public void setTerminada(boolean terminada) {
     this.terminada = terminada;
  }

  public Date getFechaCreacion() {
     return fechaCreacion;
  }

  public void setFechaCreacion(Date fechaCreacion) {
     this.fechaCreacion = fechaCreacion;
  }

  public Date getFechaLimite() {
     return fechaLimite;
  }

  public void setFechaLimite(Date fechaLimite) {
     this.fechaLimite = fechaLimite;
  }

  public Usuario getUsuario() {
     return usuario;
  }

  public void setUsuario(Usuario usuario) {
     this.usuario = usuario;
  }

  public Tablero getTablero() {
     return tablero;
  }

  public void setTablero (Tablero tablero) {
     this.tablero = tablero;
  }

  public String getSize() {
     return size;
  }

  public void setSize (String size) {
     this.size = size;
  }

  public String toString() {
     return String.format("Tarea id: %s titulo: %s usuario: %s",
                     id, titulo, usuario.toString());
  }

  @Override
  public int hashCode() {
     final int prime = 31;
     int result = prime + ((titulo == null) ? 0 : titulo.hashCode());
     return result;
  }

  @Override
  public boolean equals(Object obj) {
     if (this == obj) return true;
     if (getClass() != obj.getClass()) return false;
     Tarea other = (Tarea) obj;
     // Si tenemos los ID, comparamos por ID
     if (id != null && other.id != null)
     return ((long) id == (long) other.id);
     // sino comparamos por campos obligatorios
     else {
        if (titulo == null) {
           if (other.titulo != null) return false;
        } else if (!titulo.equals(other.titulo)) return false;
        if (usuario == null) {
           if (other.usuario != null) return false;
           else if (!usuario.equals(other.usuario)) return false;
        }
     }
     return true;
  }
}
