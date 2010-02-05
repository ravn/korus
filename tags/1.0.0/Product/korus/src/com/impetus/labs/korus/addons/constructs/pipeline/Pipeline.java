/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2010 Impetus Technologies, Inc.(http://www.impetus.com)
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Korus.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.impetus.labs.korus.addons.constructs.pipeline;

import java.util.ArrayList;
import java.util.Iterator;

import com.impetus.labs.korus.core.KorusRuntime;
import com.impetus.labs.korus.core.message.RawMessage;
import com.impetus.labs.korus.core.process.BaseProcess;
import com.impetus.labs.korus.core.scheduler.Scheduler;
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
		BaseProcess process = KorusRuntime.getRegisteredProcess(name);
		if (process == null) {
			// Register Process with the name ParallelTask
			try {
				KorusRuntime.registerProcess(name, pipelineTask);
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

			pipelineTask.putMessage(new RawMessage());
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
