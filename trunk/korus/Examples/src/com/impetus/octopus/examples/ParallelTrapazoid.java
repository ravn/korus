/**
 * ParallelTrapazoid.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

// This class is used by ParallelTrapazoidTest

package com.impetus.octopus.examples;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */

import java.util.Iterator;
import java.util.List;

import com.impetus.octopus.BlockedRange;
import com.impetus.octopus.ParallelTask;

// Extend ParallelTask
public class ParallelTrapazoid extends ParallelTask {

	private double result = 0.0;
	
	// implement a constructor.
	public ParallelTrapazoid(BlockedRange range){
		super(range);
		step = 1.0/(double) 100000000;;
		sum =0.0;
	}
	
	// implement a service method
	public void service() 
	{
				    		    	
	    	// iterate the loop from begin to end over a given range 
			for (int i = this.getRange().getBegin(); i < this.getRange().getEnd(); i++) 
			{
				 x = (i+0.5)*step;
	             sum = sum + 5.0/(1.0+x*x);
			}
			// set the intermediate result in a variable to be returned 
			// for each iteration
			this.setResult(sum);	
	}
		
		// implement a summarize method.
		public Object summarize(List<Object> intermediateResults){
			
		// iterate over the intermediate results and summarize the results 
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();) 
		{
				double temporarySum = ((Double)iter.next()).doubleValue();
				result += temporarySum;
		}
				
		double pi = step * result;
		
		// return the final result
		System.out.println("Pi :"+pi);
		return pi;
	}
		
		double step = 0;
		double sum = 0;
    	double x = 0;
		
}

