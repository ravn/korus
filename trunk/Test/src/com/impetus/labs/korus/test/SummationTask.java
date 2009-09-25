/**
 * SummationTask.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test;

import java.util.Iterator;
import java.util.List;

import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;
import com.impetus.labs.korus.addons.constructs.parallelfor.ParallelTask;
import com.impetus.labs.korus.util.BlockedRange;

/**
 * Simple demo program to demonstrate use of parallelFor construct by
 * implementing summation of numbers between start number to end number
 * specified.
 */
public class SummationTask extends ParallelTask
{

	/**
	 * Constructor of SummationTask which initializes the constructor of
	 * parallelTask with the given parallel object and blockedRange.
	 * 
	 * @param parallel Parallel Construct Object
	 * 
	 * @param range
	 *            Specified blockedRange.
	 */
	public SummationTask(Parallel parallel, BlockedRange range)
	{
		super(parallel, range);
	}

	/**
	 * Overrides the service method implementation and executes the task for a
	 * given blockedRange and sets the intermediateResults to the resultList.
	 * 
	 * @param range
	 *            Specified blockedRange.
	 */
	public void service(BlockedRange range)
	{
		int sum = 0;
		for (int i = range.getBegin(); i <= range.getEnd(); i++)
		{
			sum += i;
		}
		getParallel().setResult(this, new Integer(sum));
	}

	/**
	 * Override the summarize method implementation and it should return the
	 * final result which will be the cumulative result of all the
	 * intermediateResults from resultList.
	 * 
	 * @param intermediateResults List containing the intermediate Results
	 * @return Object finalResult object.
	 */
	public Object summarize(List<Object> intermediateResults)
	{
		int sum = 0;
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();)
		{
			Integer element = (Integer) iter.next();
			sum += element.intValue();
		}
		return new Integer(sum);
	}

}
