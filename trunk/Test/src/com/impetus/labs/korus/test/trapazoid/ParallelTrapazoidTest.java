/**
 * ParallelTrapazoidTest.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.trapazoid;

import com.impetus.labs.korus.constructs.parallelFor.Parallel;
import com.impetus.labs.korus.util.BlockedRange;

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
		int grainSize = end / 16;

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
