package fr.cephee.unilille.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;

@Controller
public class NavigationController {

	@Autowired
	private MemberPersistence datamem;
	
	@Autowired
	private PublicationPersistence datapub;
	
	@RequestMapping(value = "/lastPubli")
	public String goToLastPublication(Model model,
			HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDescByAuthorisedTrue();
		model.addAttribute("listlasttenpub", tenLastPub);
		
		return "lastPublication";
	}
	
	@RequestMapping(value = "/research")
	public String goToResearch(Model model,
			HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		
		return "research";
	}
}
