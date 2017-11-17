package fr.cephee.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.helloworld.HelloWorldApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(HelloWorldApplication.class);
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	
	@RequestMapping("/loginRequest")
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
		//model.addAttribute("user_birth", user_birth);
		
		return "home";
	}
	
	@RequestMapping("/loginForm")
	public String launchLoginForm() {
		return "login";
	}
	
}
