/**
 * ParallelFactorialTest.java
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
import java.math.BigInteger;

public class ParallelFactorialTest {
    
	// Simple demo to demonstrate Parallel For construct 
	public static void main(String[] args) {
    	
		// Create an object of the construct to be used
    	Parallel parallel = new Parallel();
    	
    	// specify begin, end and grainSize according to the machine
    	BlockedRange range= new BlockedRange(1,10,2);
    	
    	// Create an object of a class extending ParallelTask Class
    	ParallelFactorial pf = new ParallelFactorial(range);
    	try
		{
    		// pass the object to the parallelFor construct
			Object obj = parallel.parallelFor(pf);
			BigInteger b = (BigInteger)obj;
			// System.out.println(b);
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
    }    
	
} 

