package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class PublicationProject extends Publication {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToMany
	List<Competence> listcompetence = new ArrayList<Competence>();
	
	public PublicationProject()
	{
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Competence> getListcompetence() {
		return listcompetence;
	}

	public void setListcompetence(List<Competence> listcompetence) {
		this.listcompetence = listcompetence;
	}
	
	
}
