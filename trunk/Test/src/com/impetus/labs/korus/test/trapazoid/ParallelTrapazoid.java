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
package com.impetus.labs.korus.test.trapazoid;

import java.util.Iterator;
import java.util.List;

import com.impetus.labs.korus.addons.constructs.parallelfor.BlockedRange;
import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;
import com.impetus.labs.korus.addons.constructs.parallelfor.ParallelTask;

/**
 * Simple demo program to demonstrate use of parallelFor construct by
 * implementing ParallelTrapazoid calculation.
 */
public class ParallelTrapazoid extends ParallelTask
{
	private double result = 0.0;
	private double step = 0;
	private double sum = 0;
	private double x = 0;

	/**
	 * Constructor of ParallelTrapazoid which initializes the constructor of
	 * parallelTask with the given parallel object and blockedRange.
	 * 
	 * @param parallel
	 *            Parallel Construct Object
	 * 
	 * @param range
	 *            Specified blockedRange.
	 */
	public ParallelTrapazoid(Parallel parallel, BlockedRange range)
	{
		super(parallel, range);
		step = 1.0 / (double) 1000000000;
		sum = 0.0;
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
		sum = 0.0;

		// Iterate the loop from begin to end over a given range
		for (int i = range.getBegin(); i < range.getEnd(); i++)
		{
			x = (i + 0.5) * step;
			sum = sum + 5.0 / (1.0 + x * x);
		}
		// Set the intermediate result in a variable to be
		// returned for each iteration

		getParallel().setResult(this, sum);

	}

	/**
	 * Override the summarize method implementation and it should return the
	 * final result which will be the cumulative result of all the
	 * intermediateResults from resultList.
	 * 
	 * @param intermediateResults
	 *            List containing the intermediate Results
	 * @return Object finalResult object.
	 */
	@Override
	public Object summarize(List<Object> intermediateResults)
	{
		// Iterate over the intermediate results and summarize the results
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();)
		{
			double temporarySum = ((Double) iter.next()).doubleValue();
			result += temporarySum;
		}
		double pi = step * result;

		// Return the final result
		return pi;
	}

}
