package fr.cephee.unilille.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class PublicationForm {
	
	@NotEmpty
	private String title;

	@NotEmpty
	private String content;

	@NotEmpty
	private String typePublication;
	
	private List<String> typeResearch;
	
	private boolean authorised;

	private List<Category> listCategory = new ArrayList<Category>();
	
	private List<Competence> listCompetence = new ArrayList<Competence>();
	
	private Member author;
	
	
	public PublicationForm(){
	}

	
	public String getTypePublication() {
		return typePublication;
	}


	public void setTypePublication(String typePublication) {
		this.typePublication = typePublication;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Member getAuthor() {
		return author;
	}

	public void setAuthor(Member author) {
		this.author = author;
	}

	public List<Category> getListCategory() {
		return listCategory;
	}

	public void setListCategory(List<Category> listCategory) {
		this.listCategory = listCategory;
	}


	public List<Competence> getListCompetence() {
		return listCompetence;
	}


	public void setListCompetence(List<Competence> listCompetence) {
		this.listCompetence = listCompetence;
	}
	
	
	public boolean isAuthorised() {
		return authorised;
	}


	public void setAuthorised(boolean authorised) {
		this.authorised = authorised;
	}


	public List<String> getTypeResearch() {
		return typeResearch;
	}


	public void setTypeResearch(List<String> typeResearch) {
		this.typeResearch = typeResearch;
	}

	
}
