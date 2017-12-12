package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

public class JPAComentarioRepository implements ComentarioRepository {
   JPAApi jpaApi;

   @Inject
   public JPAComentarioRepository(JPAApi api) {
      this.jpaApi = api;
   }

   public Comentario add(Comentario comentario) {
      return jpaApi.withTransaction(entityManager -> {
         entityManager.persist(comentario);
         entityManager.flush();
         entityManager.refresh(comentario);
         return comentario;
      });
   }

   public Comentario update(Comentario comentario) {
      return jpaApi.withTransaction(entityManager -> {
         Comentario actualizado = entityManager.merge(comentario);
         return comentario;
      });
   }

   public void delete(Long idComentario) {
      jpaApi.withTransaction(() -> {
         EntityManager entityManager = jpaApi.em();
         Comentario comentarioBD = entityManager.getReference(Comentario.class, idComentario);
         entityManager.remove(comentarioBD);
      });
   }

   public Comentario findById(Long idComentario) {
      return jpaApi.withTransaction(entityManager -> {
         return entityManager.find(Comentario.class, idComentario);
      });
   }

   public List<Comentario> allComentarios() {
      return jpaApi.withTransaction(entityManager -> {
         TypedQuery<Comentario> query = entityManager.createQuery(
                   "select u from Comentario u", Comentario.class);
         try {
             List<Comentario> comentarios = query.getResultList();
             return comentarios;
         } catch (NoResultException ex) {
             return null;
         }
      });
   }
}
