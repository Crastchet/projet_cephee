package fr.cephee.unilille.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	
	@RequestMapping("/loginrequest")
	public String processLogin(@RequestParam(value="suppliedLogin", required=true) String suppliedLogin, Model model) {
		String user_firstname = jdbcTemplate.queryForObject("SELECT firstname FROM Students WHERE login = ?", new Object[] { suppliedLogin }, String.class);
		if(user_firstname == null) {
			model.addAttribute("error_message", "Login" + suppliedLogin + " wasn't found in student database");
			return "loginError";
		}
		
		String user_lastname = jdbcTemplate.queryForObject("SELECT lastname FROM Students WHERE login = ?", new Object[] { suppliedLogin }, String.class);
		String user_birth = jdbcTemplate.queryForObject("SELECT birth FROM Students WHERE login = ?", new Object[] { suppliedLogin }, String.class);
		
		model.addAttribute("user_firstname", user_firstname);
		model.addAttribute("user_lastname", user_lastname);
		model.addAttribute("user_birth", user_birth);
		
		return "home";
	}
	
	@RequestMapping("/loginform")
	public String launchLoginForm() {
		return "login";
	}
	
}
