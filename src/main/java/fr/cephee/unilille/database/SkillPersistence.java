package fr.cephee.unilille.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Skill;

public interface SkillPersistence extends CrudRepository<Skill, Integer>{
	public List<Skill> findAll();

	public Skill findByCompetenceAndMember(Competence competence, Member memberProfile);
}
