package fr.cephee.unilille.database;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cephee.unilille.model.Participantdata;

public interface ParticipantDataPersistence extends CrudRepository<Participantdata, Integer>{
	
	@Query("FROM Participantdata u where u.mem = :memid and u.publi = :pubid")
	public Participantdata findByMemByPubli(@Param("memid") Integer mem, @Param("pubid")Integer pub);
	
	public List<Participantdata> findByPubli(Integer pub);

	@Transactional
	@Modifying
	@Query("Delete FROM Participantdata u where u.publi = :pubid")
	public void deleteByPubli(@Param("pubid") Integer pub);
	
}
