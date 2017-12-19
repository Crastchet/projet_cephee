package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Competence;

public interface CompetencePersistence  extends CrudRepository<Competence, Integer>{
	public List<Competence> findAll();
}
