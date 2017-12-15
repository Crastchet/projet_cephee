package fr.cephee.unilille.controller;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;
import fr.cephee.unilille.model.PublicationForm;

@Controller
public class PublicationController {
	@Autowired
	private PublicationPersistence datamem;

	@RequestMapping(value = "/formPublication")
	public String publicationForm(Model model)
	{
		PublicationForm publicationForm = new PublicationForm();
		model.addAttribute("publicationForm", publicationForm);
		return "createPublication";		
	}
	
	@RequestMapping(value = "/publicationPage", method = RequestMethod.POST)
	public String publicationPage(Model model, @ModelAttribute("publication") Publication publication)
	{
		PublicationForm publicationForm = new PublicationForm();
		model.addAttribute("publicationForm", publicationForm);
		return "publication";
	}
	
	@RequestMapping(value = "/createPublication", method = RequestMethod.POST)
	public String createPublication(@ModelAttribute("publicationForm") PublicationForm publicationForm, Model model, HttpSession session)
	{
		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, 0);
		try {
			Publication publication = new Publication();
			publication.setTitle(publicationForm.getTitle());
			publication.setContent(publicationForm.getContent());
			publication.setDateCreation(date);
			publication.setAuthor((Member) session.getAttribute("member"));
			//publication.setCategory(listCategory);
			datamem.save(publication);
			model.addAttribute("publication", publication);
		}
		catch (Exception ex) {
			return "ErrorPage";
		}
		return "publication";
	}
	
	@RequestMapping("/deletePublication")
	public String delete(int id) {
		try {
			Publication publication = new Publication(id);
			datamem.delete(publication);
		}
		catch (Exception ex) {
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
		}
		catch (Exception ex) {
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
		}
		catch (Exception ex) {
			return "Error updating the Publication: " + ex.toString();
		}
		return "Publication succesfully updated!";
	}
}
