package fr.cephee.unilille.controller;

import java.text.SimpleDateFormat;
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

import fr.cephee.unilille.database.CategoryPersistence;
import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationEvent;
import fr.cephee.unilille.model.PublicationEventForm;
import fr.cephee.unilille.model.PublicationExchange;
import fr.cephee.unilille.model.PublicationForm;
import fr.cephee.unilille.model.PublicationProject;
import fr.cephee.unilille.model.TypePublicationWrapper;

@Controller
public class PublicationController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	Validator validator;
	
	@Autowired
	private PublicationPersistence datamem;

	@Autowired
	private CategoryPersistence dataCate;

	@Autowired
	private CompetencePersistence dataComp;

	@RequestMapping(value = "/formtypepublication")
	public String publicationTypeForm(@ModelAttribute TypePublicationWrapper form, Model model) {
		PublicationForm publicationForm = new PublicationForm();
		
		model.addAttribute("publicationForm", publicationForm);

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

	@RequestMapping(value = "/checktypepublication", method = RequestMethod.POST)
	public String checkTypePublication(@ModelAttribute("publicationForm") PublicationForm publicationForm, BindingResult result, Model model,
			HttpSession session) {
		List<Category> listcategory = dataCate.findAll();
		List<Competence> listcompetence = dataComp.findAll();
		
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("competenceList", listcompetence);
		if (publicationForm.getTypePublication().equals("Projet"))
			return "createProject";
		else if (publicationForm.getTypePublication().equals("Evenement"))
		{
			PublicationEventForm publicationEventForm = new PublicationEventForm();
			model.addAttribute("publicationForm", publicationEventForm);
			return "createEvent";
		}
		else if (publicationForm.getTypePublication().equals("Echange"))
			return "createExchange";
		else
			return "errorPage";
	}

	@RequestMapping(value = "/registerexchange", method = RequestMethod.POST)
	public String createExchange(@ModelAttribute("publicationForm") PublicationForm publicationForm, BindingResult result, Model model,
			HttpSession session, Errors errors) {
		publicationForm.setTypePublication("Echange");
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, publicationForm, errors);		
		
		if (result.hasErrors())
		{
			for (ObjectError obj : result.getAllErrors())
				log.info(obj.toString());
			return "createExchange";
		}
		Date date = new Date();

		try {
			PublicationExchange publication = new PublicationExchange();
			publication.setTitle(publicationForm.getTitle());
			publication.setAuthorised(true);
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(date);
			publication.setAuthor((Member) session.getAttribute("member"));
			publication.setCategory(publicationForm.getListCategory());
			datamem.save(publication);
			model.addAttribute("publication", publication);
		} catch (Exception ex) {
			log.info(ex.toString());
			return "errorPage";
		}
		return "publication";
	}
	
	@RequestMapping(value = "/registerproject", method = RequestMethod.POST)
	public String createProject(@ModelAttribute("publicationForm") PublicationForm publicationForm, BindingResult result, Model model,
			HttpSession session, Errors errors) {
		publicationForm.setTypePublication("Projet");
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, publicationForm, errors);		
		
		if (result.hasErrors())
		{
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
			datamem.save(publication);
			model.addAttribute("publication", publication);
		} catch (Exception ex) {
			log.info(ex.toString());
			return "errorPage";
		}
		return "publication";
	}

	@RequestMapping(value = "/registerevent", method = RequestMethod.POST)
	public String createEvent(@ModelAttribute("publicationForm") PublicationEventForm publicationForm, BindingResult result, Model model,
			HttpSession session, Errors errors) {
		publicationForm.setTypePublication("Evenement");
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, publicationForm, errors);		
		
		if (result.hasErrors())
		{
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
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
			Date startd = sdf.parse(publicationForm.getStartevent() + " " + publicationForm.getHourstartevent());
			Date endd = sdf.parse(publicationForm.getEndevent() + " " + publicationForm.getHourendevent());
			publication.setStartevent(startd);
			publication.setEndevent(endd);
			datamem.save(publication);
			model.addAttribute("publication", publication);
		} catch (Exception ex) {
			log.info(ex.toString());
			return "errorPage";
		}
		return "publication";
	}
	
	@RequestMapping("/deletepublication")
	public String delete(int id) {
		try {
			Publication publication = new Publication(id);
			datamem.delete(publication);
		} catch (Exception ex) {
			return "Error deleting the publication:" + ex.toString();
		}
		return "publication succesfully deleted!";
	}

//	@RequestMapping("/get-by-AuthorPublication")
//	public String getByLogin(Member author) {
//		String publiId = "";
//		try {
//			Publication publi = datamem.findByAuthor(author);
//			publiId = String.valueOf(publi.getId());
//		} catch (Exception ex) {
//			return "Publication not found";
//		}
//		return "The Publication id is: " + publiId;
//	}

	@RequestMapping("/updatepublication")
	public String updatePublication(int id, String title, String content, List<Category> listCategory) {
		try {
			Publication publi = datamem.findById(id);
			publi.setTitle(title);
			publi.setContent(content);
			publi.setCategory(listCategory);
			datamem.save(publi);	
		} catch (Exception ex) {
			return "Error updating the Publication: " + ex.toString();
		}
		return "Publication succesfully updated!";
	}
	
	@RequestMapping("/seedetailspublication")
	public String seeDetailsPublication(@RequestParam(value="id", required=true) int id,Model model) {
		Publication publi = datamem.findById(id);
		model.addAttribute("publi", publi);
		return "detailsPublication";
	}
}
