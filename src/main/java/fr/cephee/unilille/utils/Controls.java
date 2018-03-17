package fr.cephee.unilille.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.cephee.unilille.exceptions.CompetenceTitleException;
import fr.cephee.unilille.exceptions.DateFormatException;
import fr.cephee.unilille.exceptions.DescriptionException;
import fr.cephee.unilille.exceptions.DisplaynameFormatException;
import fr.cephee.unilille.exceptions.EmailFormatException;

public abstract class Controls {
	
	public static final int DESCRIPTION_SIZE_MAX = 500;
	public static final int DESCRIPTION_SIZE_MIN = 50;

	public static void checkEmail(String email) throws EmailFormatException {
		if(email.isEmpty())
			throw new EmailFormatException("Email vide");
		
		String part2 = email.split("@")[1];
		if(part2.split("\\.").length != 2)
			throw new EmailFormatException("Email non conforme");
	}
	
	public static void checkDate(String dateString) throws DateFormatException { //un peu useless au final
		Date date;
		try {
			date = new SimpleDateFormat("yy-mm-dd").parse(dateString);
			//If date is posterior to today
			if(date.after(new Date()))
				throw new DateFormatException("Date de naissance ne peut pas être futuriste");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new DateFormatException(e.getMessage());
		}
	}
	
	public static void checkDescription(String description) throws DescriptionException {
		String realDescription = description.replace(" ", "");
		System.out.println(realDescription);
		if(realDescription.length() > DESCRIPTION_SIZE_MAX)
			throw new DescriptionException("Description ne peut pas être plus grande que " + DESCRIPTION_SIZE_MAX + " caractères");
		if(realDescription.length() < DESCRIPTION_SIZE_MIN)
			throw new DescriptionException("Description ne peut pas être plus petite que " + DESCRIPTION_SIZE_MIN + " caractères");
	}
	
	public static void checkCompetenceTitle(String competenceTitle) throws CompetenceTitleException {
		if(competenceTitle.length() == 0)
			throw new CompetenceTitleException("Competence ne peut pas être vide");
	}

	public static void checkDisplayname(String displayname) throws DisplaynameFormatException {
		if(displayname.isEmpty())
			throw new DisplaynameFormatException("Pseudo vide");
		
		if(displayname.length() < 3)
			throw new DisplaynameFormatException("Pseudo trop court (" + displayname.length() + ")");
		
		if(displayname.length() > 10)
			throw new DisplaynameFormatException("Pseudo trop long (" + displayname.length() + ")");
		
		if(displayname.matches("[0-9]+"))
			throw new DisplaynameFormatException("Pseudo contient un/des chiffre(s)");
	}
	
}
