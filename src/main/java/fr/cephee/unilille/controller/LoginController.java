package fr.cephee.unilille.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.cephee.unilille.database.CategoryPersistence;
import fr.cephee.unilille.database.MemberInterestPersistence;
import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Category;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.MemberForm;
import fr.cephee.unilille.model.MemberInterest;
import fr.cephee.unilille.model.Publication;


@Controller
public class LoginController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberInterestPersistence datainterest;

	@Autowired
	private CategoryPersistence datacat;
	
	@Autowired
	private MemberPersistence datamem;

	@Autowired
	Validator validator;
	
	@Autowired
	private PublicationPersistence datapub;
	
	
	@RequestMapping(value = {"/", "/home"})
	public String returnToHome(Model model, HttpSession session, Authentication auth)
	{	
		//SI LA PERSONNE SE CONNECTE
		if(session.getAttribute("member") == null) {
			Member signed = (Member) auth.getPrincipal();		//membre lille1 qui se connecte
			String username = signed.getUsername();				//on récupère son username du CAS (prenom.nom)
			Member member = datamem.findByUsername(username);	//on check si on a déjà une trace de lui en base
			if( member == null ) {								//s'il n'existe pas en base #premiereFois
				datamem.save(signed);
				//on le save (ça lui crée un id)
				member = datamem.findByUsername(username);			//on le récupère (il a maintenant un id)
			}
			session.setAttribute("member", member);
		}
		model.addAttribute("member", session.getAttribute("member"));
		Member member = (Member) session.getAttribute("member");
		if (datainterest.findByMember(member) == null)
		{
			List<Category> cat = datacat.findAll();
			MemberInterest meminterest = new MemberInterest(cat, member);
			datainterest.save(meminterest);
		}
// !!!!!!!!!!!!!! PARTIE DE SOFIAN !!!!!!!!!!!!!!
		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDescByAuthorisedTrue(member.getId());
		model.addAttribute("listlasttenpub", tenLastPub);

		
		List<List<Object>> lpubtotal = new ArrayList<>();
		
		MemberInterest memInt = datainterest.findByMember(member);

		
		for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
			if (entry.getValue() > 0)
			lpubtotal.add(datapub.findTop20FiltredPublicationByCategoryandDateCreation(member.getId(), entry.getKey().getId()));
		
		List<Publication> pubfiltred = new ArrayList<Publication>();
		for (List<Object> lpub : lpubtotal)
		{
			Iterator<Object> itr = lpub.iterator();
			while(itr.hasNext()){
				Object[] obj = (Object[]) itr.next();
				for (Object ob : obj)
				{
					if (ob instanceof Publication)
					{
						Publication p = (Publication) ob;
						if (!pubfiltred.contains(p))
						pubfiltred.add(p);
					}
				}
			}
		}
		
		ArrayList<Publication> finalFiltredPub = new ArrayList<Publication>();
		for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
		{
			int i = 0;
			for (Publication p : pubfiltred)
			{
				for (Category c : p.getCategory())
				if (memInt.getInterests().containsKey(c))
				{
					if (!finalFiltredPub.contains(p))
						finalFiltredPub.add(p);
					i++;
				}
				if (i == entry.getValue())
					break;
			}
		}
//		for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
//		{
//			for (int i = 0; i < entry.getValue(); i++)
//			{
// 
//			}
//		}
		
		for (Publication p : finalFiltredPub)
		{
			log.info("p  : " + p.getTitle());
				for (Category c : p.getCategory())
				{
					log.info("cat = " + c.getTitle());
				}
		}
		
		model.addAttribute("listlasttenpub", tenLastPub);
		model.addAttribute("finalFiltredPub", pubfiltred);
		
		return "home";
	}
	
	
	@RequestMapping(value = "/logout")
	public String disconnect(Model model, HttpSession session)
	{	
		session.removeAttribute("member");
		MemberForm memberForm = new MemberForm();
		model.addAttribute("memberForm", memberForm);
		return "home";
	}
	
	
	
	
	
	
	
	
	
	
	
//	@RequestMapping(value = {"/", "/login"})
//	public String loginPage(Model model)
//	{		
//		MemberForm memberForm = new MemberForm();
//		model.addAttribute("memberForm", memberForm);
//		return "login";
//	}
	
//	@RequestMapping("/createmember")
//	@ResponseBody
//	public String create(@RequestParam(value="login", required=true) String login,
//			@RequestParam(value="firstname", required=true) String firstname,
//			@RequestParam(value="lastname", required=true) String lastname)
//	{
//		try {
//			Member member = new Member();
//			member.setLogin(login);
//			member.setFirstname(firstname);
//			member.setLastname(lastname);
//			datamem.save(member);
//		}
//		catch (Exception ex) {
//			return "Error creating the member: " + ex.toString();
//		}
//		return "member succesfully created with login = " + login;
//	}

//	@RequestMapping("/createmembertest")
//	@ResponseBody
//	public String createTest(@RequestParam(value="login", required=true) String login)
//	{
//		if (datamem.findByLogin(login) != null)
//			return "Error creating the member: " + login + " alreay existing";
//		try {
//			Member member = new Member();
//			member.setLogin(login);
//			member.setFirstname("test1");
//			member.setLastname("test2");
//			member.setListpublication(null);
//			member.setSkills(null);
//			datamem.save(member);
//		}
//		catch (Exception ex) {
//			return "Error creating the member: " + ex.toString();
//		}
//		return "Membre créee avec le login :  " + login + " Retournez sur la page précédente et connectez vous ";
//	}
	
//	@RequestMapping("/deletemember")
//	@ResponseBody
//	public String delete(@RequestParam(value="id", required=true) int uid) {
//		try {
//			datamem.delete(uid);
//		}
//		catch (Exception ex) {
//			return "Error deleting the member:" + ex.toString();
//		}
//		return "member succesfully deleted!";
//	}

	
	
/*	@RequestMapping(value = "/getmemberbylogin", method = RequestMethod.POST)
	public String getByLogin(@ModelAttribute("memberForm") MemberForm memberForm,
			BindingResult result,
			Model model,
			Errors errors,
			HttpSession session) {

		List<Category> cat = datacat.findAll();
		Member member = datamem.findByUserName(memberForm.getUserName());
		memberForm.setMember(member);
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, memberForm, errors);		
		if (result.hasErrors())	{
			return "login";
		}
		if (datainterest.findByMember(member) == null)
		{
			MemberInterest meminterest = new MemberInterest(cat, member);
			datainterest.save(meminterest);
		}
		model.addAttribute("member", member);
		session.setAttribute("member", member);
				
		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDescByAuthorisedTrue(member.getId());

		//Debut du système publication par filtre

		List<List<Object>> lpubtotal = new ArrayList<>();
		
		MemberInterest memInt = datainterest.findByMember(member);

		for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
			if (entry.getValue() > 0)
			lpubtotal.add(datapub.findTop20FiltredPublicationByCategoryandDateCreation(member.getId(), entry.getKey().getId()));
		
		List<Publication> pubfiltred = new ArrayList<Publication>();
		for (List<Object> lpub : lpubtotal)
		{
			Iterator<Object> itr = lpub.iterator();
			while(itr.hasNext()){
				Object[] obj = (Object[]) itr.next();
				for (Object ob : obj)
				{
					if (ob instanceof Publication)
					{
						Publication p = (Publication) ob;
						if (!pubfiltred.contains(p))
						pubfiltred.add(p);
				}
				}
			}
	}
		
		ArrayList<Publication> finalFiltredPub = new ArrayList<Publication>();
	for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
		{
		int i = 0;
		for (Publication p : pubfiltred)
			{
				for (Category c : p.getCategory())
				if (memInt.getInterests().containsKey(c))
				{
					if (!finalFiltredPub.contains(p))
						finalFiltredPub.add(p);
					i++;
				}
				if (i == entry.getValue())
					break;
			}
		}
		/*for (Map.Entry<Category, Integer> entry : memInt.getInterests().entrySet())
		{
			for (int i = 0; i < entry.getValue(); i++)
			{
 
			}
		}
		
		for (Publication p : finalFiltredPub)
	{
			log.info("p  : " + p.getTitle());
				for (Category c : p.getCategory())
				{
					log.info("cat = " + c.getTitle());
				}
		}
		
	model.addAttribute("listlasttenpub", tenLastPub);
	model.addAttribute("finalFiltredPub", pubfiltred);
	return "home";
	}*/

//	@RequestMapping("/updatemember")
//	@ResponseBody
//	public String updateUser(@RequestParam(value="id", required=true) int id,
//			@RequestParam(value="email", required=true) String email,
//			@RequestParam(value="firstname", required=true) String firstname,
//			@RequestParam(value="lastname", required=true) String lastname) {
//		try {
//			Member user = datamem.findOne(id);
//			user.setEmail(email);
//			user.setFirstname(firstname);
//			user.setLastname(lastname);
//			datamem.save(user);
//		}
//		catch (Exception ex) {
//			return "Error updating the member: " + ex.toString();
//		}
//		return "member succesfully updated!";
//	}

}
