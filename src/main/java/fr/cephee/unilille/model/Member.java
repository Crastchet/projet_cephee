package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Member {

	@Id
	@Column(unique=true)
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int id;

	private String login;
	private String firstname;
	private String lastname;
	private Date birth; //utiliser type Date de SQL ou java.util ?
	private String email;
	private String description;
	private boolean activated;		//profile activated or not
	private boolean isAdmin;
	@OneToMany(mappedBy="author")
	List<Publication> listpublication = new ArrayList<Publication>();
	
	
	public Member()
	{
		this.activated = false;
		this.isAdmin = false;
	}
	
	public Member(int id)
	{
		this();
		this.id = id;
	}
	
	
	
	public List<Publication> getListpublication() {
		return listpublication;
	}

	public void setListpublication(List<Publication> listpublication) {
		this.listpublication = listpublication;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean getActivated() {
		return this.activated;
	}
	public void setActived(boolean activate) {
		this.activated = activate;
	}
	public boolean getIsAdmin() {
		return this.isAdmin;
	}
	public void setisAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
