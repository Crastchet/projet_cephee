package fr.cephee.unilille.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Notification {

	@Id
	@Column(unique=true)
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	private String content;
	
	@ManyToOne
	private Member author;
	
	@ManyToOne
	private Member memberTargeted;
	
	private Date dateCreation;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Member getAuthor() {
		return author;
	}

	public void setAuthor(Member author) {
		this.author = author;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Member getMemberTargeted() {
		return memberTargeted;
	}

	public void setMemberTargeted(Member memberTargeted) {
		this.memberTargeted = memberTargeted;
	}
	
	
}
