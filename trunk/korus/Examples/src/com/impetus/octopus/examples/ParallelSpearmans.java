/**
 * ParallelSpearmans.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

// This class is used by ParallelSpearmansTest
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
public class ParallelSpearmans extends ParallelTask {

	RowOfCC[] rcc = null;
	
	// implement a constructor.
	public ParallelSpearmans(BlockedRange range){
		super(range);
	}
	
	public ParallelSpearmans(BlockedRange range, RowOfCC[] rcc){
		super(range);
		this.rcc = rcc;
	}
	
	// implement a service method
	public void service() 
	{		
			double diSquare = 0;
			
			// iterate the loop from begin to end over a given range 
			for (int i = this.getRange().getBegin(); i < this.getRange().getEnd(); i++) 
			{
				diSquare += Math.pow((rcc[i].rx - rcc[i].ry), 2);
			}
			// set the intermediate result in a variable to be returned 
			// for each iteration
			this.setResult(diSquare);	
	}
		
		// implement a summarize method.
		public Object summarize(List<Object> intermediateResults){
		double finalDiSquare =  0.0;
		
		// iterate over the intermediate results and summarize the results 
		for (Iterator iter = intermediateResults.iterator(); iter.hasNext();) 
		{
			finalDiSquare += ((Double)iter.next()).doubleValue();
		}
		// return the final result
		double rho = 1 - (6 * finalDiSquare)
		/ (rcc.length * (Math.pow((rcc.length), 2) - 1));
		return rho;
	}
		
}


