/**
 * Parallel.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus;

import com.impetus.octopus.util.TaskList;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public class Parallel {
	public Object parallelFor(ParallelTask task) throws CloneNotSupportedException {
		list = new TaskList(task);
		list.split();
		list.execute();
		Object finalResult = list.summarize();
		return finalResult;
		
	}
	/**
	 * TODO: Implement this
	 * @param task
	 * @return
	 */
	public static Object parallelWhile(ParallelTask task){
		return  null;
	}
	// fixed bugID 0003
	public void cleanup() {
		list.cleanup();
		
	}
	TaskList list = null;
}
