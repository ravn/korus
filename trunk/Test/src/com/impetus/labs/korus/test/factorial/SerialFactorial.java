/**
 * SerialFactorial.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.factorial;

import java.math.BigInteger;
/**
 * Serial implementation of factorial of a number using a loop.
 * e.g. Here the number is 40000.
 * 
 */
public class SerialFactorial
{
	
	public static void main(String[] args)
	{

		// -- BigInteger solution.
		long initialTime = System.currentTimeMillis();
		BigInteger n = BigInteger.ONE;
		int i = 1;
		int number = 40000;

		for (i = 1; i <= number; i++)
		{
			n = n.multiply(BigInteger.valueOf(i));
		}
		// System.out.println("##### "+n);

		long endTime = System.currentTimeMillis();
		System.out.println("TimeTaken by SerialFactorial  : "
				+ (endTime - initialTime));

	}
}
