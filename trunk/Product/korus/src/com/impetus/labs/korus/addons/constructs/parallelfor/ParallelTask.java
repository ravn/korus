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

import java.util.List;

import com.impetus.labs.korus.core.message.RawMessage;
import com.impetus.labs.korus.core.process.BaseProcess;

/**
 * ParallelTask is used to implement constructs like parallelFor etc.
 * It denotes a task which can be executed in Parallel.
 * 
 */
public abstract class ParallelTask extends BaseProcess
{
	/**
	 * Constructor for creating a new ParallelTask.
	 * 
	 * @param parallel
	 *            parallel is a object of Parallel.
	 * @param range
	 *            Specified blockedRange.
	 */
	public ParallelTask(Parallel parallel, BlockedRange range)
	{
		this.parallel = parallel;
		this.range = range;
		
	}


	/**
	 * Override the service(rawMessage) method of BaseProcess so that classes extending
	 * ParallelTask implement the service(BlockedRange) method, thereby
	 * suppressing the use of service(rawMessage) method.
	 */
	public void service(RawMessage rawMessage)
	{
		BlockedRange range = (BlockedRange)rawMessage;
		service(range);
	}

	/**
	 * Executes the task defined with in the method. It has to be implemented by
	 * the programmer extending ParallelTask
	 * 
	 * @param range
	 *            Given BlockedRange.
	 */
	public abstract void service(BlockedRange range);

	/**
	 * Summarizes the intermediateResults of each part of a task and returns the
	 * result.
	 * 
	 * @param intermediateResults
	 *            List containing results of each sub-part of task execution.
	 * @return object Final result.
	 */
	public abstract Object summarize(List<Object> intermediateResults);

	/**
	 * Get the BlockedRange.
	 * 
	 * @return The blockedRange.
	 */
	public BlockedRange getRange()
	{
		return range;
	}

	/**
	 * Sets the BlockedRange to the range.
	 * 
	 * @param range
	 *            Specified blockedRange.
	 */
	public void setRange(BlockedRange range)
	{
		this.range = range;
	}

	/**
	 * Checks that whether the Task can be split in smaller parts for execution.
	 * 
	 * @return <code>true</code> if task is can be split else returns
	 *         <code>false</code>
	 */
	public boolean isSplittable()
	{
		boolean retVal = (this.range.getGrainSize() < (this.range.getEnd()
				- this.range.getBegin() + 1));
		return retVal;
	}

	/**
	 * Calculates the maximum number of sub task(child) of a task.
	 * 
	 * @return The number of maximum sub task can be split from the task
	 */
	public int getMaxChildCount()
	{
		int retVal = (this.range.getEnd() - this.range.getBegin() + 1)
				/ this.range.getGrainSize();
		retVal += ((this.range.getEnd() - this.range.getBegin() + 1) % this.range
				.getGrainSize()) > 0 ? 1 : 0;
		return retVal;
	}

	/**
	 * @return The Parallel Object
	 */
	public Parallel getParallel() {
		return parallel;
	}


	/**
	 * @param parallel The Parallel to set
	 */
	public void setParallel(Parallel parallel) {
		this.parallel = parallel;
	}
	
	private BlockedRange range;
	private Parallel parallel = null;
	
}
