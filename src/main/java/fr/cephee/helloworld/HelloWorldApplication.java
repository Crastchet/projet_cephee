package fr.cephee.helloworld;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class HelloWorldApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(HelloWorldApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	/*@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for(String beanName : beanNames)
				System.out.println(beanName);
		};
}
	
	@Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");
        
/**
        Scanner sc = new Scanner(new File("script_db.sql"));
        if(sc == null) {
        	log.error("File script_db.sql doesn't exist !");
        	SpringApplication.exit(null);
        	return;
        }
        
        String script_to_execute = sc.useDelimiter("\\Z").next();
        
        log.info("Script used for DB initialization :");
        log.info(script_to_execute);
        jdbcTemplate.execute(script_to_execute);
**/        
        /*
        jdbcTemplate.execute("DROP TABLE greetings IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE greetings(" +
                "greetings_id SERIAL, greetings VARCHAR(255), name VARCHAR(255), language VARCHAR(50))");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("Hello,world,english", "Salut,le monde,french", "Hola,el mundo,spanish", "Saluton,mondo,esperanto", "Ciao,mondo,italian", "Hallo,welt,german", "Hallo,wereld,dutch").stream()
                .map(name -> name.split(","))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting greetings record for %s %s %s", name[0], name[1], name[2])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO greetings(greetings, name, language) VALUES (?,?,?)", splitUpNames);
		
        
        log.info("Querying for greetings records where name = 'mondo':");
        jdbcTemplate.query(
                "SELECT greetings_id, greetings, name, language FROM greetings WHERE name = ?", new Object[] { "mondo" },
                (rs, rowNum) -> new Greetings(rs.getLong("greetings_id"), rs.getString("greetings"), rs.getString("name"), rs.getString("language"))
        ).forEach(greetings -> log.info(greetings.toString()));
    }*/
}
