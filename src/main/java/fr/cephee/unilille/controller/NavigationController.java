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
import fr.cephee.unilille.database.MemberInterestPersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationAnnoncePersistence;
import fr.cephee.unilille.database.PublicationEventPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.database.PublicationProjectPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.MemberInterest;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationAnnonce;
import fr.cephee.unilille.model.PublicationEvent;
import fr.cephee.unilille.model.PublicationEventForm;
import fr.cephee.unilille.model.PublicationForm;
import fr.cephee.unilille.model.PublicationProject;
import fr.cephee.unilille.model.TypePublicationWrapper;

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
	
	@Autowired
	private PublicationProjectPersistence datapubProj;
	
	@Autowired
	private MemberInterestPersistence datainterest;
	
	@Autowired
	private PublicationEventPersistence dataevent;
	
	@Autowired
	private PublicationAnnoncePersistence dataannonce;
	
	@RequestMapping(value = "/lastpublications")
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
		TypePublicationWrapper form = new TypePublicationWrapper();
		PublicationEventForm publiEventForm = new PublicationEventForm();
		
		form.getPublicationList().add("Projet");
		form.getPublicationList().add("Annonce");
		form.getPublicationList().add("Evenement");
		

		model.addAttribute("publicationEventForm", publiEventForm);
		model.addAttribute("typepublicationlist", form);
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
		//List<Publication> authorSearched = new ArrayList<Publication>();
		List<Publication> categorySearched = new ArrayList<Publication>();
		List<PublicationProject> competenceSearched = new ArrayList<PublicationProject>();
		
		if (!publicationForm.getTitle().isEmpty())
			titleSearched = datapub.findByTitleContaining(publicationForm.getTitle());		
		
		/*if (!publicationForm.getAuthor().getLogin().isEmpty())
		{
			Member m = datamem.findByLogin(publicationForm.getAuthor().getLogin());
			authorSearched = datapub.findByAuthor(m);
		}*/
		if (!publicationForm.getListCategory().isEmpty())
		{
			categorySearched = datapub.findByCategoryIn(publicationForm.getListCategory());
			MemberInterest interest = datainterest.findByMember((Member)session.getAttribute("member"));
			for (Category c : publicationForm.getListCategory())
				interest.addInterest(c);
			datainterest.save(interest);
		}		
		if (!publicationForm.getListCompetence().isEmpty())
			competenceSearched = datapubProj.findBylistcompetenceIn(publicationForm.getListCompetence());		

		finalResult.addAll(titleSearched);
		/*for (Publication p : authorSearched)
			if (!finalResult.contains(p))
				finalResult.add(p);*/
		for (Publication p : categorySearched)
			if (!finalResult.contains(p))
				finalResult.add(p);
		for (Publication p : competenceSearched)
			if (!finalResult.contains(p))
				finalResult.add(p);
		
		List<Publication> tmp = new ArrayList<Publication>(); 
		if (!publicationForm.getTypeResearch().isEmpty())
		{
			if (finalResult.isEmpty())
			{
				log.info("publication is empty");
				if (publicationForm.getTypeResearch().contains("Projet"))
					tmp.addAll(datapubProj.findTop10ByOrderByDateCreationDesc());
				if (publicationForm.getTypeResearch().contains("Evenement"))
					tmp.addAll(dataevent.findTop10ByOrderByDateCreationDesc());
				if (publicationForm.getTypeResearch().contains("Annonce"))
					tmp.addAll(dataannonce.findTop10ByOrderByDateCreationDesc());
			}
			else
			{
				for (Publication p : finalResult)
				{log.info("publication boucle");
					if (publicationForm.getTypeResearch().contains("Projet"))
						if (p instanceof PublicationProject)
							tmp.add(p);
					if (publicationForm.getTypeResearch().contains("Evenement"))
						if (p instanceof PublicationEvent)
							tmp.add(p);
					if (publicationForm.getTypeResearch().contains("Annonce"))
						if (p instanceof PublicationAnnonce)
							tmp.add(p);
				}
			}
		}
		for (Publication p : tmp)
			if (!finalResult.contains(p))
				finalResult.add(p);
		model.addAttribute("listSearched", finalResult);
		return "researchResult";
	}
}
