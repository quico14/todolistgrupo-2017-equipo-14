package services;

import javax.inject.*;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Size;
import models.SizeRepository;
import models.Tarea;
import models.TareaRepository;
import models.Tablero;
import models.TableroRepository;


public class TableroService {
  UsuarioRepository usuarioRepository;
  TareaRepository tareaRepository;
  TableroRepository tableroRepository;
  SizeRepository sizeRepository;

  @Inject
  public TableroService(UsuarioRepository usuarioRepository, TareaRepository tareaRepository, TableroRepository tableroRepository, SizeRepository sizeRepository) {
     this.usuarioRepository = usuarioRepository;
     this.tareaRepository = tareaRepository;
     this.tableroRepository = tableroRepository;
     this.sizeRepository = sizeRepository;
  }

  public Tablero nuevoTablero(Long idUsuario, String titulo) {
     Usuario usuario = usuarioRepository.findById(idUsuario);
     if (usuario == null) {
        throw new TableroServiceException("Usuario no existente");
     }

     Tablero tablero = new Tablero(usuario, titulo);
     return tableroRepository.add(tablero);
  }

  // Devuelve la lista de tableros administrados por un usuario, ordenados por su id
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

  public Tablero addTareaSize(String stringSize, Tablero tablero) {
    Size size = sizeRepository.findByName(stringSize);
    if (size == null) {
      Size addSize = new Size(stringSize);
      size = sizeRepository.add(addSize);
    } else if (tablero.getTareaSize().contains(size)) {
      throw new TableroServiceException("Size ya asociado al tablero");
    }
    List<Size> sizes = tablero.getTareaSize();
    sizes.add(size);
    tablero.setTareaSize(sizes);
    tablero = tableroRepository.update(tablero);
    return tablero;
  }

  public Tablero removeTareaSize(Size size, Tablero tablero) {
    List<Size> sizes = tablero.getTareaSize();
    sizes.remove(size);
    tablero.setTareaSize(sizes);
    tablero = tableroRepository.update(tablero);
    return tablero;
  }

  // Devuelve los tableros en los que participa un usuario, ordenados por su id
  // (equivalente al orden de creación)
  public List<Tablero> getTableros(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new TableroServiceException("Usuario no existente");
    }
    List<Tablero> tableros = new ArrayList<Tablero>(usuario.getTableros());
    Collections.sort(tableros, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tableros;
  }

  public List<Tablero> getTablerosSinRelacion(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new TableroServiceException("Usuario no existente");
    }
    List<Tablero> tableros = tableroRepository.allTableros();
    tableros.removeAll(getTableros(idUsuario));
    tableros.removeAll(allTablerosUsuario(idUsuario));
    Collections.sort(tableros, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return tableros;
  }

  public Tablero findTableroPorId(Long id) {
    Tablero tablero = tableroRepository.findById(id);
    if (tablero == null) {
      throw new TableroServiceException("Tablero no existente");
    }
     return tablero;
  }

}
