package services;

import javax.inject.*;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;

import models.Usuario;
import models.UsuarioRepository;
import models.Grupo;
import models.GrupoRepository;


public class GrupoService {
  UsuarioRepository usuarioRepository;
  GrupoRepository grupoRepository;

  @Inject
  public GrupoService(UsuarioRepository usuarioRepository,GrupoRepository grupoRepository) {
     this.usuarioRepository = usuarioRepository;
     this.grupoRepository = grupoRepository;
  }

  public Grupo nuevoGrupo(Long idUsuario, String titulo) {
     Usuario usuario = usuarioRepository.findById(idUsuario);
     if (usuario == null) {
        throw new GrupoServiceException("Usuario no existente");
     }

     Grupo grupo = new Grupo(usuario, titulo);
     return grupoRepository.add(grupo);
  }

  // Devuelve la lista de grupos administrados por un usuario, ordenados por su id
  // (equivalente al orden de creación)
  public List<Grupo> allGruposUsuario(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new GrupoServiceException("Usuario no existente");
    }
    List<Grupo> grupos = new ArrayList<Grupo>(usuario.getGruposAdministrados());
    Collections.sort(grupos, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return grupos;
  }

  public Grupo addParticipante(String nombreUsuario, Grupo grupo) {
    Usuario usuario = usuarioRepository.findByLogin(nombreUsuario);
    if (usuario == null) {
      throw new GrupoServiceException("Usuario no existente");
    }
    if (nombreUsuario.equals(grupo.getAdministrador().getLogin())) {
      throw new GrupoServiceException("Ese usuario ya es administrador");
    }
    System.out.println(nombreUsuario + " ++ " + grupo.getAdministrador().getLogin());
    Set<Usuario> participantes = grupo.getParticipantes();
    participantes.add(usuario);
    grupo.setParticipantes(participantes);

    return grupoRepository.update(grupo);
  }

  public Grupo removeParticipante(Long idUsuario, Grupo grupo) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new GrupoServiceException("Usuario no existente");
    }
    Set<Usuario> participantes = grupo.getParticipantes();
    participantes.remove(usuario);
    grupo.setParticipantes(participantes);

    return grupoRepository.update(grupo);
  }

  public Grupo modificaGrupo(Long idGrupo, String nombreGrupo) {
    Grupo grupo = grupoRepository.findById(idGrupo);
    if (grupo == null) {
      throw new GrupoServiceException("Grupo no existente");
    }
    grupo.setNombre(nombreGrupo);

    return grupoRepository.update(grupo);
  }

  // Devuelve los grupos en los que participa un usuario, ordenados por su id
  // (equivalente al orden de creación)
  public List<Grupo> getGrupos(Long idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario);
    if (usuario == null) {
      throw new GrupoServiceException("Usuario no existente");
    }
    List<Grupo> grupos = new ArrayList<Grupo>(usuario.getGrupos());
    Collections.sort(grupos, (a, b) -> a.getId() < b.getId() ? -1 : a.getId() == b.getId() ? 0 : 1);
    return grupos;
  }

  public Grupo findGrupoPorId(Long id) {
    Grupo grupo = grupoRepository.findById(id);
    if (grupo == null) {
      throw new GrupoServiceException("Grupo no existente");
    }
     return grupo;
  }

  public void deleteGrupo(Long idGrupo) {
    Grupo grupo = grupoRepository.findById(idGrupo);
    if (grupo == null) {
      throw new GrupoServiceException("Grupo no existente");
    }
    grupoRepository.delete(idGrupo);
  }

}
