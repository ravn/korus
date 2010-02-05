/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2010 Impetus Technologies, Inc.(http://www.impetus.com)
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Korus.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.impetus.labs.korus.test.factorial;

import java.math.BigInteger;

/**
 * Serial implementation of factorial of a number using a loop. e.g. Here the
 * number is 40000.
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
