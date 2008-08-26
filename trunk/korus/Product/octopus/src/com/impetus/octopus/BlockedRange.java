/**
 * BlockedRange.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public class BlockedRange implements Cloneable{
	private int begin;
	private int end;
	private int grainSize;
	
	
	public BlockedRange(int begin, int end, int grainSize) {
		super();
		this.begin = begin;
		this.end = end;
		this.grainSize = grainSize;
	}


	public int getBegin() {
		return begin;
	}


	public void setBegin(int begin) {
		this.begin = begin;
	}


	public int getEnd() {
		return end;
	}


	public void setEnd(int end) {
		this.end = end;
	}


	public int getGrainSize() {
		return grainSize;
	}


	public void setGrainSize(int grainSize) {
		this.grainSize = grainSize;
	}
	
	public Object clone() throws CloneNotSupportedException {
		BlockedRange retVal = (BlockedRange)super.clone();
		return retVal;
	}
	
	public String toString(){
		return this.begin +"-"+this.end+","+this.grainSize;
	}
}
