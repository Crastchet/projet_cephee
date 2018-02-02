package fr.cephee.unilille.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Competence implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int level;
	private String name;
	
	public Competence()
	{
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return name;
	}
	public void setDescription(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	 
}
