package fr.cephee.unilille.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class PublicationEvent extends Publication {

	private int nbparticipant;
	private Date startevent;
	private String location;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	Set<Member> participants = new HashSet();
	
	public PublicationEvent()
	{

	}

	
	public int getNbparticipant() {
		return nbparticipant;
	}

	public void setNbparticipant(int nbparticipant) {
		this.nbparticipant = nbparticipant;
	}

	public Date getStartevent() {
		return startevent;
	}

	public void setStartevent(Date startevent) {
		this.startevent = startevent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


	public Set<Member> getParticipants() {
		return participants;
	}


	public void setParticipants(Set<Member> participants) {
		this.participants = participants;
	}


}
