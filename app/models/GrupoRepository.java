package models;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(JPAGrupoRepository.class)
public interface GrupoRepository {
  public Grupo add(Grupo grupo);
  public Grupo update(Grupo grupo);
  public Grupo findById(Long idGrupo);
  public List<Grupo> allGrupos();
}
