package fr.cephee.unilille.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import fr.cephee.unilille.database.MemberPersistence;
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
	private Validator validator;
	
	@Autowired
	private MemberPersistence datamem;

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
			datapub.delete(publi);
		} catch (Exception ex) {
			return "Error deleting the publication:" + ex.toString();
		}
		return "publication succesfully deleted!";
	}

	@RequestMapping("/finishedupdating")
	public String finishedUpdating(@ModelAttribute("publicationForm") PublicationForm publicationForm,
			@ModelAttribute("publi") Publication publication, BindingResult result, Model model, HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		if (result.hasErrors()) {
			for (ObjectError obj : result.getAllErrors())
				log.info(obj.toString());
		}
		if (publication instanceof PublicationProject) {
			Publication newpublication = updateProj(publicationForm, publication);
			model.addAttribute("publi", newpublication);
			datapub.save(newpublication);
			return "detailsProject";
		} else if (publication instanceof PublicationAnnonce) {
			Publication newpublication = updateAnnon(publicationForm, publication);
			model.addAttribute("publi", newpublication);
			datapub.save(newpublication);
			return "detailsAnnonce";
		} 
		else
			return "errorPage";
	}

	@RequestMapping("/finishedupdatingevent")
	public String finishedUpdatingEvent(@ModelAttribute("publicationForm") PublicationEventForm publicationForm,
			@ModelAttribute("publi") Publication publication, BindingResult result, Model model, HttpSession session)
	{
		Publication newpublication = updateEv(publicationForm, publication);
		model.addAttribute("member", session.getAttribute("member"));
		model.addAttribute("publi", newpublication);
		datapub.save(newpublication);
		return "detailsEvent";
	}
	
	@RequestMapping("/updateproject")
	public String updateProject(@ModelAttribute("publi") PublicationProject publication, Model model, HttpSession session) {
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
		publiForm.setListCompetence(publication.getListcompetence());

		model.addAttribute("publi", publication);
		model.addAttribute("publicationForm", publiForm);
		return "updateProject";
	}

	public Publication updateProj(PublicationForm publicationForm, Publication publi) {
		Date dateModification = new Date();
		try {
			PublicationProject publication = new PublicationProject();
			publication.setId(publi.getId());
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthor(publi.getAuthor());
			publication.setAuthorised(true);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(publi.getDateCreation());
			publication.setDateModification(dateModification);
			publication.setCategory(publicationForm.getListCategory());
			publication.setListcompetence(publicationForm.getListCompetence());
			return publication;
		} catch (Exception ex) {
			log.info(ex.toString());
		}
		return null;
	}

	@RequestMapping("/updateevent")
	public String updateEvent(@ModelAttribute("publi") PublicationEvent publication, Model model, HttpSession session) {
		List<Category> listcategory = dataCate.findAll();

		model.addAttribute("categoryList", listcategory);
		model.addAttribute("member", session.getAttribute("member"));

		PublicationEventForm publiForm = new PublicationEventForm();
		publiForm.setTitle(publication.getTitle());
		publiForm.setAuthorised(publication.isAuthorised());
		publiForm.setContent(publication.getContent());
		publiForm.setAuthor(publication.getAuthor());
		publiForm.setListCategory(publication.getCategory());
		publiForm.setLocation(publication.getLocation());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		calendar.setTime(publication.getStartevent());
		String formatted = format1.format(calendar.getTime());
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);

		String hh = "";
		String mm = "";
		if (hours < 10)
			hh = "0";
		if (minutes < 10)
			mm = "0";
		hh += String.valueOf(hours);
		mm += String.valueOf(hours);
		publiForm.setStartevent(formatted);
		publiForm.setHourstartevent(hh + ":" + mm);
		log.info(publiForm.getHourstartevent());
		PublicationEvent publi =  publication;
		model.addAttribute("publi", publi);
		model.addAttribute("publicationForm", publiForm);
		return "updateEvent";
	}

	public Publication updateEv(PublicationEventForm publicationForm, Publication publi) {
		Date dateModification = new Date();
		try {
			PublicationEvent publication = new PublicationEvent();
			publication.setId(publi.getId());
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthor(publi.getAuthor());
			publication.setAuthorised(true);
			publication.setLocation(publicationForm.getLocation());
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
			Date startd = sdf.parse(publicationForm.getStartevent() + " " + publicationForm.getHourstartevent());
			publication.setStartevent(startd);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(publi.getDateCreation());
			publication.setDateModification(dateModification);
			publication.setCategory(publicationForm.getListCategory());
			return publication;
		} catch (Exception ex) {
			log.info("Error update EVENT : " + ex.toString());
		}
		return null;
	}

	@RequestMapping("/updateannonce")
	public String updateAnnonce(@ModelAttribute("publi") Publication publication, Model model, HttpSession session) {
		model.addAttribute("member", session.getAttribute("member"));
		List<Category> listcategory = dataCate.findAll();

		model.addAttribute("categoryList", listcategory);
		model.addAttribute("member", session.getAttribute("member"));

		PublicationForm publiForm = new PublicationForm();
		publiForm.setTitle(publication.getTitle());
		publiForm.setAuthorised(publication.isAuthorised());
		publiForm.setContent(publication.getContent());
		publiForm.setAuthor(publication.getAuthor());
		publiForm.setListCategory(publication.getCategory());

		PublicationAnnonce publi = (PublicationAnnonce) publication;
		model.addAttribute("publicationForm", publiForm);
		model.addAttribute("publi", publi);
		return "updateAnnonce";
	}

	public Publication updateAnnon(PublicationForm publicationForm, Publication publi) {
		Date dateModification = new Date();
		try {
			PublicationAnnonce publication = new PublicationAnnonce();
			publication.setId(publi.getId());
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthor(publi.getAuthor());
			publication.setAuthorised(true);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(publi.getDateCreation());
			publication.setDateModification(dateModification);
			publication.setCategory(publicationForm.getListCategory());
			return publication;
		} catch (Exception ex) {
			log.info(ex.toString());
		}
		return null;
	}

	@RequestMapping("/publish")
	public String publish(@ModelAttribute("publi") Publication publi, Model model, HttpSession session)
	{
		model.addAttribute("member", session.getAttribute("member"));
		publi.setAuthorised(true);
		datapub.save(publi);
		if (publi instanceof PublicationProject) {
			{
				publi = (PublicationProject) publi;
				model.addAttribute("publi", publi);
			}
			return "detailsProject";
		} else if (publi instanceof PublicationAnnonce) {
			publi = (PublicationAnnonce) publi;
			model.addAttribute("publi", publi);
			return "detailsAnnonce";
		} else  {
			publi = (PublicationEvent) publi;
			model.addAttribute("publi", publi);
			return "detailsEvent";
		}
		
	}
	
	@RequestMapping("/notpublish")
	public String notPublish(@ModelAttribute("publi") Publication publi, Model model, HttpSession session)
	{
		model.addAttribute("member", session.getAttribute("member"));
		publi.setAuthorised(false);
		datapub.save(publi);
		if (publi instanceof PublicationProject) {
			{
				publi = (PublicationProject) publi;
				model.addAttribute("publi", publi);
			}
			return "detailsProject";
		} else if (publi instanceof PublicationAnnonce) {
			publi = (PublicationAnnonce) publi;
			model.addAttribute("publi", publi);
			return "detailsAnnonce";
		} else  {
			publi = (PublicationEvent) publi;
			model.addAttribute("publi", publi);
			return "detailsEvent";
		}
		
	}
	
	@RequestMapping("/participate")
	public String participateEvent(@ModelAttribute("publi") PublicationEvent publi, Model model,
			HttpSession session) {
		log.info("entered participate");
		publi.setNbparticipant(publi.getNbparticipant() + 1);

		Member mem = (Member) session.getAttribute("member");
		publi.getParticipant().add(mem);
		mem.getListEvent().add(publi);
		
		datamem.save(mem);
		datapub.save(publi);
		model.addAttribute("member", session.getAttribute("member"));
		model.addAttribute("publi", publi);
		return "detailsEvent";
	}
	
	@RequestMapping("/seedetailspublication")
	public String seeDetailsPublication(@RequestParam(value = "id", required = true) Integer id, Model model,
			HttpSession session) {
		Publication publi = datapub.findById(id);
		model.addAttribute("member", session.getAttribute("member"));

		if (publi instanceof PublicationProject) {
			{
				publi = (PublicationProject) publi;
				model.addAttribute("publi", publi);
			}
			return "detailsProject";
		} else if (publi instanceof PublicationAnnonce) {
			publi = (PublicationAnnonce) publi;
			model.addAttribute("publi", publi);
			return "detailsAnnonce";
		} else  {
			publi = (PublicationEvent) publi;
			model.addAttribute("publi", publi);
			return "detailsEvent";
		}
	}
}
