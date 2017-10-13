package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;

public class JPATableroRepository implements TableroRepository {
   JPAApi jpaApi;

   @Inject
   public JPATableroRepository(JPAApi api) {
      this.jpaApi = api;
   }

   public Tablero add(Tablero tablero) {
      return jpaApi.withTransaction(entityManager -> {
         entityManager.persist(tablero);
         entityManager.flush();
         entityManager.refresh(tablero);
         return tablero;
      });
   }
}
