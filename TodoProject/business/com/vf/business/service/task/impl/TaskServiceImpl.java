package com.vf.business.service.task.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vf.business.service.task.TaskService;
import com.vf.common.Task;
import com.vf.common.dao.DAO;
import com.vf.common.dao.entityobjects.TaskEO;
import com.vf.common.util.JsonUtil;

/**
 * Purpose to perform CRUD operations on the Task object.
 * 
 * @author Dan McIntyre
 *
 */
@Service("taskService")
public class TaskServiceImpl implements TaskService{

	/**
	 * The injected data access object.
	 */
	private DAO dao;
	
	/**
	 * The named query used to fetch a list of all Task objects.
	 */
	private static final String listAllQuery = "findAllTasks";
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.business.service.task.TaskService#getTasks()
	 */
	public List<Task> getTasks(){
		  return dao.executeNamedQuery(listAllQuery, new ArrayList());
	}

	/*
	 * (non-Javadoc)
	 * @see com.vf.business.service.task.TaskService#saveFromJson(java.lang.String)
	 */
	public void saveFromJson(String json) {
		//Deserialize the json string into a list of Task objects.
		@SuppressWarnings("unchecked")
		List<Task> items = (ArrayList<Task>) fromJson(json);
		
		//Process each Task in the list
		Iterator it = items.iterator();
		Task t;
		while(it.hasNext()){
			t = (Task)it.next();
			if(t.isDelete()){
				dao.deleteObject(t);
			}
			else{
				dao.saveObject(t);
			}
		}
	}
	
	/*
	 * Deserialize the josn array.
	 * @return a List of Task objects. 
	 */
	@SuppressWarnings("unchecked")
	protected List<Task> fromJson(String jsonArray){
		List<Task> tasks = new ArrayList<Task>();
		
		//Simple test for now. Could be enhanced to make sure the string is "like" a json array.
		if(jsonArray != null && jsonArray.length() > 0){
			tasks = (ArrayList<Task>) JsonUtil.deserializeToArray(jsonArray, TaskEO.class);
		}
		return tasks;
	}
	
	@Autowired
	protected void setDao(DAO dao){
		this.dao = dao;
	}
}
