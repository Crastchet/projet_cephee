package fr.cephee.unilille.model;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class PublicationEvent extends Publication {

	private int nbparticipant;
	private Date startevent;
	private String location;

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


}
