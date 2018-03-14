package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;

public class ProfileForm {

	@NotEmpty
	private String birth;
	@NotEmpty
	private String email;
	@NotEmpty
	private String description;
	
	private String competenceTitle;
	
	private List<Skill> skills = new ArrayList<Skill>();
	
	private List<Competence> listCompetence = new ArrayList<Competence>();
	
	public ProfileForm() {
		
	}
		
	public String getBirth() {
		return this.birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompetenceTitle() {
		return competenceTitle;
	}
	public void setCompetenceTitle(String competenceTitle) {
		this.competenceTitle = competenceTitle;
	}
	public List<Skill> getSkills() {
		return skills;
	}
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Competence> getListCompetence() {
		return listCompetence;
	}

	public void setListCompetence(List<Competence> listCompetence) {
		this.listCompetence = listCompetence;
	}

}
