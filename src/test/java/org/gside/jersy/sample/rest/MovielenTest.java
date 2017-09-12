package org.gside.jersy.sample.rest;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.junit.BeforeClass;

public class MovielenTest {
	static protected final String BASE_URI = "http://localhost:8080/cm-assignment-web/";
	static protected Client client;
	static final EntityManagerFactory emf; 
	static {
		Properties pros = new Properties();
		pros.setProperty(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML, 
		                 "META-INF/persistence_test.xml");
		emf = Persistence.createEntityManagerFactory("cm_assignment_test", pros);
	}
	@BeforeClass
	public static void setUpClass() {
		client = ClientBuilder.newClient();
	          
	    EntityManager manager = emf.createEntityManager();
	    
	    EntityTransaction tx = manager.getTransaction();
	    tx.begin();
	    manager.createNativeQuery("delete from MOVIELENFILE").executeUpdate();
	    manager.createNativeQuery("delete from TOKEN").executeUpdate();
	    tx.commit();
	    manager.close();
	    emf.close();
		
	}
	public static class Info {
		public String message;

	}
}
