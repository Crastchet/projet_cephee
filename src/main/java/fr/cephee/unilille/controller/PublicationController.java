package fr.cephee.unilille.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

import fr.cephee.unilille.database.CategoryPersistence;
import fr.cephee.unilille.database.CompetencePersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Competence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Project;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationForm;
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

	@RequestMapping(value = "/formTypePublication")
	public String publicationTypeForm(@ModelAttribute TypePublicationWrapper form, Model model) {
		PublicationForm publicationForm = new PublicationForm();
		model.addAttribute("publicationForm", publicationForm);

		form.getPublicationList().add("Projet");
		form.getPublicationList().add("Echange");
		form.getPublicationList().add("Evenement");
		model.addAttribute("typepublicationlist", form);
		return "chooseTypePublication";
	}

	/*@RequestMapping(value = "/formPublication")
	public String publicationForm(@ModelAttribute TypePublicationWrapper form, Model model) {
		PublicationForm publicationForm = new PublicationForm();
		model.addAttribute("publicationForm", publicationForm);

		List<Category> listcategory = dataCate.findAll();
		List<Competence> listcompetence = dataComp.findAll();
		form.getPublicationList().add("Projet");
		form.getPublicationList().add("Echange");
		form.getPublicationList().add("Evenement");
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("competenceList", listcompetence);
		model.addAttribute("typepublicationlist", form);
		return "createPublication";
	}*/

	@RequestMapping(value = "/publicationPage", method = RequestMethod.POST)
	public String publicationPage(Model model, @ModelAttribute("publication") Publication publication) {
		PublicationForm publicationForm = new PublicationForm();
		model.addAttribute("publicationForm", publicationForm);
		return "publication";
	}

	@RequestMapping(value = "/checkTypePublication", method = RequestMethod.POST)
	public String checkTypePublication(@ModelAttribute("publicationForm") PublicationForm publicationForm, BindingResult result, Model model,
			HttpSession session) {
		List<Category> listcategory = dataCate.findAll();
		List<Competence> listcompetence = dataComp.findAll();
		
		model.addAttribute("categoryList", listcategory);
		model.addAttribute("competenceList", listcompetence);
		if (publicationForm.getTypePublication().equals("Projet"))
			return "createProject";
		else if (publicationForm.getTypePublication().equals("Evenement"))
			return "createEvent";
		else if (publicationForm.getTypePublication().equals("Echange"))
			return "createExchange";
		else
			return "errorPage";
	}

	@RequestMapping(value = "/registerProject", method = RequestMethod.POST)
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
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, 0);

		try {
			Project publication = new Project();
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

	@RequestMapping("/deletePublication")
	public String delete(int id) {
		try {
			Publication publication = new Publication(id);
			datamem.delete(publication);
		} catch (Exception ex) {
			return "Error deleting the publication:" + ex.toString();
		}
		return "publication succesfully deleted!";
	}

	@RequestMapping("/get-by-AuthorPublication")
	public String getByLogin(Member author) {
		String publiId = "";
		try {
			Publication publi = datamem.findByAuthor(author);
			publiId = String.valueOf(publi.getId());
		} catch (Exception ex) {
			return "Publication not found";
		}
		return "The Publication id is: " + publiId;
	}

	@RequestMapping("/updatePublication")
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
}
