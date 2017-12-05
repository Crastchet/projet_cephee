package fr.cephee.unilille.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.cephee.unilille.database.MemberPersistence;
import fr.cephee.unilille.model.Member;

@Controller
public class MemberController {

	@Autowired
	private MemberPersistence datamem;

	@RequestMapping("/create")
	@ResponseBody
	public String create(String login, String firstname, String lastname)
	{
		String userId = "";
		try {
			Member member = new Member();
			member.setLogin(login);
			member.setFirstname(firstname);
			member.setLastname(lastname);
			datamem.save(member);
			userId = String.valueOf(member.getId());
		}
		catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}
		return "User succesfully created with id = " + userId;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public String delete(int id) {
		try {
			Member user = new Member(id);
			datamem.delete(user);
		}
		catch (Exception ex) {
			return "Error deleting the user:" + ex.toString();
		}
		return "User succesfully deleted!";
	}

	@RequestMapping("/get-by-login")
	@ResponseBody
	public String getByLogin(String login) {
		String userId = "";
		try {
			Member user = datamem.findByLogin(login);
			userId = String.valueOf(user.getId());
		}
		catch (Exception ex) {
			return "User not found";
		}
		return "The user id is: " + userId;
	}

	@RequestMapping("/update")
	@ResponseBody
	public String updateUser(int id, String email, String firstname, String lastname) {
		try {
			Member user = datamem.findOne(id);
			user.setEmail(email);
			user.setFirstname(firstname);
			user.setLastname(lastname);
			datamem.save(user);
		}
		catch (Exception ex) {
			return "Error updating the user: " + ex.toString();
		}
		return "User succesfully updated!";
	}
}
