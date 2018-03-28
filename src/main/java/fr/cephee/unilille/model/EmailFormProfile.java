package fr.cephee.unilille.model;

import org.hibernate.validator.constraints.NotEmpty;

public class EmailFormProfile {

	@NotEmpty
	private String object;
	@NotEmpty
	private String content;
	@NotEmpty
	private int receiverId;
	
	
	public EmailFormProfile() {
		
	}
	
	
	public String getObject() {
		return this.object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	
}
