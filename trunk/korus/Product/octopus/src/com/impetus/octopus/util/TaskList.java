/**
 * TaskList.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.impetus.octopus.BlockedRange;
import com.impetus.octopus.ParallelTask;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public class TaskList extends ArrayList<ParallelTask> {
	private static final Logger logger = Logger.getLogger(TaskList.class.getName());
	
	private ParallelTask task = null;
	
	public TaskList(ParallelTask task){
		this.task = task; 
	}
	
	public TaskList() {
		// TODO Auto-generated constructor stub
	}

	public void split() throws CloneNotSupportedException {
		logger.entering(TaskList.class.getName(), "split");
		
		int begin = task.getRange().getBegin();
		int end = task.getRange().getEnd();
		int grainSize = task.getRange().getGrainSize();
		// fixed bugID 0002
		if (((end-begin)+1) == grainSize) {
			logger.fine("Task Not Splited");
			this.add(task);
		}
		else if(task.isSplittable()){
			int maxChildCount = task.getMaxChildCount();
			logger.fine("MaxChildCount = "+maxChildCount);			
			for (int i = 0; i < maxChildCount; i++) {
				int newBegin = grainSize*i +begin;
				int newEnd = grainSize * (i+1);
				newEnd = (end < newEnd) ? end :newEnd;
				BlockedRange newRange = new BlockedRange(newBegin,newEnd,grainSize);
				ParallelTask newTask = (ParallelTask)task.clone();
				newTask.setRange(newRange);
				this.add(newTask);
			}
		}
		logger.exiting(TaskList.class.getName(), "split");
	}
	
	public void execute(){
		logger.entering(TaskList.class.getName(), "execute");
		for (ParallelTask pt : this) {
			logger.fine("Submitting task with range "+pt.getRange()+ " to threadPool" );
			ThreadPool.getThreadPool().execute(pt);
		}
		logger.exiting(TaskList.class.getName(), "execute");
	}
	
	public Object summarize(){
		logger.entering(TaskList.class.getName(), "summarize");
		List<Object> intermediateResults = new ArrayList<Object>();
		
		for (ParallelTask pt : this) {
			Object result = pt.getResult();
			logger.finest("Result of slave task : "+result);
			synchronized (pt) {
				while((result= pt.getResult()) == null){
					logger.finest("Waiting as result is not yet processed for this task");
					try {
						pt.wait();
					} catch (Exception e) {
						logger.throwing(TaskList.class.getName(), "summarize", e);
						e.printStackTrace();
					}
				}
			}
			
			intermediateResults.add(result);
		}
		logger.exiting(TaskList.class.getName(), "summarize");
		return task.summarize(intermediateResults);
	}
	
	// fixed bugID 0003
	public void cleanup(){
		ThreadPool.getThreadPool().shutdown();
	}

}
