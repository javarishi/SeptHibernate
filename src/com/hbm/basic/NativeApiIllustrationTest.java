package com.hbm.basic;

import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class NativeApiIllustrationTest {
	
	private static SessionFactory sessionFactory = null;

	public NativeApiIllustrationTest() {
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
		NativeApiIllustrationTest test = new NativeApiIllustrationTest();
		Employee empToSave = new Employee();
		empToSave.setFirstName("Ryan");
		empToSave.setLastName("Cobalt");
		empToSave.setSalary(4000); 
	//	test.insertEmployee(empToSave);
		
		Employee empToUpdate = new Employee();
		empToUpdate.setId(5);
		empToUpdate.setFirstName("Ryan");
		empToUpdate.setLastName("Coble");
		empToUpdate.setSalary(5000); 
		//test.updateEmployee(empToUpdate);
		//test.deleteEmployee(empToUpdate);
		test.selectAll();
		//test.updateEmployee(4, 5000);
		// Write delete method for HQL
		//test.hqlAggregators();
		sessionFactory.close();

	}

	// insert is just session.save()
	private void insertEmployee(Employee empToSave){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		//session.save( empToSave );
		session.saveOrUpdate( empToSave );
		System.out.println("New Employee ID: " + empToSave.getId());
		tx.commit();
		session.close();
	}
	
	private void updateEmployee(Employee empToUpdate){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update( empToUpdate );
		System.out.println("Updated Employee ID: " + empToUpdate.getId());
		tx.commit();
		session.close();
	}
	
	private void deleteEmployee(Employee empToDelete){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.delete( empToDelete );
		System.out.println("Delete Employee ID: " + empToDelete.getId());
		tx.commit();
		session.close();
	}
	
	private void selectAll(){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List<Employee> result = session.createQuery( "from Employee" ).list();
		
		for ( Employee eachEmp : result) {
			System.out.println(eachEmp);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	
	private void selectEmployee(int empId){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		TypedQuery<Employee> query = session.createQuery( "from Employee E where E.id = :emp_id" );
		query.setParameter("emp_id", empId);
		List<Employee> result = query.getResultList();
		for ( Employee eachEmp : result) {
			System.out.println(eachEmp);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	
	private void updateEmployee(int empId, int salary){
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		TypedQuery<Employee> query = session.createQuery( "Update Employee E set E.salary = :salary where E.id = :emp_id" );
		query.setParameter("emp_id", empId);
		query.setParameter("salary", salary);
		int numberOfRowsAffected = query.executeUpdate();
		System.out.println("Number of Rows affected :: " + numberOfRowsAffected);
		session.getTransaction().commit();
		session.close();
	}
	
	private void hqlAggregators(){
		String hql = "SELECT SUM(E.salary) as salary, E.firstName FROM Employee E GROUP BY E.firstName";
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		List result = session.createQuery(hql).list();
		System.out.println(result);
		session.getTransaction().commit();
		session.close();
	}
	// Please add methods for other Aggregators - count, max, min, avg
	
	
}
