package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Form about skills in part "vos compétences" on profile page
 * @author coilliaux
 *
 */
public class ProfileSkillForm {

	@NotEmpty
	private String competenceTitle;
	@NotEmpty
	private int level;
	private List<Competence> listCompetence = new ArrayList<Competence>();
	

	public ProfileSkillForm() {
		
	}
	
	
	public String getCompetenceTitle() {
		return competenceTitle;
	}
	public void setCompetenceTitle(String competenceTitle) {
		this.competenceTitle = competenceTitle;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<Competence> getListCompetence() {
		return listCompetence;
	}
	public void setListCompetence(List<Competence> listCompetence) {
		this.listCompetence = listCompetence;
	}
	
}
