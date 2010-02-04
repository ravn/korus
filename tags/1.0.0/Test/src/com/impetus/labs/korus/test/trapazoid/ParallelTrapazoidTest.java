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

import com.impetus.labs.korus.addons.constructs.parallelfor.BlockedRange;
import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;

/**
 * TestClass to run ParallelTrapazoid.
 * 
 * 
 */
public class ParallelTrapazoidTest
{

	public static void main(String[] args)
	{

		long startTime = System.currentTimeMillis();

		// Create an object of the construct to be used
		Parallel parallel = new Parallel();

		// specify begin, end and grainSize according to the machine
		int begin = 1;
		// number of steps the curve should be divided. Bigger the number, more
		// accurate will be the value of the area under curve.
		// Therefore, blockedRangeEnd = num_steps(in SerialTrapezoid) =
		// 100000000;
		int end = 1000000000;

		// grainSize equal to blockedRangeEnd/2 to enable two threads
		int grainSize = end/4;

		BlockedRange range = new BlockedRange(begin, end, grainSize);

		// Create an object of a class extending ParallelTask Class
		ParallelTrapazoid pt = new ParallelTrapazoid(parallel, range);
		try
		{
			// Pass the object to the parallelFor construct
			parallel.parallelFor(pt);

			// Get the finalResult of execution
			Object object = parallel.getResult();

			System.out.println("AreaUnderCurve: "
					+ ((Double) object).doubleValue());
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Total Time Taken :" + (endTime - startTime));

		// ShutDown when task done.
		parallel.cleanup();

	}

}
