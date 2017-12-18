package models;

import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(JPAEtiquetaRepository.class)
public interface EtiquetaRepository {
	public Etiqueta add(Etiqueta columna);
	public Etiqueta update(Etiqueta columna);
	public Etiqueta findById(Long idEtiqueta);
	public void delete(Long idEtiqueta);
	public List<Etiqueta> getAll();
}
