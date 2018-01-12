package fr.cephee.unilille.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Publication implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String title;
	private String content;
	private Calendar dateCreation; 
	@ManyToOne
	private Member author;

	@ManyToMany
	private List<Category> category = new ArrayList<Category>();	

	private boolean authorised = true;
	
	private String formatted = "";
	
	public void setDateCreation(Calendar dateCreation) {
		this.dateCreation = dateCreation;
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		formatted = format1.format(dateCreation.getTime());
	}

	public Publication() {
		
	}
	
	
	public String getFormatted() {
		return formatted;
	}

	public void setFormatted(String formatted) {
		this.formatted = formatted;
	}

	public Publication(int id) {
		this.id = id;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public List<Category> getCategory() {
		return category;
	}

	public void setCategory(List<Category> category) {
		this.category = category;
	}

	public boolean isAuthorised() {
		return authorised;
	}

	public void setAuthorised(boolean authorised) {
		this.authorised = authorised;
	}

	public Calendar getDateCreation() {
		return dateCreation;
	} 
	
	
}
