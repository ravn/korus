/**
 * ParallelFactorial.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

// This class is used by ParallelFactorialTest
package com.impetus.octopus.examples;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import com.impetus.octopus.BlockedRange;
import com.impetus.octopus.ParallelTask;

// Extend ParallelTask
public class ParallelFactorial extends ParallelTask {

	private static BigInteger finalfact = BigInteger.ONE;
	
	// implement a constructor.
	public ParallelFactorial(BlockedRange range){
		super(range);
	}
	
	// implement a service method
	public void service() 
	{
			// Set the fact variable to 1 initially.
	    	BigInteger fact =  BigInteger.ONE;
	    	
	    	// iterate the loop from begin to end over a given range 
			for (int i = this.getRange().getBegin(); i <= this.getRange().getEnd(); i++) 
			{
				// write the logic to calculate factorial
				fact = fact.multiply(BigInteger.valueOf(i));
			}
			// set the intermediate result in a variable to be returned 
			// for each iteration
			
			this.setResult(fact);	
	}
		
		// implement a summarize method.
		public Object summarize(List<Object> intermediateResults){
		BigInteger factorial =  BigInteger.ONE;
		// iterate over the intermediate results and summarize the results 
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();) 
		{
				factorial = (BigInteger)iter.next();
				finalfact = finalfact.multiply(factorial);
		}
		// return the final result
		System.out.println(finalfact);
		return finalfact;
	}
		
}
