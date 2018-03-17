package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;

@Transactional
public interface MemberPersistence extends CrudRepository<Member, Integer> {
	public Member findById(int id);
	public Member findByUsername(String username);
	public Member findByDisplayname(String displayname);
	
	public List<Member> findByIsActivated(boolean activated);
}
