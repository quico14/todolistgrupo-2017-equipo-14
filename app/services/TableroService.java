package services;

import javax.inject.*;

import java.util.List;
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
}
