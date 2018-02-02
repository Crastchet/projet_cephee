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
	
	public static void checkDate(String date) throws DateFormatException { //un peu useless au final
		SimpleDateFormat sdf = new SimpleDateFormat("yy-mm-dd");
		try {
			sdf.parse(date);
			if(sdf.)//antérieur ou pas à date aujourd'hui (mettre easter egg)
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new DateFormatException(e.getMessage());
		}
	}
}
