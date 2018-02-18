package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;

@Transactional
public interface PublicationPersistence extends CrudRepository<Publication, Integer> {
	public Publication findById(int id);
	public List<Publication> findByTitleContaining(String title);
	public List<Publication> findByCategoryIn(List<Category> category);
	public List<Publication> findByAuthor(Member author);
	public List<Publication> findTop10ByOrderByDateCreationDesc();
	@Query("from Publication u where u.authorised = true and u.author.id != :idmember order by date_creation DESC")
	public List<Publication> findTop10ByOrderByDateCreationDescByAuthorisedTrue(@Param("idmember") int id);
	
}