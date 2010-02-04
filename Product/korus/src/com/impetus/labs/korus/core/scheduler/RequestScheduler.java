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
package com.impetus.labs.korus.core.scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.impetus.labs.korus.core.KorusRuntime;
import com.impetus.labs.korus.core.executer.RequestExecuter;
import com.impetus.labs.korus.core.process.BaseProcess;

/**
 *  
 * This Class is responsible for scheduling the first sub-part of the Requests
 * and places them in a RequestExecuter's queue. Thus the tasks whose some 
 * part of request is processed, gets priority over new tasks accepted by the system.
 * 
 */
public class RequestScheduler extends Thread
{

	public void run()
	{
		while (true)
		{
			// 1. Pick a Process
			BaseProcess process = getProcess();

			// 2. Select a RequestExecuter
			RequestExecuter requestExecuter = KorusRuntime
					.getNextRequestExecuter();

			// 3. Set the process to the nRExecuter's processQueue
			requestExecuter.setProcess(process);
		}
	}

	/**
	 * Set a Process to the RequestScheduler's processQueue.
	 * 
	 * @param process
	 *            process is the smallest part of any execution.
	 */
	public static void setProcess(BaseProcess process)
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
	 * Get a Process from the RequestScheduler's processQueue.
	 * 
	 * @return the<code> process</code> from RequestScheduler's
	 *         <code>processQueue</code>.
	 */
	public static BaseProcess getProcess()
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

	private static BlockingQueue<BaseProcess> prcoessQueue = new LinkedBlockingQueue<BaseProcess>();

}
