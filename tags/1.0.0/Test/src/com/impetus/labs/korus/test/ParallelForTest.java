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
package com.impetus.labs.korus.test;

import junit.framework.TestCase;

import com.impetus.labs.korus.addons.constructs.parallelfor.BlockedRange;
import com.impetus.labs.korus.addons.constructs.parallelfor.Parallel;

/**
 * A test class containing methods to check the functionality of single
 * parallelFor and multipleParallelFor.
 * 
 */
public class ParallelForTest extends TestCase
{
	/**
	 * Test the single parallelFor working if test pass represent parallelFor
	 * working correctly.
	 */
	public void testSingleParallelFor()
	{

		int start = 1;
		int end = 10;
		int grainSize = 5;
		BlockedRange range = new BlockedRange(start, end, grainSize);

		Parallel parallel = new Parallel();
		SummationTask task = new SummationTask(parallel, range);
		parallel.parallelFor(task);
		Object obj = parallel.getResult();
		assertTrue(obj instanceof Integer);
		assertEquals(55, ((Integer) obj).intValue());

	}

	/**
	 * Test the multiple parallelFor working if test pass represent more than
	 * one parallelFor working correctly.
	 */
	public void testMultipleParallelFor()
	{
		int start1 = 1;
		int end1 = 10;
		int grainSize1 = 5;
		BlockedRange range1 = new BlockedRange(start1, end1, grainSize1);
		Parallel parallel1 = new Parallel();

		int start2 = 1;
		int end2 = 12;
		int grainSize2 = 6;

		BlockedRange range2 = new BlockedRange(start2, end2, grainSize2);
		Parallel parallel2 = new Parallel();

		SummationTask task1 = new SummationTask(parallel1, range1);
		SummationTask task2 = new SummationTask(parallel2, range2);

		parallel1.parallelFor(task1);
		parallel2.parallelFor(task2);

		Object obj1 = parallel1.getResult();
		assertTrue(obj1 instanceof Integer);
		assertEquals(55, ((Integer) obj1).intValue());

		Object obj2 = parallel2.getResult();
		assertTrue(obj2 instanceof Integer);
		assertEquals(78, ((Integer) obj2).intValue());

	}

}
