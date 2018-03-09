package fr.cephee.unilille;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class UniLilleApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(UniLilleApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(UniLilleApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		log.info("coucou2");
        return application.sources(UniLilleApplication.class);
	}
}
