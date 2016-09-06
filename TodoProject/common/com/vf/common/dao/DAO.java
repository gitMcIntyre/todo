package com.vf.common.dao;

import java.util.List;

import com.vf.common.Task;

/**
 * Define the methods necessary to support storing application data to a data store.
 *  
 * @author Dan McIntyre
 *
 */
public interface DAO {
	
	/**
	 * Fetch a list of items based on a known query.
	 * @param <T> The type of objects returned in the List.
	 * @param qName	The name of the query to execute.
	 * @param parameters A list of named parameters to supply to the query.
	 * @return
	 * 		A List of <T> objects.
	 */
	public <T> List<T> executeNamedQuery(final String qName, final List parameters);
	
	/**
	 * Persist the given object to the data store.
	 * @param saveObject	The object to persist.
	 * @return
	 * 		The newly persisted object.
	 */
	public Object saveObject(final Object saveObject);
	
	/**
	 * Remove an object from the data store.
	 * @param theObject	The object to remove.
	 */
	public void deleteObject(Object theObject);
}
