package fr.cephee.unilille.model;

import org.hibernate.validator.constraints.NotEmpty;

public class ReportForm {

	@NotEmpty
	private String title;
	
	private String content;
	
	
	public ReportForm() {
		
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
}
