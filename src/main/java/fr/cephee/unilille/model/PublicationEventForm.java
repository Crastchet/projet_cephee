package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class PublicationEventForm extends PublicationForm {
	

	private List<Category> listCategory = new ArrayList<Category>();
	
	@NotNull
	private String startevent;
	
	@NotNull
	private String endevent;
	
	@NotNull
	private String hourstartevent;
	
	@NotNull
	private String hourendevent;
	
	@NotEmpty
	private String location;

	

	public List<Category> getListCategory() {
		return listCategory;
	}

	public void setListCategory(List<Category> listCategory) {
		this.listCategory = listCategory;
	}

	public String getStartevent() {
		return startevent;
	}

	public void setStartevent(String startevent) {
		this.startevent = startevent;
	}

	public String getEndevent() {
		return endevent;
	}

	public void setEndevent(String endevent) {
		this.endevent = endevent;
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

	public String getHourendevent() {
		return hourendevent;
	}

	public void setHourendevent(String hourendevent) {
		this.hourendevent = hourendevent;
	}
	
	
}
