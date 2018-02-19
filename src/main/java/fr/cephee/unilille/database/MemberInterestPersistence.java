package fr.cephee.unilille.database;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.MemberInterest;

public interface MemberInterestPersistence extends CrudRepository<MemberInterest, Integer> {

	public MemberInterest findByMember(Member member);
}
