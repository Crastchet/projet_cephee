package fr.cephee.unilille.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.cephee.unilille.database.CategoryPersistence;
import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationAnnonce;
import fr.cephee.unilille.model.PublicationEvent;
import fr.cephee.unilille.model.PublicationEventForm;
import fr.cephee.unilille.model.PublicationForm;
import fr.cephee.unilille.model.PublicationProject;
import fr.cephee.unilille.model.TypePublicationWrapper;

@Controller
@SessionAttributes({ "publi" })
public class PublicationController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	Validator validator;

	@Autowired
	private PublicationPersistence datapub;

	@Autowired
	private CategoryPersistence dataCate;

	@Autowired
	private CompetencePersistence dataComp;

	@RequestMapping(value = "/formtypepublication")
	public String publicationTypeForm(@ModelAttribute TypePublicationWrapper form, Model model, HttpSession session) {
		PublicationForm publicationForm = new PublicationForm();

		model.addAttribute("publicationForm", publicationForm);
		model.addAttribute("member", session.getAttribute("member"));

		form.getPublicationList().add("Projet");
		form.getPublicationList().add("Echange");
		form.getPublicationList().add("Evenement");
		model.addAttribute("typepublicationlist", form);
		return "chooseTypePublication";
	}

	@RequestMapping(value = "/publicationpage", method = RequestMethod.POST)
	public String publicationPage(Model model, @ModelAttribute("publication") Publication publication) {
		PublicationForm publicationForm = new PublicationForm();
		model.addAttribute("publicationForm", publicationForm);
		return "publication";
	}

	@RequestMapping(value = "/checktypepublication", method = RequestMethod.GET)
	public String checkTypePublication(@RequestParam(value = "publicationType", required = true) String publicationType,
			Model model, HttpSession session) {
		List<Category> listcategory = dataCate.findAll();
		List<Competence> listcompetence = dataComp.findAll();
		model.addAttribute("member", session.getAttribute("member"));
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("competenceList", listcompetence);

		if (publicationType.equals("project")) {
			model.addAttribute("publicationForm", new PublicationForm());
			return "createProject";
		} else if (publicationType.equals("event")) {
			PublicationEventForm publicationEventForm = new PublicationEventForm();
			model.addAttribute("publicationForm", publicationEventForm);
			return "createEvent";
		} else if (publicationType.equals("exchange")) {
			model.addAttribute("publicationForm", new PublicationForm());
			return "createAnnonce";
		} else
			return "errorPage";
	}

	@RequestMapping(value = "/registerannonce", method = RequestMethod.POST)
	public String createAnnonce(@ModelAttribute("publicationForm") PublicationForm publicationForm,
			BindingResult result, Model model, HttpSession session, Errors errors) {
		publicationForm.setTypePublication("Echange");
		model.addAttribute("member", session.getAttribute("member"));
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, publicationForm, errors);

		if (result.hasErrors()) {
			for (ObjectError obj : result.getAllErrors())
				log.info(obj.toString());
			return "createAnnonce";
		}
		Date date = new Date();

		try {
			PublicationAnnonce publication = new PublicationAnnonce();
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthorised(true);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(date);
			publication.setAuthor((Member) session.getAttribute("member"));
			publication.setCategory(publicationForm.getListCategory());
			datapub.save(publication);
			model.addAttribute("publication", publication);
		} catch (Exception ex) {
			log.info(ex.toString());
			return "errorPage";
		}
		return "publication";
	}

	@RequestMapping(value = "/registerproject", method = RequestMethod.POST)
	public String createProject(@ModelAttribute("publicationForm") PublicationForm publicationForm,
			BindingResult result, Model model, HttpSession session, Errors errors) {
		model.addAttribute("member", session.getAttribute("member"));
		publicationForm.setTypePublication("Projet");
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, publicationForm, errors);

		if (result.hasErrors()) {
			for (ObjectError obj : result.getAllErrors())
				log.info(obj.toString());
			return "createProject";
		}
		Date date = new Date();

		try {
			PublicationProject publication = new PublicationProject();
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthorised(true);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(date);
			publication.setAuthor((Member) session.getAttribute("member"));
			for (Category cat : publicationForm.getListCategory())
			{
				log.info("category added : " + cat.getTitle());
			}
			publication.setCategory(publicationForm.getListCategory());
			publication.setListcompetence(publicationForm.getListCompetence());
			datapub.save(publication);
			model.addAttribute("publication", publication);
		} catch (Exception ex) {
			log.info(ex.toString());
			return "errorPage";
		}
		return "publication";
	}

	@RequestMapping(value = "/registerevent", method = RequestMethod.POST)
	public String createEvent(@ModelAttribute("publicationForm") PublicationEventForm publicationForm,
			BindingResult result, Model model, HttpSession session, Errors errors) {
		publicationForm.setTypePublication("Evenement");
		model.addAttribute("member", session.getAttribute("member"));
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, publicationForm, errors);
		if (result.hasErrors()) {
			for (ObjectError obj : result.getAllErrors())
				log.info(obj.toString());
			return "createEvent";
		}
		Date date = new Date();

		try {
			PublicationEvent publication = new PublicationEvent();
			log.info(publicationForm.getHourstartevent());
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthorised(true);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(date);
			publication.setAuthor((Member) session.getAttribute("member"));
			publication.setCategory(publicationForm.getListCategory());
			publication.setLocation(publicationForm.getLocation());
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
			Date startd = sdf.parse(publicationForm.getStartevent() + " " + publicationForm.getHourstartevent());
			log.info(startd.toString());
			publication.setStartevent(startd);

			datapub.save(publication);
			model.addAttribute("publication", publication);
		} catch (Exception ex) {
			log.info(ex.toString());
			return "errorPage";
		}
		return "publication";
	}

	@RequestMapping("/deletepublication")
	@ResponseBody
	public String delete(@ModelAttribute("publi") Publication publication, Model model) {
		log.info("publication is => " + publication.getId() + publication.getTitle() + " " + publication.getContent());
		try {
			Publication publi = datapub.findById(publication.getId());
			log.info("INFO = " + publi.getId() + "  " + publi.getTitle() + " " + publi.getContent());
			datapub.delete(publi);
		} catch (Exception ex) {
			return "Error deleting the publication:" + ex.toString();
		}
		return "publication succesfully deleted!";
	}

	// @RequestMapping("/get-by-AuthorPublication")
	// public String getByLogin(Member author) {
	// String publiId = "";
	// try {
	// Publication publi = datamem.findByAuthor(author);
	// publiId = String.valueOf(publi.getId());
	// } catch (Exception ex) {
	// return "Publication not found";
	// }
	// return "The Publication id is: " + publiId;
	// }

	@RequestMapping("/finishedupdating")
	public String finishedUpdating(@ModelAttribute("publi") Publication publication, Model model,
			HttpSession session)
	{
		model.addAttribute("publi", publication);
		model.addAttribute("member", session.getAttribute("member"));
		
		if (publication instanceof PublicationProject) {
			return "detailsProject";
		} else if (publication instanceof PublicationAnnonce) {
			return "detailsAnnonce";
		} else if (publication instanceof PublicationEvent) {
			return "detailsEvent";
		} else
			return "errorPage";
	}

	@RequestMapping("/updateproject")
	public String updateProject(@ModelAttribute("publi") Publication publication, Model model, HttpSession session) {
		List<Category> listcategory = dataCate.findAll();
		List<Competence> listcompetence = dataComp.findAll();
		
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("competenceList", listcompetence);
		
		model.addAttribute("member", session.getAttribute("member"));
		
		PublicationForm publiForm = new PublicationForm();
		publiForm.setTitle(publication.getTitle());
		publiForm.setAuthorised(publication.isAuthorised());
		publiForm.setContent(publication.getContent());
		publiForm.setAuthor(publication.getAuthor());	
		publiForm.setListCategory(publication.getCategory());
		publiForm.setListCompetence(((PublicationProject) publication).getListcompetence());
		
		for (Category c : publiForm.getListCategory())			
			log.info("category : " + c.getTitle());
		
		model.addAttribute("publicationForm", publiForm);
		for (Competence c : publiForm.getListCompetence())			
		log.info("competence : " + c.getTitle());
		return "updateProject";
	}
	
	@RequestMapping("/updateevent")
	public String updateEvent(@ModelAttribute("publi") Publication publication, Model model, HttpSession session) {
		List<Category> listcategory = dataCate.findAll();
		
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("member", session.getAttribute("member"));
		
		return "detailsEvent";
	}
	
	@RequestMapping("/updateannonce")
	public String updateAnnonce(@ModelAttribute("publi") Publication publication, Model model, HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		
		return "detailsAnnonce";
	}
	
	@RequestMapping("/seedetailspublication")
	public String seeDetailsPublication(@RequestParam(value = "id", required = true) Integer id, Model model,
			HttpSession session) {
		Publication publi = datapub.findById(id);
		model.addAttribute("publi", publi);
		model.addAttribute("member", session.getAttribute("member"));

		if (publi instanceof PublicationProject) {
			return "detailsProject";
		} else if (publi instanceof PublicationAnnonce) {
			return "detailsAnnonce";
		} else /* if (publi instanceof PublicationEvent) */ {
			return "detailsEvent";
		}
	}
}
