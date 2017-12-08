package fr.cephee.unilille.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fr.cephee.unilille.database.MemberRowMapper;


@Controller
public class LoginController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	
	@RequestMapping("/loginrequest")
	public String processLogin(@RequestParam(value="suppliedLogin", required=true) String suppliedLogin, Model model) {
		List<String> strLst  = jdbcTemplate.query("SELECT * FROM Students WHERE login = ?", new MemberRowMapper(), suppliedLogin);
		if(strLst.isEmpty()) {
			model.addAttribute("error_message", "Login " + suppliedLogin + " wasn't found in student database");
			return "loginError";
		}
		
		return "home";
	}
	
	@RequestMapping("/loginform")
	public String launchLoginForm() {
		return "login";
	}
	
}
