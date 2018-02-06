package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class PublicationEventForm extends PublicationForm {
	


	
	@NotNull
	private String startevent;
	
	@NotNull
	private String hourstartevent;

	
	@NotEmpty
	private String location;	


	public String getStartevent() {
		return startevent;
	}

	public void setStartevent(String startevent) {
		this.startevent = startevent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHourstartevent() {
		return hourstartevent;
	}

	public void setHourstartevent(String hourstartevent) {
		this.hourstartevent = hourstartevent;
	}
	
	
}
