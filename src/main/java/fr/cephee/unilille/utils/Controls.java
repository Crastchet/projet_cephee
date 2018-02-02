package fr.cephee.unilille.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.cephee.unilille.exceptions.DateFormatException;
import fr.cephee.unilille.exceptions.EmailFormatException;

public abstract class Controls {

	public static void checkEmail(String email) throws EmailFormatException {
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
}
