package fr.cephee.unilille.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Competence for one person : Competence + Level
 * @author thibault
 *
 */
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"competence_id", "member_id"})
	)

@Entity
public class Skill {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	private Competence competence;
	private int level;
	
	@ManyToOne
	private Member member;
	
	
	public Skill() {
		
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Competence getCompetence() {
		return competence;
	}
	public void setCompetence(Competence competence) {
		this.competence = competence;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	
	@Override
	public boolean equals(Object o) {
		Skill s = (Skill) o;
		return s.id == this.id;
	}
}
