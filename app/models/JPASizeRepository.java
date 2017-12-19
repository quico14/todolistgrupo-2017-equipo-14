package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;

 public class JPASizeRepository implements SizeRepository {
    // Objeto definido por Play para acceder al API de JPA
    // https://www.playframework.com/documentation/2.5.x/JavaJPA#Using-play.db.jpa.JPAApi
    JPAApi jpaApi;

    // Para usar el JPASizeRepository hay que proporcionar una JPAApi.
    // La anotación Inject hace que Play proporcione el JPAApi cuando se lance
    // la aplicación.
    @Inject
    public JPASizeRepository(JPAApi api) {
       this.jpaApi = api;
    }

    public Size add(Size size) {
       return jpaApi.withTransaction(entityManager -> {
          entityManager.persist(size);
          // Hacemos un flush y un refresh para asegurarnos de que se realiza
          // la creación en la BD y se devuelve el id inicializado
          entityManager.flush();
          entityManager.refresh(size);
          return size;
       });
    }

    public Size edit(Size size) {
       return jpaApi.withTransaction(entityManager -> {
          entityManager.merge(size);
          return size;
       });
    }

    public Size findById(Long idSize) {
       return jpaApi.withTransaction(entityManager -> {
          return entityManager.find(Size.class, idSize);
       });
    }

    public Size findByName(String nombre) {
       return jpaApi.withTransaction(entityManager -> {
          TypedQuery<Size> query = entityManager.createQuery(
                    "select u from Size u where u.nombre = :nombre", Size.class);
          try {
              Size size = query.setParameter("nombre", nombre).getSingleResult();
              return size;
          } catch (NoResultException ex) {
              return null;
          }
       });
    }
 }
