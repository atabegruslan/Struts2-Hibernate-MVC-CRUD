package com.ruslan.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.ruslan.RatingsEntry;

public class Database {
	
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	public List<RatingsEntry> read(){
		List<RatingsEntry> entries = null;
		
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		
		session.beginTransaction();
		
		try{
			entries = (List<RatingsEntry>) session.createQuery("from RatingsEntry").list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		session.getTransaction().commit();
		session.close();
		
		return entries;
	}
	
	public List<RatingsEntry> readOne(String dest){
		List<RatingsEntry> entries = null;
		
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		
		session.beginTransaction();
		
		try{
			Query query = session.createQuery("from RatingsEntry where destination = :dest ");
			query.setParameter("dest", dest);
			entries = (List<RatingsEntry>) query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		session.getTransaction().commit();
		session.close();
		
		return entries;		
	}
	
	public void create(RatingsEntry ratingsEntry){
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();		
		session.save(ratingsEntry);
		session.getTransaction().commit();
		session.close();		
	}
	
	public void update(String newRating, String destForNewRating){
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();		
		
		Query query = session.createQuery("update RatingsEntry set rating = :rating where destination = :dest ");
		query.setParameter("rating", newRating);
		query.setParameter("dest", destForNewRating);
		query.executeUpdate();
		session.getTransaction().commit();
		session.close();	
	}
	
	public void delete(String dest){
		SessionFactory sf = createSessionFactory();
		Session session = sf.openSession();
		session.beginTransaction();		
		
		try{
			Query query = session.createQuery("from RatingsEntry where destination = :dest ");
			query.setParameter("dest", dest);
			List<RatingsEntry> entries = (List<RatingsEntry>) query.list();
			session.delete(entries.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
				
		session.getTransaction().commit();
		session.close();		
	}

	public static SessionFactory createSessionFactory() {
	    Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    return sessionFactory;
	}
}
