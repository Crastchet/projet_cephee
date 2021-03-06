package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationEvent;

@Transactional
public interface PublicationEventPersistence extends CrudRepository<PublicationEvent, Integer> {

	//public List<Publication> findAllByParticipants(Member member);
	public Publication findById(int id);
	public Iterable<PublicationEvent> findAll();
	public void deleteById(int id);
	public List<PublicationEvent> findTop10ByOrderByDateCreationDesc();
}
