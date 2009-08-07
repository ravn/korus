/**
 * Executer.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.impetus.labs.korus.constructs.parallelFor.ParallelTask;

/**
 * Executer is a class extends thread its task is to pick the process from its
 * processQueue and execute it.
 */
public class Executer extends Thread
{

	public void run()
	{
		while (true)
		{
			// 1. Pick a Process
			Process process = getProcess();

			// 2. Check if the Process has null msg.
			if (process.peekMessage() == null)
			{
				// Cast process to the ParallelTask and execute the task.
				ParallelTask pt = (ParallelTask) process;
				pt.service(pt.getRange());
			} else
			{
				// Execute the Process.
				process.service(process.getMessage());
			}
		}
	}

	/**
	 * Set a Process to the Exectuer's processQueue.
	 * 
	 * @param process
	 *            process is the smallest part of any execution.
	 */
	public void setProcess(Process process)
	{
		try
		{
			processQueue.put(process);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Get a Process from the Exeuter's processQueue.
	 * 
	 * @return the<code> process</code> from Exeuter's
	 *         <code>processQueue</code>.
	 */
	public Process getProcess()
	{
		try
		{
			return processQueue.take();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private BlockingQueue<Process> processQueue = new LinkedBlockingQueue<Process>();

}