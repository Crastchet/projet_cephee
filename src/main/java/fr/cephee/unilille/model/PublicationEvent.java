package fr.cephee.unilille.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PublicationEvent extends Publication {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int idpubli;
	private int nbparticipant;
	private Date startevent;
	private Date endevent;
	private String location;

	public PublicationEvent()
	{

	}

	public int getIdpubli() {
		return idpubli;
	}

	public void setIdpubli(int idpubli) {
		this.idpubli = idpubli;
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

	public Date getEndevent() {
		return endevent;
	}

	public void setEndevent(Date endevent) {
		this.endevent = endevent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


}
