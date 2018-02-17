package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Member {
	@Id
	@Column(unique=true)
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;

	private String login;
	private String firstname;
	private String lastname;
	private Date birth; //utiliser type Date de SQL ou java.util ?
	private String email;
	private String description;
	private boolean activated;		//profile activated or not
	private boolean isAdmin;
	
	@OneToMany(mappedBy="author")
	private List<Publication> listpublication = new ArrayList<Publication>();
	
	@OneToMany(mappedBy="member")
	private List<Skill> skills = new ArrayList<Skill>();
	
	public Member()
	{
		this.activated = false;
		this.isAdmin = false;
	}
	
	public Member(Integer id)
	{
		this();
		this.id = id;
	}
	
	
	public void addSkill(Skill skill) {
		this.skills.add(skill);
	}
	
	public List<Publication> getListpublication() {
		return listpublication;
	}
	public void setListpublication(List<Publication> listpublication) {
		this.listpublication = listpublication;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public List<Skill> getSkills() {
		return skills;
	}
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	
}
