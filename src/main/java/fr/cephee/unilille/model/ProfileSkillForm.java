package fr.cephee.unilille.model;

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
	
}
