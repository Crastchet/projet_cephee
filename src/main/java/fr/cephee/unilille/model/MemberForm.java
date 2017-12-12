package fr.cephee.unilille.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class MemberForm {
	
	@NotNull(message = "Please enter a login")
	@NotEmpty(message = "Please enter a login")
	private String login;
	
	@NotNull(message  = "Utilisateur non trouvé")
	private Member member;
	
	//private String password;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	/*public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}*/

	
	public MemberForm()
	{
		
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
}
