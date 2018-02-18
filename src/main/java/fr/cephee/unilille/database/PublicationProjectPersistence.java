package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.PublicationProject;

public interface PublicationProjectPersistence  extends CrudRepository<PublicationProject, Integer> {

	public List<PublicationProject> findBylistcompetenceIn(List<Competence> competence);
}
