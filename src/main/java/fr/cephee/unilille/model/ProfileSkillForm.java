package fr.cephee.unilille.model;

import org.hibernate.validator.constraints.NotEmpty;

public class ProfileSkillForm {

	@NotEmpty
	private String title;
	@NotEmpty
	private int level;
	
	
	public ProfileSkillForm() {
		
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
