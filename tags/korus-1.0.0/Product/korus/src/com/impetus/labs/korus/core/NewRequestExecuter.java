/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2009 Impetus Technologies, Inc.(http://www.impetus.com/)
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * 
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *    
 * You should have received a copy of the GNU General Public License
 * along with Korus.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
