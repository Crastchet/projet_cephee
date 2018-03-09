package fr.cephee.unilille.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fr.cephee.unilille.model.Participantdata;

public interface ParticipantDataPersistence extends CrudRepository<Participantdata, Integer>{
	
	@Query("FROM Participantdata u where u.mem = :memid and u.publi = :pubid")
	public Participantdata findByMemByPubli(@Param("memid") Integer mem, @Param("pubid")Integer pub);
}
