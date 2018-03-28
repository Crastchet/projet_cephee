package fr.cephee.unilille.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Reports for publications
 * @author thibault
 *
 */
@Entity
public class Report {

	@Id
	@Column(unique=true)
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Integer id;
	
	private Publication publication;
	@ManyToOne
	private Member reporter;
	
	@NotEmpty
	private String title;
	private String content;
	@NotEmpty
	private Date date;
	
	
	public Report() {
		
	}
	
	
	@Override
	public String toString() {
		return "Publication : " + publication.getTitle()+ 
				"\nSignalé par : " + reporter.getDisplayname() + 
				"\nDate : " + date + 
				"\nDescription : " + content;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Publication getPublicationId() {
		return publication;
	}
	public void setPublication(Publication publication) {
		this.publication = publication;
	}
	public Member getReporter() {
		return reporter;
	}
	public void setReporter(Member reporter) {
		this.reporter = reporter;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
