package fr.cephee.unilille.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
		model.addAttribute("member", session.getAttribute("member"));
		PublicationForm publicationForm = new PublicationForm();

	//	publicationForm.setListCategory(listcategory);
//		publicationForm.setListCompetence(listcompetence);
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("competenceList", listcompetence);
		model.addAttribute("publicationForm", publicationForm);
		return "research";
	}
	
	@RequestMapping(value = "/searching")
	public String searching(@ModelAttribute("publicationForm") PublicationForm publicationForm,
			Model model,			
			HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		
		List<Publication> titleSearched = new ArrayList<Publication>();
		List<Publication> finalResult = new ArrayList<Publication>();
		List<Publication> categorySearched = new ArrayList<Publication>();
		
		if (!publicationForm.getTitle().isEmpty())
			titleSearched = datapub.findByTitleContaining(publicationForm.getTitle());		

		if (!publicationForm.getListCategory().isEmpty())
			categorySearched = datapub.findByCategoryIn(publicationForm.getListCategory());
		
		
		for (Publication p : titleSearched)
		{
			log.info("Publication title search : " + p.getId() + p.getTitle());
		}
		for (Publication pc : categorySearched)
		{
			log.info("Publication category search : " + pc.getId() + pc.getTitle());
			for (Category c : pc.getCategory())
			log.info("Category : " + c.getTitle());
			
		}

		finalResult.addAll(titleSearched);
		for (Publication p : categorySearched)
			if (!finalResult.contains(p))
				finalResult.add(p);
		model.addAttribute("listSearched", finalResult);
		return "researchResult";
	}
}
