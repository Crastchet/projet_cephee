package fr.cephee.unilille.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;

@Transactional
public interface PublicationPersistence extends CrudRepository<Publication, Integer> {
	public Publication findById(int id);
	public Publication findByAuthor(Member author);
}
