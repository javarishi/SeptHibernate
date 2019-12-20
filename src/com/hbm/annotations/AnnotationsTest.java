package com.hbm.annotations;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class AnnotationsTest {
	
	private SessionFactory sessionFactory = null; 
	public AnnotationsTest() {
		// A SessionFactory is set up once for an application!
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		}
		catch (Exception e) {
			e.printStackTrace();
			// The registry would be destroyed by the SessionFactory, 
			// but we had trouble building the SessionFactory so destroy it manually.
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	public static void main(String[] args) {
		AnnotationsTest test = new AnnotationsTest();
		test.selectAll();
		Actor actor = new Actor();
		actor.setFirstName("Niel");
		actor.setLastName("Harris");
		actor.setLastUpdate(new Date());
		test.insertActor(actor);
	}
	
	
	// insert is just session.save()
	private void insertActor(Actor actor){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save( actor );
		session.getTransaction().commit();
		session.close();
	}
	
	private void selectAll(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Actor> result = session.createQuery( "from Actor" ).list();
		for ( Actor eachActor : result) {
			System.out.println(eachActor);
		}
		session.getTransaction().commit();
		session.close();
	}
}
