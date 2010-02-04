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
package com.impetus.labs.korus.test.spearsman;

import java.util.Iterator;
import java.util.List;

import com.impetus.labs.korus.addons.constructs.parallelfor.BlockedRange;
import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;
import com.impetus.labs.korus.addons.constructs.parallelfor.ParallelTask;

/**
 * Simple demo program to demonstrate use of parallelFor construct by
 * implementing ParallelSpearmans calculation.
 */
public class ParallelSpearmans extends ParallelTask
{

	private static RowOfCC[] rcc = null;

	/**
	 * Constructor of ParallelSpearmans which initializes the constructor of
	 * parallelTask with the given parallel object and blockedRange.
	 * 
	 * @param parallel
	 *            Parallel Construct Object
	 * 
	 * @param range
	 *            Specified blockedRange.
	 */

	public ParallelSpearmans(Parallel parallel, BlockedRange range)
	{
		super(parallel, range);
	}

	/**
	 * Constructor of ParallelSpearmans which initializes the constructor of
	 * parallelTask with the given parallel object, blockedRange and rccArray.
	 * 
	 * @param parallel
	 *            Parallel Construct Object
	 * 
	 * @param range
	 *            Specified blockedRange.
	 * @param rccArray
	 *            It is the sample dataArray generated on which the Spearman
	 *            formula will be applied and calculation will be done.
	 */
	public ParallelSpearmans(Parallel parallel, BlockedRange range,
			RowOfCC[] rccArray)
	{
		super(parallel, range);
		this.rcc = rccArray;
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
		double diSquare = 0.0;

		// Iterate the loop from begin to end over a given range
		for (int i = range.getBegin(); i < range.getEnd(); i++)
		{
			diSquare += Math.pow((rcc[i].rx - rcc[i].ry), 2);
		}

		// Set the intermediate result in a variable to be
		// returned for each iteration

		getParallel().setResult(this, diSquare);
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
	public Object summarize(List<Object> intermediateResults)
	{
		double finalDiSquare = 0.0;

		// Iterate over the intermediate results and summarize the results
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();)
		{
			finalDiSquare += ((Double) iter.next()).doubleValue();
		}

		// Return the final result
		double rho = 1 - (6 * finalDiSquare)
				/ (rcc.length * (Math.pow((rcc.length), 2) - 1));
		return rho;
	}

}
