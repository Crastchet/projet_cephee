package fr.cephee.unilille.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.CategoryPersistence;
import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationForm;

@Controller
public class NavigationController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CategoryPersistence dataCate;

	@Autowired
	private CompetencePersistence dataComp;
	
	@Autowired
	private MemberPersistence datamem;
	
	@Autowired
	private PublicationPersistence datapub;
	
	@RequestMapping(value = "/lastPubli")
	public String goToLastPublication(Model model,
			HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		Member memb = (Member) session.getAttribute("member");
		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDescByAuthorisedTrue(memb.getId());
		model.addAttribute("listlasttenpub", tenLastPub);
		
		return "lastPublication";
	}
	
	@RequestMapping(value = "/research")
	public String goToResearch(Model model,
			HttpSession session) {
		List<Category> listcategory = dataCate.findAll();
		List<Competence> listcompetence = dataComp.findAll();
		log.info("ENTERED RESEARCH");
		model.addAttribute("member", session.getAttribute("member"));
		//model.addAttribute("categoryList", listcategory);
		//model.addAttribute("competenceList", listcompetence);
		PublicationForm publicationForm = new PublicationForm();

		publicationForm.setListCategory(listcategory);
		publicationForm.setListCompetence(listcompetence);
		model.addAttribute("publicationForm", publicationForm);
		return "research";
	}
	
	@RequestMapping(value = "/searching")
	public String searching(Model model,
			HttpSession session) {

		
		model.addAttribute("member", session.getAttribute("member"));

		return "researchResult";
	}
}
