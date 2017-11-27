package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

public class JPAGrupoRepository implements GrupoRepository {
   JPAApi jpaApi;

   @Inject
   public JPAGrupoRepository(JPAApi api) {
      this.jpaApi = api;
   }

   public Grupo add(Grupo grupo) {
      return jpaApi.withTransaction(entityManager -> {
         entityManager.persist(grupo);
         entityManager.flush();
         entityManager.refresh(grupo);
         return grupo;
      });
   }

   public Grupo update(Grupo grupo) {
      return jpaApi.withTransaction(entityManager -> {
         Grupo actualizado = entityManager.merge(grupo);
         return actualizado;
      });
   }

   public Grupo findById(Long idGrupo) {
      return jpaApi.withTransaction(entityManager -> {
         return entityManager.find(Grupo.class, idGrupo);
      });
   }

   public List<Grupo> allGrupos() {
      return jpaApi.withTransaction(entityManager -> {
         TypedQuery<Grupo> query = entityManager.createQuery(
                   "select u from Grupo u", Grupo.class);
         try {
             List<Grupo> grupos = query.getResultList();
             return grupos;
         } catch (NoResultException ex) {
             return null;
         }
      });
   }

}
