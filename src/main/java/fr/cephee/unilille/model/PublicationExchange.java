package fr.cephee.unilille.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PublicationExchange extends Publication {

	/*@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;*/
	
	
	public PublicationExchange()
	{
		
	}
}
