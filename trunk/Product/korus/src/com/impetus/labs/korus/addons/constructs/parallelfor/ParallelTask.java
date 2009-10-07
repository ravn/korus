/**
 * ParallelTask.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.addons.constructs.parallelfor;

import java.util.List;

import com.impetus.labs.korus.core.Message;
import com.impetus.labs.korus.core.Process;
import com.impetus.labs.korus.util.BlockedRange;

/**
 * ParallelTask is used to implement constructs like parallelFor etc.
 * It denotes a task which can be executed in Parallel.
 * 
 */
public abstract class ParallelTask extends Process
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
	 * Override the service(Message) method of Process so that classes extending
	 * ParallelTask implement the service(BlockedRange) method, thereby
	 * suppressing the use of service(Message) method.
	 */
	public void service(Message msg)
	{
	};

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
	 * Gets the BlockedRange.
	 * 
	 * @return the blockedRange.
	 */
	public BlockedRange getRange()
	{
		return range;
	}

	/**
	 * Sets the BlockedRange to the range.
	 * 
	 * @param range
	 *            specified blockedRange.
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
	 * @return the number of maximum sub task can be split from the task
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
	 * @return the parallel
	 */
	public Parallel getParallel() {
		return parallel;
	}


	/**
	 * @param parallel the parallel to set
	 */
	public void setParallel(Parallel parallel) {
		this.parallel = parallel;
	}
	
	private BlockedRange range;
	private Parallel parallel = null;
	
}
