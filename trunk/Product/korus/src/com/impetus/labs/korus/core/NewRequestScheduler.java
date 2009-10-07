/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2009 Impetus Technologies, Inc.
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
 * This Class is responsible for scheduling the first sub-part of the Requests
 * and places them in a NewRequestExecuter's queue. Thus the tasks whose some 
 * part of request is processed, gets priority over new tasks accepted by the system.
 * 
 */
public class NewRequestScheduler extends Thread
{

	public void run()
	{
		while (true)
		{
			// 1. Pick a Process
			Process process = getProcess();

			// 2. Select a NewRequestExecuter
			NewRequestExecuter nRExecuter = KorusRuntime
					.getNextNewRequestExecuter();

			// 3. Set the process to the nRExecuter's processQueue
			nRExecuter.setProcess(process);
		}
	}

	/**
	 * Set a Process to the NewRequestScheduler's processQueue.
	 * 
	 * @param process
	 *            process is the smallest part of any execution.
	 */
	public static void setProcess(Process process)
	{
		try
		{
			prcoessQueue.put(process);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Get a Process from the NewRequestScheduler's processQueue.
	 * 
	 * @return the<code> process</code> from NewRequestScheduler's
	 *         <code>processQueue</code>.
	 */
	public static Process getProcess()
	{
		try
		{
			return prcoessQueue.take();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static BlockingQueue<Process> prcoessQueue = new LinkedBlockingQueue<Process>();

}
