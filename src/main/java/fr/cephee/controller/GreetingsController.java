package fr.cephee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingsController {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@RequestMapping("/greetings")
	public String greetings(@RequestParam(value="login", required=false, defaultValue="english") String language, Model model) {
		/*List<Object> str = jdbcTemplate.query(
                "SELECT greetings, name FROM greetings WHERE language = ?", new Object[] { language },
                (rs, rowNum) -> new HashMap<String,String>().put(rs.getString("greetings"), rs.getString("name"))
        );
        */
		String greetings = jdbcTemplate.queryForObject("SELECT greetings FROM greetings WHERE language = ?", new Object[] { language }, String.class);
		String name = jdbcTemplate.queryForObject("SELECT name FROM greetings WHERE language = ?", new Object[] { language }, String.class);
		
		model.addAttribute("greetings", greetings);
		model.addAttribute("name", name);
		
		//model.addAttribute("name", language);
		return "greetings";
	}
	
	@RequestMapping("/loginRequest")
	public String processLogin(@RequestParam(value="suppliedLogin", required=true) String suppliedLogin, Model model) {
		String user_firstname;
		try {
			user_firstname = jdbcTemplate.queryForObject("SELECT firstname FROM Students WHERE login = ?", new Object[] { suppliedLogin }, String.class);
		} catch (EmptyResultDataAccessException e) {
			model.addAttribute("error_message", "Login " + suppliedLogin + " wasn't found in student database");
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
