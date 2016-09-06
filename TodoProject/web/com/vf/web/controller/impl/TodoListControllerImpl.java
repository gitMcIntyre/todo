package com.vf.web.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vf.business.service.task.TaskService;
import com.vf.common.Task;
import com.vf.common.dao.entityobjects.TaskEO;
import com.vf.common.util.JsonUtil;
import com.vf.web.controller.TodoListController;

@Controller
public class TodoListControllerImpl implements TodoListController{

	private String view = "todolist/context";
	
	@Autowired
	@Qualifier("taskService")
	private TaskService taskService;
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.web.controller.TodoListController#load()
	 */
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public ModelAndView load() {
		return new ModelAndView(view);
	}

	/*
	 * (non-Javadoc)
	 * @see com.vf.web.controller.TodoListController#fetch()
	 */
	@RequestMapping(value = "/fetch", method = RequestMethod.GET)
	@ResponseBody
	public String fetch() {
		
		List<Task> tasks = taskService.getTasks();
		String json = JsonUtil.serialize(tasks);
		
		return "{identifier: 'rowId', items: " + json + "}";
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.web.controller.TodoListController#save(java.lang.String)
	 */
	//@RequestMapping(value = "/save", method = RequestMethod.POST)
	//@ResponseBody
	public String save(@RequestBody String jsonArray){
		
		taskService.saveFromJson(jsonArray);
		return ""; //Not necessary at this point but I thouhgt about returning the updated list.
	}
}
