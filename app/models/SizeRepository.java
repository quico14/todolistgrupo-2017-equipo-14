package models;

import com.google.inject.ImplementedBy;

 // Interfaz que define los métodos del UsuarioRepository
 // La anotación ImplementedBy hace que Play para resolver la
 // inyección de dependencias escoja como objeto que implementa
 // esta interfaz un objeto JPAUsuarioRepository
 @ImplementedBy(JPASizeRepository.class)
 public interface SizeRepository {
    Size add(Size size);
    Size edit(Size size);
    Size findById(Long id);
    Size findByName(String name);
 }
