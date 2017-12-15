package services;

import javax.inject.*;

import java.util.List;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;
import models.Tablero;
import models.Size;
import models.TableroRepository;
import models.SizeRepository;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import play.data.format.*;


public class TareaService {
  UsuarioRepository usuarioRepository;
  TareaRepository tareaRepository;
  TableroRepository tableroRepository;
  SizeRepository sizeRepository;

  @Inject
  public TareaService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository,
                      TableroRepository tableroRepository, SizeRepository sizeRepository) {
     this.usuarioRepository = usuarioRepository;
     this.tareaRepository = tareaRepository;
     this.tableroRepository = tableroRepository;
     this.sizeRepository = sizeRepository;
  }

  // Devuelve la lista de tareas de un usuario, ordenadas por su id
  // (equivalente al orden de creación)
  public List<Tarea> allTareasUsuario(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new TareaServiceException("Usuario no existente");
    }
    List<Tarea> tareas = usuario.getTareas();
    Collections.sort(tareas, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tareas;
  }

  public Tarea nuevaTarea(Long idUsuario, String titulo, Date fechaLimite, Long idTablero, Long idSize) {
     Usuario usuario = usuarioRepository.findById(idUsuario);
     if (usuario == null) {
        throw new TareaServiceException("Usuario no existente");
     }
     Tablero tablero = null;
     if(idTablero != null) {
       tablero = tableroRepository.findById(idTablero);
       if (tablero == null) {
         throw new TareaServiceException("Tablero no existente");
       }
     }
     Size size = null;
     if(idSize != null) {
       size = sizeRepository.findById(idSize);
       if (size == null) {
         throw new SizeServiceException("Tamaño no existente");
       }
     }
     Tarea tarea = new Tarea(usuario, titulo);
     tarea.setFechaLimite(fechaLimite);
     tarea.setTablero(tablero);
     tarea.setSize(size);
     return tareaRepository.add(tarea);
  }

  public Tarea obtenerTarea(Long idTarea) {
     return tareaRepository.findById(idTarea);
  }

  public Tarea modificaTarea(Long idTarea, String nuevoTitulo, Date nuevaFechaLimite, Long idTablero, Long idSize) {
     Tarea tarea = tareaRepository.findById(idTarea);
     if (tarea == null)
          throw new TareaServiceException("No existe tarea");
    Tablero tablero = null;
    if(idTablero != null) {
        tablero = tableroRepository.findById(idTablero);
        if (tablero == null) {
          throw new TareaServiceException("Tablero no existente");
        }
     }
     Size size = null;
     if(idSize != null) {
       size = sizeRepository.findById(idSize);
       if (size == null) {
         throw new SizeServiceException("Tamaño no existente");
       }
     }
     tarea.setTitulo(nuevoTitulo);
     tarea.setFechaLimite(nuevaFechaLimite);
     tarea.setTablero(tablero);
     tarea.setSize(size);
     tarea = tareaRepository.update(tarea);
     return tarea;
  }

  public Tarea cambiaTerminada(Long idTarea) {
    Tarea tarea = tareaRepository.findById(idTarea);
    if (tarea == null) {
      throw new TareaServiceException("No existe tarea");
    }
    tarea.setTerminada(!tarea.getTerminada());

    tarea = tareaRepository.update(tarea);
    return tarea;
  }

  public void borraTarea(Long idTarea) {
     Tarea tarea = tareaRepository.findById(idTarea);
     if (tarea == null)
          throw new TareaServiceException("No existe tarea");
     tareaRepository.delete(idTarea);
  }
}
