package fr.cephee.unilille.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.NotificationPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.EmailFormProfile;
import fr.cephee.unilille.model.EmailFormPublication;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Notification;
import fr.cephee.unilille.model.Publication;


@Controller
public class InteractionController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PublicationPersistence datapub;
	@Autowired
	private MemberPersistence datamem;
	@Autowired
	private NotificationPersistence datanot;
	
	
	@RequestMapping(value = "/getsendemail")
	public String getSendEmail() { //privilégier javascript
		return null;
	}
	
	@RequestMapping(value = "/sendemailfrompublication")
	public String sendEmailFromPublication(@ModelAttribute("emailForm") EmailFormPublication emailForm, Model model, HttpSession session) {
		//Controls of received data
			//peut etre toujours vérifier que le membre est activé ?
		String object = emailForm.getObject();
		int publicationId = emailForm.getPublicationId(); //toujours visible ?
		String content = emailForm.getContent();
		
		
		//Gathering data
		Member member = (Member) session.getAttribute("member");
		String senderEmail = member.getEmail();
		
		Publication publication = datapub.findById(publicationId);
		String receiverEmail = publication.getAuthor().getEmail();
		

		// Assuming you are sending email from lille1-smtp
		String host = "smtps.univ-lille1.fr";
		String port = "587";

		
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		
		// Setup server port
		properties.setProperty("mail.smtp.port", port);
		
		//Set authentication to TRUE
		properties.setProperty("mail.smtp.auth", "true");
		
		properties.setProperty("mail.smtp.starttls.enable", "true");
		
		
		// Get the default Session object.
		javax.mail.Session mailSession = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(mailSession);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(senderEmail));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

			// Set Subject: header field
			message.setSubject(object);

			// Now set the actual message
			message.setText(content);

			// Send message
			Transport.send(message, "sofian.casier", "C&?1+mur");
			model.addAttribute("displaymessage", "Sent email successfully....");
			
			Notification notification = new Notification();
			notification.setAuthor(member);
			notification.setContent("Vous avez reçu un email de la part de " + notification.getAuthor() + " concernant votre annonce " + publication.getTitle());
			notification.setMemberTargeted(publication.getAuthor());
			datanot.save(notification);
		} catch (MessagingException mex) {
			mex.printStackTrace();
			model.addAttribute("displaymessage", "Email failed....");
		}

		model.addAttribute("member", member);
		return "errorPage";
	}
	
	
	@RequestMapping(value = "/sendemailfromprofile")
	public String sendEmailFromProfile(@ModelAttribute("emailForm") EmailFormProfile emailForm, Model model, HttpSession session) {
		//Controls of received data
			//peut etre toujours vérifier que le membre est activé ?
			//vérifier le membre receiver !
		String object = emailForm.getObject();
		String content = emailForm.getContent();
		System.out.println(emailForm.getContent());
		Member memberProfile = datamem.findById(emailForm.getReceiverId());
		
		
		//Gathering data
		Member member = (Member) session.getAttribute("member");
		String senderEmail = member.getEmail();
		
		String receiverEmail = memberProfile.getEmail();

		// Assuming we are sending email from lille1-smtp
		String host = "smtps.univ-lille1.fr";
		String port = "587";

		
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		
		// Setup server port
		properties.setProperty("mail.smtp.port", port);
		
		//Set authentication to TRUE
		properties.setProperty("mail.smtp.auth", "true");
		
		properties.setProperty("mail.smtp.starttls.enable", "true");
		
		
		// Get the default Session object.
		javax.mail.Session mailSession = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(mailSession);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(senderEmail));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));

			// Set Subject: header field
			message.setSubject(object);

			// Now set the actual message
			message.setText(content);

			// Send message with sofian account
			Transport.send(message, "sofian.casier", "C&?1+mur");
			model.addAttribute("displaymessage", "Sent email successfully....");
			
			Notification notification = new Notification();
			notification.setAuthor(member);
			notification.setContent("Vous avez reçu un email de la part de " + notification.getAuthor());
			notification.setMemberTargeted(memberProfile);
			datanot.save(notification);
		} catch (MessagingException mex) {
			mex.printStackTrace();
			model.addAttribute("displaymessage", "Email failed....");
		}

		model.addAttribute("member", member);
		return "errorPage";
	}
	
}
