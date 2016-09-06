package com.vf.test.business.service.task.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import com.vf.business.service.task.TaskService;
import com.vf.business.service.task.impl.TaskServiceImpl;
import com.vf.common.Task;
import com.vf.common.dao.DAO;

public class TaskServiceImplTest extends EasyMockSupport{

	private List<Task> mockTasks;
	private TaskService taskService;
	private DAO mockDao;
	private Task newTask;
	
	@Before
	public void setup(){
		mockDao = EasyMock.createNiceMock(DAO.class);
		
		taskService = new MockService(mockDao);
		
		mockTasks = new ArrayList<Task>();
		newTask = new Task(){

			@Override
			public Integer getRowId() {
				return 1;
			}

			@Override
			public Integer getTaskId() {
				return 0;
			}

			@Override
			public String getDescription() {
				return "new";
			}

			@Override
			public String getStatus() {
				return "Active";
			}

			@Override
			public Date getCreateDate() {
				return null;
			}

			@Override
			public boolean isDelete() {
				return false;
			}};
			
			mockTasks.add(newTask);
	}
	
	@Test
	public void saveFromJsonTest(){
		
		EasyMock.expect(mockDao.saveObject(newTask)).andReturn(newTask);
		
		EasyMock.replay(mockDao);
		taskService.saveFromJson("does not matter for testing");
		EasyMock.verify(mockDao);
	}
	
	
	private class MockService extends TaskServiceImpl{
		
		public MockService(DAO dao){
			super();
			this.setDao(dao);
		}
		
		@Override
		protected List<Task> fromJson(String jsonArray){
			return mockTasks;
		}
	}
}
