/**
 * ParallelFactorial.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.factorial;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import com.impetus.labs.korus.constructs.parallelFor.Parallel;
import com.impetus.labs.korus.constructs.parallelFor.ParallelTask;
import com.impetus.labs.korus.util.BlockedRange;

/**
 * Simple demo program to demonstrate use of parallelFor construct by
 * implementing ParallelFactorial calculation.
 */
public class ParallelFactorial extends ParallelTask
{
	private static BigInteger finalfact = BigInteger.ONE;

	/**
	 * Constructor of ParallelFactorial which initializes the constructor of
	 * parallelTask with the given parallel object and blockedRange.
	 * 
	 * @param parallel
	 * 
	 * @param range
	 *            Specified blockedRange.
	 */
	// Change - Implement a constructor with Parallel
	public ParallelFactorial(Parallel parallel, BlockedRange range)
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
		BigInteger fact = BigInteger.ONE;

		// Iterate the loop from begin to end over a given range
		for (int i = range.getBegin(); i <= range.getEnd(); i++)
		{
			fact = fact.multiply(BigInteger.valueOf(i));
		}
		getParallel().setResult(this, fact);

	}

	/**
	 * Override the summarize method implementation and it should return the
	 * final result which will be the cumulative result of all the
	 * intermediateResults from resultList.
	 * @param intermediateResults List containing the intermediate Results
	 * @return Object finalResult object.
	 */
	public Object summarize(List<Object> intermediateResults)
	{
		BigInteger factorial = BigInteger.ONE;

		// Iterate over the intermediate results and summarize the results
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();)
		{
			factorial = (BigInteger) iter.next();
			finalfact = finalfact.multiply(factorial);
		}
		// Return the final result
		return finalfact;
	}

}
