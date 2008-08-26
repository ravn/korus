/**
 * RowOfCC.java
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
 * Class to denote a row created using the dataset
 */
public class RowOfCC {
	
	public static final int SIZE_OF_DATA = 1000000;
	public static final int SORT_BY_XI = 1;
	public static final int SORT_BY_YI = 2;
	
	public int x;
	public int y;
	public int rx = -1;
	public int ry = -1;

	public String toString() {
		return x + "\t " + y + "\t " + rx + "\t " + ry;
	}
	
	/**
	 * Compares two rows according to the field specified
	 */ 
	static class RowOfCCComparator implements java.util.Comparator {
		String field;

		public RowOfCCComparator(String field) {
			this.field = field;
		}

		public int compare(Object o1, Object o2) {
			RowOfCC t1 = (RowOfCC) o1;
			RowOfCC t2 = (RowOfCC) o2;
			if (field.equals("x"))
				return t1.x - t2.x;
			else if (field.equals("y"))
				return t1.y - t2.y;
			throw new RuntimeException("no such field " + field);
		}
	}
		
	/**
	 * Function to create a Random Dataset and generating values
	 * for Xi and Yi
	 * @return Array of pairs of Xi and and Yi
	 */
	public RowOfCC[] makeRowOfCCs() {
		RowOfCC[] t = new RowOfCC[SIZE_OF_DATA];
		for (int i = 0; i < SIZE_OF_DATA; i++) {
			t[i] = new RowOfCC();
			t[i].x = (int)(Math.random()*1000);
			t[i].y = (int)(Math.random()*1000);
		}
         
       return t;
   }

	/**
	 * Print Rows
	 * @param rows Rows to be printed
	 */
   static void printRowOfCCs(RowOfCC[] t) {
	   System.out.println("Xi\t " + "Yi\t " + "rxi\t " +"ryi");
		for (int i = 0; i < t.length; i++) {
			System.out.println(t[i]);
		}
	}

   /**
	 * Rank each row
	 * @param row	Array of rows
	 * @param field	Field according to which the Rows need to be ranked
	 */      
   static void rankRowOfCCs(RowOfCC[] t, int field) {

		if (field == 1)
			for (int i = 0; i < t.length; i++) {
				t[i].rx = i + 1;
			}
		else
			for (int i = 0; i < t.length; i++) {
				t[i].ry = i + 1;
			}

	}
   
   public RowOfCC[] setup()
   {
	    RowOfCC[] rowOfCC = makeRowOfCCs();
		
	    java.util.Arrays.sort(rowOfCC, new RowOfCCComparator("x"));
		rankRowOfCCs(rowOfCC, SORT_BY_XI);
		//printRowOfCCs(rowOfCC);
		
		java.util.Arrays.sort(rowOfCC, new RowOfCCComparator("y"));
		rankRowOfCCs(rowOfCC, SORT_BY_YI);
		//printRowOfCCs(rowOfCC);

		java.util.Arrays.sort(rowOfCC, new RowOfCCComparator("x"));
		rankRowOfCCs(rowOfCC, SORT_BY_XI);
		//System.out.println("Processed Data\n");
		//printRowOfCCs(rowOfCC);
		
		return rowOfCC;
   }
}