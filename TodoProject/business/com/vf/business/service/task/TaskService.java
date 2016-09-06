package com.vf.business.service.task;

import java.util.List;

import com.vf.common.Task;

/**
 * Define the methods that support persisting a Task object to the database.
 * 
 * @author Dan McIntyre
 *
 */
public interface TaskService {
	
	/**
	 * @return a List of Task objects
	 */
	public List<Task> getTasks();
	
	/**
	 * Save a list of Task objects represented by the JSON data.
	 * @param json	A JSON serialized list of Task objects.
	 */
	public void saveFromJson(String json);
}
