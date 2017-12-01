package fr.cephee.unilille;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorldApplication implements CommandLineRunner {

	//private static final Logger log = LoggerFactory.getLogger(HelloWorldApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class);
	}

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
