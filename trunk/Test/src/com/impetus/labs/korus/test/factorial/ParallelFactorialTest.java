/**
 * ParallelFactorialTest.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
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
