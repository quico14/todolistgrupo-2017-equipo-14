package services;

import javax.inject.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import models.Usuario;
import models.UsuarioRepository;


public class UsuarioService {
  UsuarioRepository repository;

  // Play proporcionará automáticamente el UsuarioRepository necesario
  // usando inyección de dependencias
  @Inject
  public UsuarioService(UsuarioRepository repository) {
     this.repository = repository;
  }

  public Usuario creaUsuario(String login, String email, String password) {
    if (repository.findByLogin(login) != null) {
      throw new UsuarioServiceException("Login ya existente");
    }
     Usuario usuario = new Usuario(login, email);
     usuario.setPassword(password);
     return repository.add(usuario);
  }

  public Usuario editaUsuario(Usuario usuario, Long id) throws ParseException {
    if ((repository.findByLogin(usuario.getLogin()) != null) && (!usuario.getLogin().equals(repository.findById(id).getLogin()))) {
      throw new UsuarioServiceException("Login ya existente");
    }
    if (usuario.getFechaNacimiento() != null) {
      System.out.println(usuario.getFechaNacimiento().toString());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Date fecha = sdf.parse("1900-01-01");
      if (usuario.getFechaNacimiento().before(fecha)) {
        throw new UsuarioServiceException("La fecha de nacimiento tiene que ser posterior al año 1900");
      }
    }

     return repository.edit(usuario);
  }
  public Usuario findUsuarioPorLogin(String login) {
     return repository.findByLogin(login);
  }

  public Usuario findUsuarioPorId(Long id) {
     return repository.findById(id);
  }


  public Usuario login(String login, String password) {
     Usuario usuario = repository.findByLogin(login);
     if (usuario != null && usuario.getPassword().equals(password)) {
        return usuario;
     } else {
        return null;
     }
  }
}
