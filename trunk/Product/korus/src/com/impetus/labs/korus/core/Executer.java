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

import com.impetus.labs.korus.addons.constructs.parallelfor.ParallelTask;

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
