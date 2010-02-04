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
package com.impetus.labs.korus.test.trapazoid;

/**
 * Program to calculate the area under curve using Trapezoidal Method
 */
public class SerialTrapazoid
{
	// number of steps the curve should be divided. Bigger the number, more
	// accurate will be the value of the area under curve
	static long num_steps = 1000000000;

	public static void main(String[] args)
	{

		long startTime = System.currentTimeMillis();

		double step;
		int i;
		double x, pi, sum = 0.0;

		step = 1.0 / (double) num_steps;

		for (i = 0; i < num_steps; i++)
		{
			x = (i + 0.5) * step;
			sum = sum + 5.0 / (1.0 + x * x);
		}

		pi = step * sum;

		long endTime = System.currentTimeMillis();

		System.out.println("Pi: " + pi);
		System.out.println("Time Taken by SerialTrapazoid :"
				+ (endTime - startTime));
	}
}
