package models;

import javax.inject.Inject;
import play.db.jpa.JPAApi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JPAEtiquetaRepository implements EtiquetaRepository {
	JPAApi jpaApi;

	@Inject
	public JPAEtiquetaRepository(JPAApi api) {
		this.jpaApi = api;
	}

	public Etiqueta add(Etiqueta etiqueta) {
		return jpaApi.withTransaction(entityManager -> {
			entityManager.persist(etiqueta);
			entityManager.flush();
			entityManager.refresh(etiqueta);
			return etiqueta;
		});
	}

	public Etiqueta update(Etiqueta etiqueta) {
		return jpaApi.withTransaction(entityManager -> {
			Etiqueta actualizado = entityManager.merge(etiqueta);
			entityManager.flush();
			return actualizado;
		});
	}

	public Etiqueta findById(Long idEtiqueta) {
		return jpaApi.withTransaction(entityManager -> {
			return entityManager.find(Etiqueta.class, idEtiqueta);
		});
	}

	public void delete(Long idEtiqueta) {
		jpaApi.withTransaction(() -> {
			EntityManager entityManager = jpaApi.em();
			Etiqueta etiquetaBD = entityManager.getReference(Etiqueta.class, idEtiqueta);
			entityManager.remove(etiquetaBD);
		});
	}

	public List<Etiqueta> getAll() {
		return jpaApi.withTransaction(entityManager -> {
			TypedQuery<Etiqueta> query = entityManager.createQuery("SELECT e FROM Etiqueta e", Etiqueta.class);
			List<Etiqueta> results = query.getResultList();
			return results;
		});
	}
}
