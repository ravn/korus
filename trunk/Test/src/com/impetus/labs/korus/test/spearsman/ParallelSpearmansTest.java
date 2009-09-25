/**
 * ParallelSpearmansTest.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

package com.impetus.labs.korus.test.spearsman;

import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;
import com.impetus.labs.korus.util.BlockedRange;

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
