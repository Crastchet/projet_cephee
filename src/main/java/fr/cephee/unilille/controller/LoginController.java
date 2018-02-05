package fr.cephee.unilille.controller;

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
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.database.PublicationPersistence;
import fr.cephee.unilille.model.Member;
import fr.cephee.unilille.model.MemberForm;
import fr.cephee.unilille.model.Publication;


@Controller
public class LoginController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private MemberPersistence datamem;

	@Autowired
	Validator validator;
	
	@Autowired
	private PublicationPersistence datapub;
	
	@RequestMapping(value = "/home")
	public String returnToHome(Model model, HttpSession session)
	{		
		model.addAttribute("member", session.getAttribute("member"));
		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDesc();
		model.addAttribute("listlasttenpub", tenLastPub);
		return "home";
	}
	
	@RequestMapping(value = {"/", "/login"})
	public String loginPage(Model model)
	{		
		MemberForm memberForm = new MemberForm();
		model.addAttribute("memberForm", memberForm);
		return "login";
	}
	
	@RequestMapping(value = "/disconnect")
	public String disconnect(Model model, HttpSession session)
	{	
		session.removeAttribute("member");
		MemberForm memberForm = new MemberForm();
		model.addAttribute("memberForm", memberForm);
		return "login";
	}
	
	@RequestMapping("/createmember")
	@ResponseBody
	public String create(@RequestParam(value="login", required=true) String login,
			@RequestParam(value="firstname", required=true) String firstname,
			@RequestParam(value="lastname", required=true) String lastname)
	{
		try {
			Member member = new Member();
			member.setLogin(login);
			member.setFirstname(firstname);
			member.setLastname(lastname);
			datamem.save(member);
		}
		catch (Exception ex) {
			return "Error creating the member: " + ex.toString();
		}
		return "member succesfully created with login = " + login;
	}

	@RequestMapping("/createmembertest")
	@ResponseBody
	public String createTest(@RequestParam(value="login", required=true) String login)
	{
		if (datamem.findByLogin(login) != null)
			return "Error creating the member: " + login + " alreay existing";
		try {
			Member member = new Member();
			member.setLogin(login);
			member.setFirstname("test1");
			member.setLastname("test2");
			member.setListpublication(null);
			member.setSkills(null);
			datamem.save(member);
		}
		catch (Exception ex) {
			return "Error creating the member: " + ex.toString();
		}
		return "Membre créee avec le login :  " + login + " Retournez sur la page précédente et connectez vous ";
	}
	
	@RequestMapping("/deletemember")
	@ResponseBody
	public String delete(@RequestParam(value="id", required=true) int id) {
		try {
			Member user = new Member(id);
			datamem.delete(user);
		}
		catch (Exception ex) {
			return "Error deleting the member:" + ex.toString();
		}
		return "member succesfully deleted!";
	}

	@RequestMapping(value = "/getmemberbylogin", method = RequestMethod.POST)
	public String getByLogin(@ModelAttribute("memberForm") MemberForm memberForm,
			BindingResult result,
			Model model,
			Errors errors,
			HttpSession session) {
		
		Member member = datamem.findByLogin(memberForm.getLogin());
		memberForm.setMember(member);
		ValidationUtils.invokeValidator((org.springframework.validation.Validator) validator, memberForm, errors);		
		if (result.hasErrors())	{
			return "login";
		}
		model.addAttribute("member", member);
		session.setAttribute("member", member);
		List<Publication> tenLastPub = datapub.findTop10ByOrderByDateCreationDesc();
		model.addAttribute("listlasttenpub", tenLastPub);

		return "home";
	}

	@RequestMapping("/updatemember")
	@ResponseBody
	public String updateUser(@RequestParam(value="id", required=true) int id,
			@RequestParam(value="email", required=true) String email,
			@RequestParam(value="firstname", required=true) String firstname,
			@RequestParam(value="lastname", required=true) String lastname) {
		try {
			Member user = datamem.findOne(id);
			user.setEmail(email);
			user.setFirstname(firstname);
			user.setLastname(lastname);
			datamem.save(user);
		}
		catch (Exception ex) {
			return "Error updating the member: " + ex.toString();
		}
		return "member succesfully updated!";
	}

}
