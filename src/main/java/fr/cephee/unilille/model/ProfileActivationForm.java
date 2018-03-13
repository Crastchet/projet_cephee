package fr.cephee.unilille.model;

import org.hibernate.validator.constraints.NotEmpty;

public class ProfileActivationForm {

	@NotEmpty
	private String displayname;
	@NotEmpty
	private String email;
	@NotEmpty
	private String description;
	
	
	public ProfileActivationForm() {
		
	}

	
	public String getDisplayname() {
		return this.displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
