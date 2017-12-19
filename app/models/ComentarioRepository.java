package models;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(JPAComentarioRepository.class)
public interface ComentarioRepository {
  public Comentario add(Comentario comentario);
  public Comentario update(Comentario comentario);
  public void delete(Long idComentario);
  public Comentario findById(Long idComentario);
  public List<Comentario> allComentarios();
}
