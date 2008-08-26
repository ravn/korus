/**
 * ParallelSpearmansTest.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

package com.impetus.octopus.examples;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */

import com.impetus.octopus.BlockedRange;
import com.impetus.octopus.Parallel;

public class ParallelSpearmansTest {

	// Simple demo to demonstrate Parallel For construct 
	public static void main(String[] args) {
		
		// Create an instance of RowOfCC and generate a dataset 
		RowOfCC rcc = new RowOfCC();
		RowOfCC[] rccArray = rcc.setup();
		
		// After data is generated actual value of 
		long initalTime = System.currentTimeMillis();

		if (rccArray != null && rccArray.length >= 1) {
			// Create an object of the construct to be used
			Parallel parallel = new Parallel();

			//	Specify begin, end and grainSize according to the machine
			int blockedRangeBegin = 0;
			int blockedRangeEnd = rccArray.length;
			int blockedRangeGrainSize = rccArray.length / 2 + 1;

			BlockedRange range = new BlockedRange(blockedRangeBegin,
					blockedRangeEnd, blockedRangeGrainSize);

			// Create an object of a class extending ParallelTask Class
			ParallelSpearmans ps = new ParallelSpearmans(range, rccArray);

			try {
				// Pass the object to the parallelFor construct
				Object obj = parallel.parallelFor(ps);
				System.out.println("Rho = " + ((Double) obj).doubleValue());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Shutdown the pool if you do not need it
			parallel.cleanup();
		}
		long finalTime = System.currentTimeMillis();
		System.out.println("Time Taken: " + (finalTime - initalTime));
	}

}
