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
import models.Tablero;
import models.TableroRepository;


public class TableroService {
  UsuarioRepository usuarioRepository;
  TareaRepository tareaRepository;
  TableroRepository tableroRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository, TableroRepository tableroRepository) {
     this.usuarioRepository = usuarioRepository;
     this.tareaRepository = tareaRepository;
     this.tableroRepository = tableroRepository;
  }

  public Tablero nuevoTablero(Long idUsuario, String titulo) {
     Usuario usuario = usuarioRepository.findById(idUsuario);
     if (usuario == null) {
        throw new TableroServiceException("Usuario no existente");
     }

     Tablero tablero = new Tablero(usuario, titulo);
     return tableroRepository.add(tablero);
  }

  // Devuelve la lista de tableros administrados por un usuario, ordenadas por su id
  // (equivalente al orden de creación)
  public List<Tablero> allTablerosUsuario(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new TableroServiceException("Usuario no existente");
    }
    List<Tablero> tableros = new ArrayList<Tablero>(usuario.getAdministrados());
    Collections.sort(tableros, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tableros;
  }

  public Tablero addParticipante(Long idUsuario, Tablero tablero) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new TableroServiceException("Usuario no existente");
    }
    Set<Usuario> participantes = tablero.getParticipantes();
    participantes.add(usuario);
    tablero.setParticipantes(participantes);

    return tableroRepository.update(tablero);
  }

}
