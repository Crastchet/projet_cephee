package fr.cephee.unilille.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.model.Member;


@Controller
public class LoginController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private MemberPersistence datamem;

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

	@RequestMapping("/getmemberbylogin")
	public String getByLogin(@RequestParam(value="login", required=true) String suppliedLogin,
			Model model) {
		Member member = datamem.findByLogin(suppliedLogin);
		if (member == null)
			return "loginError";
		model.addAttribute("member", member);
		model.addAttribute("error_message", "Login " + suppliedLogin + " wasn't found in student database");
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

	@RequestMapping("/loginrequest")
	public String processLogin(@RequestParam(value="suppliedLogin", required=true) String suppliedLogin, Model model) {
		/*List<String> strLst  = jdbcTemplate.query("SELECT * FROM Students WHERE login = ?", new MemberRowMapper(), suppliedLogin);
		if(strLst.isEmpty()) {
			model.addAttribute("error_message", "Login " + suppliedLogin + " wasn't found in student database");
			return "loginError";
		}*/

		return "home";
	}

	@RequestMapping("/loginform")
	public String launchLoginForm() {
		return "login";
	}

}
