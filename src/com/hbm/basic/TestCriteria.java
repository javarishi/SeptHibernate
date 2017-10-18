package com.hbm.basic;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;

public class TestCriteria {
	
	private static SessionFactory sessionFactory = null;
	
	public TestCriteria() {
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
		TestCriteria test = new TestCriteria();
		// test.selectEmployee(6000);
		test.selectEmployee(6000, "Z%");

	}
	
	
	private void selectEmployee(int salary){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Employee.class);
		criteria.add(Restrictions.gt("salary", salary));
		List<Employee> result = criteria.list();
	
		for ( Employee eachEmp : result) {
			System.out.println(eachEmp);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	
	private void selectEmployee(int salary, String firstName){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Employee.class);
		Criterion cr1 = Restrictions.gt("salary", salary);
		Criterion cr2 = Restrictions.like("firstName", firstName);
		
		LogicalExpression exp = Restrictions.and(cr1,cr2 );
		
		criteria.add(exp);
		
		List<Employee> result = criteria.list();
	
		for ( Employee eachEmp : result) {
			System.out.println(eachEmp);
		}
		session.getTransaction().commit();
		session.close();
	}

}
