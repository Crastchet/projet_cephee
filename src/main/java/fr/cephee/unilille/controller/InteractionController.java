package fr.cephee.unilille.controller;

import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.EmailForm;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;


@Controller
public class InteractionController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PublicationPersistence datapub;
	
	
	
	@RequestMapping(value = "/getsendemail")
	public String getSendEmail() { //privilégier javascript
		return null;
	}
	
	@RequestMapping(value = "/sendemail")
	public String sendEmail(@ModelAttribute("emailForm") EmailForm emailForm, Model model, HttpSession session) {
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
		
		
		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

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
			Transport.send(message);
			model.addAttribute("displaymessage", "Sent email successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

		return "errorPae";
	}
}
