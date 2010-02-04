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
package com.impetus.labs.korus.addons.constructs.parallelfor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.impetus.labs.korus.core.KorusRuntime;
import com.impetus.labs.korus.core.process.BaseProcess;
import com.impetus.labs.korus.exception.ProcessAlreadyExistsException;
/**
 * Parallel Construct parallelFor is implemented inside this class.
 * Similar constructs can be implemented by creating classes like these.
 * 
 */
public class Parallel
{
	/**
	 * Impelementation of a Parallel For
	 * 
	 * @param parallelTask
	 *            A task which is to execute parallel.
	 */
	public void parallelFor(ParallelTask parallelTask)
	{
		// 1. Select process from registered process list
		BaseProcess process = KorusRuntime
				.getRegisteredProcess("_~_~_Parallel_~_Task_~_~_");
		if (process == null)
		{
			// Register Process with the name "ParallelTask"
			try {
				KorusRuntime.registerProcess("_~_~_Parallel_~_Task_~_~_",
						parallelTask);
			} catch (ProcessAlreadyExistsException e) {
				e.printStackTrace();
			}
		}
		// 2. Put message in process's messageQueue
		parallelTask.putMessage(parallelTask.getRange());
		
		// 3. execute the task.
		execute(parallelTask);
	}

	/**
	 * Get the final result from the resultQueue.
	 * 
	 * @return the final result of a task, cumulative of all sub-tasks.
	 */
	public Object getResult()
	{
		Object finalResult = null;
		try
		{
			finalResult = (Object) resultQueue.take();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return finalResult;
	}

	/**
	 * Executes the parallelTask.
	 * 
	 * @param parallelTask
	 *            A sub-part of a task which will be executed
	 */
	private void execute(ParallelTask parallelTask)
	{
		int begin = parallelTask.getRange().getBegin();
		int end = parallelTask.getRange().getEnd();
		int grainSize = parallelTask.getRange().getGrainSize();

		Parallel parallel = parallelTask.getParallel();

		// Check if grainSize is greater than end make it equal to end
		if (grainSize > end)
		{
			grainSize = end;
		}
		// Check if grainSize is equal to end
		if (((end - begin) + 1) == grainSize)
		{
			// Set the task to Scheduler's processQueue.
			KorusRuntime.getNextCoreScheduler().setProcess(parallelTask);

		}
		// Check if task is splittable.
		else if (parallelTask.isSplittable())
		{
			// get the maximum parts possible
			maxChildCount = parallelTask.getMaxChildCount();
			ParallelTask pt = null;

			// iterate to the maxChildCount and split the bigger task into
			// smaller ones.
			for (int i = 0; i < maxChildCount; i++)
			{
				int newBegin = grainSize * i + begin;
				int newEnd = grainSize * (i + 1);
				newEnd = (end < newEnd) ? end : newEnd;

				// Create BlockRange with newBegin, newEnd, grainSize
				BlockedRange newRange = new BlockedRange(newBegin, newEnd,
						grainSize);

				// Create the objects as much the maxChildCount.
				pt = (ParallelTask) createObject(parallelTask.getClass()
						.getName(), newRange, parallel);
								 
				pt.putMessage(newRange);

				// Set the splitted task to Scheduler processQueue.
				KorusRuntime.getNextCoreScheduler().setProcess(pt);
			}
		}
	}

	/**
	 * Add the intermediateResults to resultList and set the result to the
	 * resultQueue when all parts done.
	 * 
	 * @param parallelTask  A sub-part of a task which has been executed.
	 * @param result Result of the sub-part execution.
	 */

	public synchronized void setResult(ParallelTask parallelTask, Object result)
	{
		resultList.add(result);
		executionCounter++;

		// Check for all the partial task to be completed
		if (executionCounter == maxChildCount)
		{
			try
			{
				resultQueue.put(parallelTask.summarize(resultList));
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create the Object of the given className using constructor with given
	 * blockedRange.
	 * 
	 * @param className
	 *            Name of the class of which the object to be created.
	 * @param newRange
	 *            BlockedRange to initialize the constructor of the class
	 * @param parallel
	 * @return the created Object of the class. 
	 */
	
	private static Object createObject(String className, BlockedRange newRange,
			Parallel parallel)
	{
		Object object = null;
		try
		{
			Class cls = Class.forName(className);
			Constructor ct = cls.getConstructor(Parallel.class,
					BlockedRange.class);
			object = ct.newInstance(parallel, newRange);
			
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * Shutdowns the system and cleanup the threads.
	 */
	public void cleanup()
	{
		System.exit(0);
	}

	private BlockingQueue<Object> resultQueue = new LinkedBlockingQueue<Object>();
	private ArrayList<Object> resultList = new ArrayList<Object>();
	private int maxChildCount = 1;
	private int executionCounter = 0;

}
