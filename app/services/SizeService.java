package services;

import javax.inject.*;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import models.Size;
import models.SizeRepository;


public class SizeService {
  SizeRepository repository;

  // Play proporcionará automáticamente el UsuarioRepository necesario
  // usando inyección de dependencias
  @Inject
  public SizeService(SizeRepository repository) {
     this.repository = repository;
  }

  public Size findPorNombre(String size) {
     return repository.findByName(size);
  }
}
