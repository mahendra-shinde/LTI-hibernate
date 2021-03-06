/* 
 * Hibernate, Relational Persistence for Idiomatic Java
 * 
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.mahendra.hibernate4.module01;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import com.mahendra.hibernate4.module01.entity.Comment;
import com.mahendra.hibernate4.module01.entity.Community;
import com.mahendra.hibernate4.module01.entity.Donation;
import com.mahendra.hibernate4.module01.entity.Project;
import com.mahendra.hibernate4.module01.entity.ServiceEvent;
import com.mahendra.hibernate4.module01.entity.Skill;
import com.mahendra.hibernate4.module01.entity.Tool;
import com.mahendra.hibernate4.module01.entity.User;

/**
 * @author Brett Meyer
 */
public class BasicOrmDemo {
	
	public static void main(String[] args) {
		try {
			Tool tool = new Tool();
			// Note: id generated by Hibernate
			tool.setName( "Hammer" );
			insertTool( tool );
			List<Tool> tools = new ArrayList<Tool>();
			tools.add( tool );
			
			Skill skill = new Skill();
			// Note: id generated by Hibernate
			skill.setName( "Hammering Things" );
			insertSkill( skill );
			List<Skill> skills = new ArrayList<Skill>();
			skills.add( skill );
			
			User user = new User();
			// Note: id generated by Hibernate
			user.setName( "Brett Meyer" );
			user.setEmail( "foo@foo.com" );
			user.setPhone( "123-456-7890" );
			user.setTools( tools );
			user.setSkills( skills );
			
			insertUser( user );
			user = getUser(1);
			System.out.println( user.toString() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private static void insertUser(User user) throws Exception {
		//'user' is Transient
		Session session = openSession();
		session.getTransaction().begin();
		session.persist( user ); // cascades the tool & skill relationships
		//'user' is now Persistent object
		session.getTransaction().commit();
		//Leave Session, now 'user' is Detached!!
	}
	
	private static void insertTool(Tool tool) throws SQLException {
		Session session = openSession();
		session.getTransaction().begin();
		session.persist( tool );
		session.getTransaction().commit();
	}
	
	private static void insertSkill(Skill skill) throws SQLException {
		Session session = openSession();
		session.getTransaction().begin();
		session.persist( skill );
		session.getTransaction().commit();
	}
	
	private static User getUser(int id) throws SQLException {
		Session session = openSession();
		User user = (User) session.get( User.class, id );
		
		System.out.println("Get result: "+user.getClass().getCanonicalName());
		
//		Query query = session.createQuery( "SELECT u FROM User u WHERE u.id=:id" );
//		query.setParameter( "id", id );
//		User user = (User) query.uniqueResult();
		
//		User user = (User) session.createCriteria( User.class )
//				.add( Restrictions.eq( "id", id ) )
//				.uniqueResult();
		
	//	Hibernate.initialize( user.getTools() );
	//	Hibernate.initialize( user.getSkills() );
		
		session.close();
		
		return user;
	}
	
	private static SessionFactory sessionFactory = null;
	
	private static Session openSession() {
		if (sessionFactory == null) {
			final Configuration configuration = new Configuration();
			
			configuration.addAnnotatedClass( User.class );
			configuration.addAnnotatedClass( Tool.class );
			configuration.addAnnotatedClass( Skill.class );
			configuration.addAnnotatedClass( Community.class );
			configuration.addAnnotatedClass( Donation.class );
			configuration.addAnnotatedClass( Comment.class );
			configuration.addAnnotatedClass( ServiceEvent.class );
			configuration.addAnnotatedClass( Project.class );
			
			
			sessionFactory = configuration.buildSessionFactory(
					new StandardServiceRegistryBuilder().build() );
		}
		return sessionFactory.openSession();
	}
}
