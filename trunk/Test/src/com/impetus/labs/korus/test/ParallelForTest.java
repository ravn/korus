/**
 * ParallelForTest.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test;

import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;
import com.impetus.labs.korus.util.BlockedRange;

/**
 * A test class containing methods to check the functionality of single
 * parallelFor and multipleParallelFor.
 * 
 */
public class ParallelForTest
{
	public static boolean isTestSingleParallelForDone= false;
	public static boolean isTestMultipleParallelForDone= false;
	
	public static void main(String[] args)
	{
		
		ParallelForTest pft = new ParallelForTest();

		pft.testSingleParallelFor();
		pft.testMultipleParallelFor();
		
		// shutdown the execution when done.
		if(isTestSingleParallelForDone&&isTestMultipleParallelForDone)
		{
			System.exit(0);
		}
		
	}

	/**
	 * Test the single parallelFor working if test pass represent parallelFor
	 * working correctly.
	 */
	public boolean testSingleParallelFor()
	{

		int start = 1;
		int end = 10;
		int grainSize = 5;
		BlockedRange range = new BlockedRange(start, end, grainSize);

		Parallel parallel = new Parallel();
		SummationTask task = new SummationTask(parallel, range);
		parallel.parallelFor(task);
		Object obj = parallel.getResult();
		if ((obj instanceof Integer) && ((Integer) obj).intValue() == 55)
		{
			System.out.println("testSingleParallelFor() is successful.");
		} else
			System.out.println("testSingleParallelFor() failed");
		
		return isTestSingleParallelForDone=true;
	}

	/**
	 * Test the multiple parallelFor working if test pass represent more than
	 * one parallelFor working correctly.
	 */
	public boolean testMultipleParallelFor()
	{
		boolean isLoop1Passed = false;
		boolean isLoop2Passed = false;

		int start1 = 1;
		int end1 = 10;
		int grainSize1 = 5;
		BlockedRange range1 = new BlockedRange(start1, end1, grainSize1);
		Parallel parallel1 = new Parallel();

		int start2 = 1;
		int end2 = 12;
		int grainSize2 = 6;

		BlockedRange range2 = new BlockedRange(start2, end2, grainSize2);
		Parallel parallel2 = new Parallel();

		SummationTask task1 = new SummationTask(parallel1, range1);
		SummationTask task2 = new SummationTask(parallel2, range2);

		parallel1.parallelFor(task1);
		parallel2.parallelFor(task2);

		Object obj1 = parallel1.getResult();
		if ((obj1 instanceof Integer) && ((Integer) obj1).intValue() == 55)
		{
			isLoop1Passed = true;
		}
		Object obj2 = parallel2.getResult();
		if ((obj2 instanceof Integer) && ((Integer) obj2).intValue() == 78)
		{
			isLoop2Passed = true;
		}
		if (isLoop1Passed && isLoop2Passed)
		{
			System.out.println("testMultipleParallelFor() is successful.");
		} else
			System.out.println("testMultipleParallelFor() failed");

		return isTestMultipleParallelForDone=true;
	}
	
}
