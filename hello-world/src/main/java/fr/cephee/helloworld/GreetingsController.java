package fr.cephee.helloworld;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String greetings(@RequestParam(value="lang", required=false, defaultValue="english") String language, Model model) {
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

}
