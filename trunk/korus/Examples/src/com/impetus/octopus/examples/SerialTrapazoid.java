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

/**
 * Program to calculate the area under curve using Trapezoidal Method
 */
public class SerialTrapazoid {
	// number of steps the curve should be divided. Bigger the number, more 
	// accurate will be the value of the area under curve 
	static long num_steps = 100000000; 
	
	public static void main(String[] args) {
	
		long startTime = System.currentTimeMillis();
		
		double step;
		int i;
		double x, pi, sum = 0.0;

		 step = 1.0/(double) num_steps;

         for (i=0;i<num_steps; i++){
        	 
        	 x = (i+0.5)*step;
             sum = sum + 5.0/(1.0+x*x);
         }

         pi = step * sum;
         
         long endTime = System.currentTimeMillis();

         System.out.println("Result:" + pi);
                  System.out.println("Time Taken :"+(endTime-startTime));
	}
}

