package fr.cephee.unilille.controller;

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
	
	@RequestMapping("/profile")
	public String profile(
			@RequestParam(value="login", required=false) String login,		//The member login who asks to see his profile
			Model model) {
		if(login == null)
			login = "thibs"; // à remplacer avec systeme de session
		Member member = datamem.findByLogin(login);
		if (member == null) {
			model.addAttribute("error_message", "Profile of " + login + " wasn't found in student database");
			return "errorPage";
		}
		
		model.addAttribute("member", member);
		boolean ownProfile = false;
		if(ownProfile)
			return "profileEditable";
		else
			return "profile";
	}
	
}
