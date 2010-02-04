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

import com.impetus.labs.korus.addons.constructs.parallelfor.BlockedRange;
import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;

/**
 * TestClass to run ParallelSpearmans.
 * 
 * 
 */
public class ParallelSpearmansTest
{

	public static void main(String[] args)
	{

		// Create an instance of RowOfCC and generate a dataset
		RowOfCC rcc = new RowOfCC();
		RowOfCC[] rccArray = rcc.setup();

		// After data is generated actual value of
		long initalTime = System.currentTimeMillis();

		// Create an object of the construct to be used
		Parallel parallel = new Parallel();

		if (rccArray != null && rccArray.length >= 1)
		{

			// Specify begin, end and grainSize according to the machine
			int begin = 1;
			int end = rccArray.length;
			int grainSize = rccArray.length / 4;

			BlockedRange range = new BlockedRange(begin, end, grainSize);

			// Create an object of a class extending ParallelTask Class
			ParallelSpearmans ps = new ParallelSpearmans(parallel, range,
					rccArray);

			try
			{
				// Pass the object to the parallelFor construct
				parallel.parallelFor(ps);

				// Get the finalResult of execution
				Object obj = parallel.getResult();
				System.out.println("The value of Rho = "
						+ ((Double) obj).doubleValue());

			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		long finalTime = System.currentTimeMillis();
		System.out.println("Time Taken by ParallelSpearmansTest : "
				+ (finalTime - initalTime));

		// ShutDown when task done.
		parallel.cleanup();

	}

}
