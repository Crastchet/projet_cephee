package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import fr.cephee.unilille.model.PublicationAnnonce;

@Transactional
public interface PublicationAnnoncePersistence  extends CrudRepository<PublicationAnnonce, Integer> {

	public List<PublicationAnnonce> findTop10ByOrderByDateCreationDesc();
}
