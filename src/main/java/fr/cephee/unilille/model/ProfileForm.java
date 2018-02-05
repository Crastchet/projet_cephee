package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class ProfileForm {

	@NotEmpty
	private String birth;
	@NotEmpty
	private String email;
	
	private String description;
	
	
	public ProfileForm() {
		
	}
	
	
	private List<Competence> skills = new ArrayList<Competence>();
	
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
	public List<Competence> getSkills() {
		return skills;
	}
	public void setSkills(List<Competence> skills) {
		this.skills = skills;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
