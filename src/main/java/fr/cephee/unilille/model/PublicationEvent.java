package fr.cephee.unilille.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

//	@JoinTable(name = "member_publication",
	//joinColumns = @JoinColumn(name = "publi_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "mem_id", referencedColumnName = "id", updatable = true))
//	@LazyCollection(LazyCollectionOption.FALSE)
	//@ManyToMany
	
	//private List<Member> participants = new ArrayList<Member>();
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		      name="event_participate",
		      joinColumns=@JoinColumn(name="event_id", referencedColumnName="id"),
		      inverseJoinColumns=@JoinColumn(name="mem_id", referencedColumnName="id")
            )
	private Set<Member> participants = new HashSet<Member>();
	

	public PublicationEvent() {

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

	/*public List<Member> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Member> participants) {
		this.participants = participants;
	}*/

	
}
