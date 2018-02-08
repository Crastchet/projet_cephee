package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class PublicationEvent extends Publication {

	private int nbparticipant;
	private Date startevent;
	private String location;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany
	List<Member> participant = new ArrayList<Member>();
	
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


	public List<Member> getParticipant() {
		return participant;
	}


	public void setParticipant(List<Member> participant) {
		this.participant = participant;
	}


}
