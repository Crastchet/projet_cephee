package fr.cephee.unilille.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.exceptions.DateFormatException;
import fr.cephee.unilille.exceptions.EmailFormatException;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.ProfileForm;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationForm;
import fr.cephee.unilille.utils.Controls;

@Controller
public class ProfileController {

	@Autowired
	private MemberPersistence datamem;
	
	@Autowired
	private PublicationPersistence datapub;
	
	
	/**
	 * - if /profile - return session profile
	 * - if /profile?login=mylogin - return mylogin profile
	 * @param login
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/profile")
	public String profile(
			@RequestParam(value="login", required=false) String login,		//The member login who asks to see his profile
			Model model,
			HttpSession session) {
		
		//If no login is specified, return session profile
		if(login == null)
			return this.profile(
					((Member)session.getAttribute("member")).getLogin(), 
					model, 
					session);
		
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)session.getAttribute("member")).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		
		
		model.addAttribute("member", member);
		
		//If it is my Profile
		if( itIsMemberSession ) {
			//If profile is not activated - we suggest to activate
			if( member.getActivated() == false )
				return "profilePersonnal-NotActivated";				    //parler de cette convention de nommage
			//If it is activated - we don't suggest to activate
			else {
				this.addProfilePublications(member, model);
				return "profilePersonnal";
			}
		}
		
		//If it is not my Profile
		return "profileMember";
	}
	
	
	private void addProfilePublications(Member member, Model model) {
		List<Publication> publications = datapub.findByAuthor(member);
		model.addAttribute("publications", publications);
	}
	
	@RequestMapping(value = "/editprofile", method = RequestMethod.GET)
	public String editProfile(
			Model model,
			HttpSession session) {
		Member member = (Member)session.getAttribute("member");
		model.addAttribute("member", member);
		model.addAttribute("profileForm", new ProfileForm());
		return "profileEdit";
	}
	
	@RequestMapping(value = "/registerprofileedition", method = RequestMethod.POST)
	public String registerProfileEdition(
			@RequestParam(value="login", required=false) String login,
			@ModelAttribute("profileForm") ProfileForm profileForm,
			Model model,
			HttpSession session) {
		
		//If no login is specified, recall for session profile
				if(login == null)
					return this.registerProfileEdition(
							((Member)session.getAttribute("member")).getLogin(),
							profileForm,
							model, 
							session);
		
		Member member = datamem.findByLogin(login);
		boolean itIsMemberSession = member.getLogin().equals( ((Member)session.getAttribute("member")).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		

		//If try to edit own profile OR If I am an admin
		if( itIsMemberSession || ((Member)session.getAttribute("member")).getIsAdmin() ) {
			try {
				Controls.checkEmail(profileForm.getEmail());
				member.setEmail(profileForm.getEmail());
				Controls.checkDate(profileForm.getBirth());
				member.setBirth(new SimpleDateFormat("yy-mm-dd").parse(profileForm.getBirth()));
				//peut être contrôler également la description ??
				member.setDescription(profileForm.getDescription());
				
				datamem.save(member);
			} catch (EmailFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DateFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block (j'aimerais l'enlever mais bon..)
				e.printStackTrace();
			}
		}
		
		//We return member profile
		return this.profile(login, model, session);
	}
	
}
