package services;

import javax.inject.*;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Tarea;
import models.TareaRepository;
import models.Comentario;
import models.ComentarioRepository;


public class ComentarioService {
  UsuarioRepository usuarioRepository;
  TareaRepository tareaRepository;
  ComentarioRepository comentarioRepository;

  @Inject
  public ComentarioService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository,
        ComentarioRepository comentarioRepository) {
     this.usuarioRepository = usuarioRepository;
     this.tareaRepository = tareaRepository;
     this.comentarioRepository = comentarioRepository;
  }

  public Comentario nuevoComentario(Long idUsuario, Long idTarea, String descripcion) {
     Usuario usuario = usuarioRepository.findById(idUsuario);
     if (usuario == null) {
        throw new ComentarioServiceException("Usuario no existente");
     }
     Tarea tarea = tareaRepository.findById(idTarea);
     if (tarea == null) {
        throw new ComentarioServiceException("Tarea no existente");
     }

     Comentario comentario = new Comentario(usuario, tarea, descripcion);
     return comentarioRepository.add(comentario);
  }

  // Devuelve la lista de comentarios por una tarea, ordenados por su id
  // (equivalente al orden de creación)
  public List<Comentario> allComentarios(Long idTarea) {
    Tarea tarea = tareaRepository.findById(idTarea);
    if (tarea == null) {
       throw new ComentarioServiceException("Tarea no existente");
    }
    List<Comentario> comentarios = new ArrayList<Comentario>(tarea.getComentariosRecibidos());
    Collections.sort(comentarios, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return comentarios;
  }

  public Comentario findComentarioPorId(Long id) {
    Comentario comentario = comentarioRepository.findById(id);
    if (comentario == null) {
      throw new ComentarioServiceException("Comentario no existente");
    }
     return comentario;
  }

  public Comentario modificaComentario(Long idComentario, String nuevaDescripcion) {
    Comentario comentario = comentarioRepository.findById(idComentario);
    if (comentario == null) {
      throw new ComentarioServiceException("Comentario no existente");
    }

     comentario.setDescripcion(nuevaDescripcion);
     comentario = comentarioRepository.update(comentario);
     return comentario;
  }

  public void borraComentario(Long idComentario) {
     Comentario comentario = comentarioRepository.findById(idComentario);
     if (comentario == null) {
       throw new ComentarioServiceException("Comentario no existente");
     }
     comentarioRepository.delete(idComentario);
  }

}
