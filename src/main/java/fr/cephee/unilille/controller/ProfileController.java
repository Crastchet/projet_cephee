package fr.cephee.unilille.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.model.Member;

@Controller
public class ProfileController {

	@Autowired
	private MemberPersistence datamem;
	
	
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
		boolean memberSession = member.getLogin().equals( ((Member)session.getAttribute("member")).getLogin() ); //on pourrait faire des equals entre Member, méthode à redéfinir ?
		//FAIRE LE IF LOGIN DOESN'T EXIST etc
		model.addAttribute("member", member);
		
		//If it is my Profile
		if( memberSession ) {
			//If profile is not activated - we suggest to activate
			if( member.getActivated() == false ) {
				//TOUJOURS MËME QUESTION : UNE VUE ou PLEIN DE VUES ?
				model.addAttribute("display_activate-button", true); //parler de cette convention de nommage
			}
			//If it is activated - we don't suggest to activate
			else {
				model.addAttribute("display_activate-button", false); //parler de cette convention de nommage
				model.addAttribute("display_editable-buttons", true);
			}
			return "profile";
		}
		
		//If it is not my Profile
		//If profile is not activated - ???
		if( member.getActivated() == false ) {
			//TOUJOURS MËME QUESTION : UNE VUE ou PLEIN DE VUES ?
			model.addAttribute("active", false); //parler de cette convention de nommage
		}
		//If it is activated - ???
		return "profile";
		
		
		
		//ICI DECIDER SI ON FAIT 2 VUES DIFFRENTES POUR LA PAGE PROFILE ET PROFILE PERSO OU SI ON EN FAIT QU'UNE
		//A CHECKER EN FONCTION DES MAQUETTES QU'ON A FAITES
		
		/** VERSION UNE SEULE VUE **/
		//If profile is activated
		if( ((Member)session.getAttribute("member")).getLogin().equals(login) ) {
			//If it is session profile - we add editable buttons
			if( ((Member)session.getAttribute("member")).getLogin().equals(login) )
				model.addAttribute("display_editable-buttons", true); //parler de cette convention de nommage
			//If it is not session profile - we don't add editable buttons
			else
				model.addAttribute("display_editable-buttons", false); //parler de cette convention de nommage
			return "profile";
		}
		
		/** VERSION DEUX VUES **/
		//If profile is activated
		if( ((Member)session.getAttribute("member")).getLogin().equals(login) ) {
			//If it is session profile - we add editable buttons
			if( ((Member)session.getAttribute("member")).getLogin().equals(login) )
				return "myProfile";
			//If it is not session profile - we don't add editable buttons
			else
				return "profile";
		}
		
		if (member == null) {
			model.addAttribute("error_message", "Profile of " + login + " wasn't found in student database");
			return "errorPage";
		}
		
		model.addAttribute("member", member);
		boolean ownProfile = true;
		if(ownProfile)
			return "profilePersonnal";
		else
			return "profile";
	}
	
}
