/**
 * ParallelTrapazoidTest.java
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

public class ParallelTrapazoidTest {
    
	// Simple demo to demonstrate Parallel For construct 
	public static void main(String[] args) {
    	
		long startTime = System.currentTimeMillis();
		
		// Create an object of the construct to be used
    	Parallel parallel = new Parallel();
    	int blockedRangeBegin = 0;
    	
    	// number of steps the curve should be divided. Bigger the number, more 
    	// accurate will be the value of the area under curve.
    	// Therefore, blockedRangeEnd = num_steps(in SerialTrapezoid) = 100000000;
    	int blockedRangeEnd = 100000000;
    	
    	// grainSize equal to blockedRangeEnd/2 to enable two threads
    	int grainSize = 100000000/1;
    	
    	// specify begin, end and grainSize according to the machine
    	BlockedRange range= new BlockedRange(blockedRangeBegin,blockedRangeEnd,grainSize);
    	
    	// Create an object of a class extending ParallelTask Class
    	ParallelTrapazoid pt = new ParallelTrapazoid(range);
    	try
		{
    		// pass the object to the parallelFor construct
			Object object = parallel.parallelFor(pt);			
		}
    	catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
    	catch (Exception e)
		{
			e.printStackTrace();
		}     
    	    	
    	// Shutdown the pool if you do not need it
    	parallel.cleanup();
    	
    	long endTime = System.currentTimeMillis();
    	System.out.println("Time Taken :"+ (endTime-startTime));
    }    
	
} 

