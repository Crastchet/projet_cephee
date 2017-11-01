package fr.cephee.helloworld;

public class Greetings {
	
	private long greetings_id;
	private String  greetings, name, language;
	
	protected Greetings() {}
	
	public Greetings(long id, String greets, String nam, String lang) {
		this.greetings_id = id;
		this.greetings = greets;
		this.name = nam;
		this.language = lang;
	}
	
	@Override
	public String toString() {
		return String.format( "Greetings[greetings_id=%d, greetings='%s', name='%s', language='%s']",
                greetings_id, greetings, name, language);
	}

}
