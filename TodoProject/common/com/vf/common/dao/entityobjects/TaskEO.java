package com.vf.common.dao.entityobjects;

import java.util.Date;

import com.vf.common.Task;

/**
 * Entity Object implementation for the Task Interface.
 * @author Administrator
 *
 */
public class TaskEO implements Task{
	
	private Integer taskId;
	private String description;
	private String status;
	private Date createDate;
	private Integer rowId;
	private boolean isDelete = false;

	/*
	 * (non-Javadoc)
	 * @see com.vf.common.Task#getRowId()
	 */
	public Integer getRowId(){
		return this.rowId;
	}
	
	/**
	 * @param rowId
	 */
	public void setRowId(Integer rowId){
		this.rowId = rowId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.common.Task#getTaskId()
	 */
	public Integer getTaskId(){
		return taskId;
	}
	
	/**
	 * Set this value to 0 to identify the Task as new.
	 * @param taskId
	 */
	public void setTaskId(Integer taskId){
		this.taskId = taskId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.common.Task#getDescription()
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.common.Task#getStatus()
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * @param status
	 */
	public void setStatus(String status){
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * @see com.vf.common.Task#getCreateDate()
	 */
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.vf.common.Task#isDelete()
	 */
	public boolean isDelete(){
		return this.isDelete;
	}
	
	/**
	 * @param isDelete
	 */
	public void setDelete(boolean isDelete){
		this.isDelete = isDelete;
	}
}
