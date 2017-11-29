package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JPAColumnaRepository implements ColumnaRepository {
	JPAApi jpaApi;

	@Inject
	public JPAColumnaRepository(JPAApi api) {
		this.jpaApi = api;
	}

	public Columna add(Columna columna) {
		return jpaApi.withTransaction(entityManager -> {
			entityManager.persist(columna);
			entityManager.flush();
			entityManager.refresh(columna);
			return columna;
		});
	}

	public Columna update(Columna columna) {
		return jpaApi.withTransaction(entityManager -> {
			Columna actualizado = entityManager.merge(columna);
			return actualizado;
		});
	}

	public Columna findById(Long idColumna) {
		return jpaApi.withTransaction(entityManager -> {
			return entityManager.find(Columna.class, idColumna);
		});
	}

	public void delete(Long idColumna) {
		jpaApi.withTransaction(() -> {
			EntityManager entityManager = jpaApi.em();
			Columna columnaBD = entityManager.getReference(Columna.class, idColumna);
			entityManager.remove(columnaBD);
		});
	}

	public List<Columna> getAll() {
		return jpaApi.withTransaction(entityManager -> {
			TypedQuery<Columna> query = entityManager.createQuery("SELECT c FROM Columna c", Columna.class);
			List<Columna> results = query.getResultList();
			return results;
		});
	}
}
