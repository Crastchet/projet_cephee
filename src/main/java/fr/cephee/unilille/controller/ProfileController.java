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
			@RequestParam(value="login", required=true) String login,		//The member login who asks to see his profile
			Model model) {
		Member member = datamem.findByLogin(login);
		if (member == null) {
			model.addAttribute("error_message", "Login " + login + " wasn't found in student database");
			return "error";
		}
		
		model.addAttribute("member", member);
		boolean ownProfile = false;
		if(ownProfile)
			return "profileEditable";
		else
			return "profile";
	}
	
}
