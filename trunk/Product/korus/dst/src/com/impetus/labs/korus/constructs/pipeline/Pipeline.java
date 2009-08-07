/**
 * Pipeline.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.constructs.pipeline;

import java.util.ArrayList;
import java.util.Iterator;

import com.impetus.labs.korus.core.Message;
import com.impetus.labs.korus.core.Process;
import com.impetus.labs.korus.core.ProcessManager;
import com.impetus.labs.korus.core.Scheduler;
import com.impetus.labs.korus.exception.ProcessAlreadyExistsException;

/**
 * This is the implementation of the Pipeline Construct.
 * Methods used by a Pipeline are written inside this class.
 * 
 */
public class Pipeline {

	/**
	 * constructor with parameter String identify 
	 * @param identity
	 */

	public Pipeline(String identity) {
		this.identity = identity;
	}

	/**
	 * Adds a Pipeline Task to the Pipeline Queue
	 * @param name Name of the Pipeline task
	 * @param pipelineTask Object of the PipelineTask
	 */
	public void add(String name, PipelineTask pipelineTask) {
		// 1. Select process from registered process list
		Process process = ProcessManager.getRegisteredProcess(name);
		if (process == null) {
			// Register Process with the name ParallelTask
			try {
				ProcessManager.registerProcess(name, pipelineTask);
			} catch (ProcessAlreadyExistsException e) {
				e.printStackTrace();
			}
		}
		pipelineTaskList.add(pipelineTask);
	}

	/**
	 * This method is used to join two Pipelined Tasks. It means, the order of
	 * the pipelined tasks is decided using this method. The First argument is
	 * the first pipelined tasks executed before the second argument or the 
	 * following pipeline Task.
	 * @param pipelineTaskProd The producer pipelined Task
	 * @param pipelineTaskCon The consumer pipelined Task
	 */
	public void join(PipelineTask pipelineTaskProd, PipelineTask pipelineTaskCon) {

		// create new blocking queue
		PipelineQueue<Object> blockingQueue = new PipelineQueue<Object>();

		// associate it with the tasks input and output
		pipelineTaskProd.setOutputQueue(blockingQueue);
		pipelineTaskProd.setNextTask(pipelineTaskCon);

		pipelineTaskCon.setInputQueue(blockingQueue);
		pipelineTaskCon.setPreviousTask(pipelineTaskProd);

	}

	/**
	 * Starts the execution of the pipeline after the initialization and 
	 * joining of the tasks are done.
	 *
	 */
	public void execute() {
		PipelineQueue<Object> blockingQueue = new PipelineQueue<Object>();
		lastTask = ((PipelineTask) pipelineTaskList.get(pipelineTaskList.size() - 1));

		lastTask.setOutputQueue(blockingQueue);

		for (Iterator iter = pipelineTaskList.iterator(); iter.hasNext();) {
			PipelineTask pipelineTask = (PipelineTask) iter.next();

			// Message put to prevent from the null check in executer
			Message msg = new Message();
			msg.put("pipelinetask", "pipelinetask");
			pipelineTask.putMessage(msg);

			Scheduler.setProcess(pipelineTask);
		}

	}

	/**
	 * Returns the result of the execution
	 * @return An Object representing the result of the execution of the
	 * Pipeline
	 */
	public Object getResult() {
		Object finalResult = null;

		try {
			lastTask.getOutputQueue().take();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		return finalResult;
	}
	
	public void cleanUp()
	{
		System.exit(0);
	}

	private String identity = null;

	private ArrayList<PipelineTask> pipelineTaskList = new ArrayList<PipelineTask>();

	private PipelineTask lastTask = null;

}
