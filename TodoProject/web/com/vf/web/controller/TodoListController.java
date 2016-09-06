package com.vf.web.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * A ToDoListController is a RESTful url mapped component that handles requests from
 * the UI to load the UI and fetch data.
 * 
 * @author Dan McIntyre
 *
 */
@RequestMapping (value="/home")
public interface TodoListController {
	
	/**
	 * @return the ModelAndView for the Todo List View.
	 */
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ModelAndView load(); 

	/**
	 * @return the JSON array of Task objeects.
	 */
	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
	@ResponseBody
	public String fetch();
	
	/**
	 * Process the jsonArray of Task objects. The array may contain add, updates, and deletes.
	 * @param jsonArray	the JSON array of Task data
	 * @return	JSON data to return to the UI.
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestBody String jsonArray);

}
