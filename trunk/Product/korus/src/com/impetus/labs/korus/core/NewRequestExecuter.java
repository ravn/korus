/**
 * NewRequestExecuter.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  
 * This Class is responsible for executing the first sub-part of the Requests
 * and places them in a separate queue. Thus the tasks whose some part of
 * request is processed, gets priority over new tasks accepted by the system.
 * 
 */
public class NewRequestExecuter extends Thread
{

	public void run()
	{
		while (true)
		{
			// 1. Pick a Process
			Process process = getProcess();
			// 2. Execute the Process.
			process.service(process.getMessage());
		}
	}

	/**
	 * Set a Process to the NewRequestExecuter's processQueue.
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
	 * Get a Process from the NewRequestExecuter's processQueue.
	 * 
	 * @return the<code> process</code> from NewRequestExecuter's
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
