package com.vf.common;

import java.util.Date;

/**
 * This class defines the methods that support the Task Entity Object.
 * 
 * @author Dan McIntyre
 *
 */
public interface Task {
	
	/**
	 * @return The row identifier that uniquely identifies the row in the databse.
	 */
	public Integer getRowId();
	
	/**
	 * @return The Task id assigned by the databse.
	 */
	public Integer getTaskId();
	
	/**
	 * @return The Task description
	 */
	public String getDescription();
	
	/**
	 * @return The status of the Task
	 */
	public String getStatus();

	/**
	 * @return The Date the Task was created.
	 */
	public Date getCreateDate();
	
	/**
	 * @return Is this Task marked for deletion.
	 */
	public boolean isDelete();
}
