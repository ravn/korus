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
package com.impetus.labs.korus.test.spearsman;

/**
 * Helper class to denote a row created using the dataset
 */
public class RowOfCC
{

	public static final int SIZE_OF_DATA = 10000000;
	public static final int SORT_BY_XI = 1;
	public static final int SORT_BY_YI = 2;

	public int x;
	public int y;
	public int rx = -1;
	public int ry = -1;

	public String toString()
	{
		return x + "\t " + y + "\t " + rx + "\t " + ry;
	}

	/**
	 * Compares two rows according to the field specified
	 */
	static class RowOfCCComparator implements java.util.Comparator
	{
		String field;

		public RowOfCCComparator(String field)
		{
			this.field = field;
		}

		public int compare(Object object1, Object object2)
		{
			RowOfCC rowOfCC1 = (RowOfCC) object1;
			RowOfCC rowOfCC2 = (RowOfCC) object2;
			if (field.equals("x"))
				return rowOfCC1.x - rowOfCC2.x;
			else if (field.equals("y"))
				return rowOfCC1.y - rowOfCC2.y;
			throw new RuntimeException("no such field " + field);
		}
	}

	/**
	 * Function to create a Random Dataset and generating values for Xi and Yi
	 * 
	 * @return Array of pairs of Xi and and Yi
	 */
	public RowOfCC[] makeRowOfCCs()
	{
		RowOfCC[] rowOfCC = new RowOfCC[SIZE_OF_DATA];
		for (int i = 0; i < SIZE_OF_DATA; i++)
		{
			rowOfCC[i] = new RowOfCC();
			rowOfCC[i].x = (int) (Math.random() * 1000);
			rowOfCC[i].y = (int) (Math.random() * 1000);
		}

		return rowOfCC;
	}

	/**
	 * Print Rows
	 * 
	 * @param rows
	 *            Rows to be printed
	 */
	static void printRowOfCCs(RowOfCC[] t)
	{
		System.out.println("Xi\t " + "Yi\t " + "rxi\t " + "ryi");
		for (int i = 0; i < t.length; i++)
		{
			System.out.println(t[i]);
		}
	}

	/**
	 * Rank each row
	 * 
	 * @param row
	 *            Array of rows
	 * @param field
	 *            Field according to which the Rows need to be ranked
	 */
	static void rankRowOfCCs(RowOfCC[] rowOfCC, int field)
	{

		if (field == 1)
			for (int i = 0; i < rowOfCC.length; i++)
			{
				rowOfCC[i].rx = i + 1;
			}
		else
			for (int i = 0; i < rowOfCC.length; i++)
			{
				rowOfCC[i].ry = i + 1;
			}

	}

	/**
	 * Intialize the Rows
	 * 
	 * @return Rows Array
	 */
	public RowOfCC[] setup()
	{
		RowOfCC[] rowOfCC = makeRowOfCCs();

		java.util.Arrays.sort(rowOfCC, new RowOfCCComparator("x"));
		rankRowOfCCs(rowOfCC, SORT_BY_XI);

		java.util.Arrays.sort(rowOfCC, new RowOfCCComparator("y"));
		rankRowOfCCs(rowOfCC, SORT_BY_YI);

		java.util.Arrays.sort(rowOfCC, new RowOfCCComparator("x"));
		rankRowOfCCs(rowOfCC, SORT_BY_XI);

		return rowOfCC;
	}
}
