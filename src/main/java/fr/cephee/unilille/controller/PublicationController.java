package fr.cephee.unilille.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.Publication;

@Controller
public class PublicationController {
	@Autowired
	private PublicationPersistence datamem;
	
	@RequestMapping("/createPublication")
	@ResponseBody
	public String create(String title, String content, Date date, Member author, List<Category> listCategory)
	{
		
		String publicationInfo = "";
		try {
			Publication publication = new Publication();
			publication.setTitle(title);
			publication.setContent(content);
			publication.setDateCreation(date);
			publication.setAuthor(author);
			publication.setCategory(listCategory);
			datamem.save(publication);
			publicationInfo = String.valueOf(publication.getId());
		}
		catch (Exception ex) {
			return "Error creating the publication: " + ex.toString();
		}
		return "publication succesfully created with id = " + publicationInfo;
	}
	
	@RequestMapping("/deletePublication")
	@ResponseBody
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
	@ResponseBody
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
	@ResponseBody
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
