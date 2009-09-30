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

package com.impetus.labs.korus.test.factorial;

import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;
import com.impetus.labs.korus.util.BlockedRange;

/**
 * Test Class to run ParallelFactorial.
 * 
 *
 */
public class ParallelFactorialTest
{

	public static void main(String[] args)
	{
		long initialTime, finalTime;
		initialTime = System.currentTimeMillis();

		// Create an object of the construct to be used
		Parallel parallel = new Parallel();

		// Specify begin, end and grainSize according to the machine
		int begin = 1;
		int end = 40000;
		int grainSize = 250;

		BlockedRange range = new BlockedRange(begin, end, grainSize);

		// Create an object of a class extending ParallelTask Class
		ParallelFactorial af = new ParallelFactorial(parallel, range);

		// Pass the object to the parallelFor construct
		parallel.parallelFor(af);

		// Get the finalResult of execution
		Object obj = parallel.getResult();

		finalTime = System.currentTimeMillis();
		System.out.println("Total Time Taken by ParallelFactorialTest : "
				+ (finalTime - initialTime));
		

		// ShutDown when task done.
		parallel.cleanup();

	}

}
