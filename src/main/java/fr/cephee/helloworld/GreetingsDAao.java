package fr.cephee.helloworld;

import javax.annotation.PostConstruct;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

@Repository
public class GreetingsDAao extends JdbcDaoSupport {

	
	@Autowired
	DataSource dataSource; //bon package ?
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	
}
