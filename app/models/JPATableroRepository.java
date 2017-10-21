package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

  public Tablero update(Tablero tablero) {
    return jpaApi.withTransaction(entityManager -> {
        Tablero actualizado = entityManager.merge(tablero);
        return actualizado;
    });
  }

  public Tablero findById(Long idTablero) {
    return jpaApi.withTransaction(entityManager -> {
        return entityManager.find(Tablero.class, idTablero);
    });
  }

  public List<Tablero> getAll() {
    return jpaApi.withTransaction(entityManager -> {
      TypedQuery<Tablero> query = entityManager.createQuery("SELECT t FROM Tablero t", Tablero.class);
      List<Tablero> results = query.getResultList();
      return results;
    });
  }
}
