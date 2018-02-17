package fr.cephee.unilille.model;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Participantdata {

	@Id
	@Column(unique=true)
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	//private PublicationEvent publication;
	
	//private Member member;

	private Integer publi;
	
	private Integer mem;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
/*	public PublicationEvent getPublication() {
		return publication;
	}

	public void setPublication(PublicationEvent publication) {
		this.publication = publication;
	} */

	public Integer getPubli() {
		return publi;
	}

	public void setPubli(Integer publi) {
		this.publi = publi;
	}

	public Integer getMem() {
		return mem;
	}

	public void setMem(Integer mem) {
		this.mem = mem;
	}

	/*public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}*/
	
	
	/*private HashMap<Member, PublicationEvent> data = new HashMap<Member, PublicationEvent>();

	public HashMap<Member, PublicationEvent> getData() {
		return data;
	}

	public void setData(HashMap<Member, PublicationEvent> data) {
		this.data = data;
	}
*/
}
