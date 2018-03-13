package fr.cephee.unilille.model;

import org.hibernate.validator.constraints.NotEmpty;

public class EmailForm {

	
	@NotEmpty
	private String object;
	@NotEmpty
	private int publicationId;
	@NotEmpty
	private String content;
//	@NotEmpty
//	private String receiver; //pas sûr de ça
	
	
	public EmailForm() {
		
	}
	
	
	public String getObject() {
		return this.object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public int getPublicationId() {
		return this.publicationId;
	}
	public void setPublicationId(int publicationId) {
		this.publicationId = publicationId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
//	public String getReceiver() {
//		return receiver;
//	}
//	public void setReceiver(String receiver) {
//		this.receiver = receiver;
//	}
	
}
