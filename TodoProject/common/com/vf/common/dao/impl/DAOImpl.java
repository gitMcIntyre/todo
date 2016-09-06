package com.vf.common.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vf.common.dao.DAO;

/**
 * Implementation that uses Hibernate to persist data to the data store.
 * 
 * @author Dan McIntyre
 *
 */
@Service
public class DAOImpl implements DAO{
	
	/**
	 * The Hibernate Session Factory
	 */
	SessionFactory sessionFactory;
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.common.dao.DAO#executeNamedQuery(java.lang.String, java.util.List)
	 */
	public <T> List<T> executeNamedQuery(final String qName, final List parameters) {
		final Session session = this.getHibernateSession();
		List<T> result = null;

		//Get the named query.
		final Query q = session.getNamedQuery(qName);
		q.setCacheable(true);

		this.setQueryParameters(q, parameters);
		
		//Execute the query
		result = q.list();

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see com.vf.common.dao.DAO#saveObject(java.lang.Object)
	 */
	public Object saveObject(final Object saveObject) {
		try {
			// assigns primary key values
			Session session = this.getHibernateSession();	
			Object returnObj = null;
			
			//Updae the object and call flush to persist the changes.
			returnObj =	session.merge(saveObject);
			session.flush();
	
			return returnObj;
		}
		catch (ConstraintViolationException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.common.dao.DAO#deleteObject(java.lang.Object)
	 */
	public void deleteObject(Object theObject){
		try {
			//Delete the object and call flush to persist the change.
			Session session = this.getHibernateSession();
			session.delete(theObject);
			session.flush();
		}
		catch (ConstraintViolationException ex) {
			throw new RuntimeException("Constraing Violation");
		}
	}

	/*
	 * @return the hibernate Session
	 * 
	 */
	private Session getHibernateSession() {
		Session session = null;
		session = sessionFactory.getCurrentSession();
	
		//throw exception if the session is not connected
		if ((session == null) || !(session.isOpen())
				|| !(session.isConnected())) {
			throw new RuntimeException("Database connection exception");
		}
		
		return session;
	}

	/*
	 * Set the query parameters into the query
	 * @parameters is a List of sub lists. Each sub list contain two entries
	 * the first one is parameter name (string), and the second entry is the
	 * value (object), the value can be a collection, list, or object
	 */
	private void setQueryParameters(final Query q, final List parameters) {
		int i = 0;
		for (final Iterator iter = parameters.iterator(); iter.hasNext();) {
			final Object p = iter.next();
			if (p instanceof List) {
				final List parameter = (List) p;
				if (parameter.get(1) instanceof Collection) {
					q.setParameterList((String) parameter.get(0),
							(Collection) parameter.get(1));
				} else if (parameter.get(1) instanceof Object[]) {
					q.setParameterList((String) parameter.get(0),
							(Object[]) parameter.get(1));
				} else {
					q.setParameter((String) parameter.get(0), parameter.get(1));
				}
			} else {
				q.setParameter(i, p);
			}
			i++;
		}
	}

	@Autowired
	public void setSessionFactory(SessionFactory sesstionFactory){
		this.sessionFactory = sesstionFactory;
	}
	
	/*
	 * A utility method that can be used to recreate the SQLite database.
	 */
	public void createDB(){
		
		 Connection connection = null;
	        try
	        {
	          // create a database connection
	          connection = DriverManager.getConnection("jdbc:sqlite:project.sqlite");
	          Statement statement = connection.createStatement();
	          statement.setQueryTimeout(30);  // set timeout to 30 sec.

	          statement.executeUpdate("drop table if exists task");
	          statement.executeUpdate("create table task (taskId integer primary key asc, description string, status string, createDate datetime default current_timestamp)");
	          statement.executeUpdate("insert into task values(1, 'leo', 'Active', null)");
	          statement.executeUpdate("insert into task values(2, 'yui', 'Complete', null)");
	        }
	        catch(SQLException ex){
	        	throw new RuntimeException(ex);
	        }
		
	}
}
