/**
 * ParallelTask.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public abstract class ParallelTask implements Runnable,Cloneable {
	
	    private static final Logger logger = Logger.getLogger(ParallelTask.class.getName());
		/*
		 * The range on which this parallel task should operate on
		 */
		private BlockedRange range;
		
		private Object result;
		
		public ParallelTask(){
		}
		
		public ParallelTask(BlockedRange range){
			this.range = range;
		}
	
		public abstract void service();
		
		public abstract Object summarize(List<Object> intermediateResults);
		
		public void run(){
			logger.entering(ParallelTask.class.getName(), "run");
			service();
			synchronized(this){
				this.notify();	
			}
			logger.exiting(ParallelTask.class.getName(), "run");
		}

		public BlockedRange getRange() {
			return range;
		}

		public void setRange(BlockedRange range) {
			this.range = range;
		}

		public synchronized Object getResult() {
			return result;
		}

		public synchronized void setResult(Object result) {
			logger.entering(ParallelTask.class.getName(), "setResult");
			this.result = result;
			logger.exiting(ParallelTask.class.getName(), "setResult");
		}
		
		public boolean isSplittable(){
			logger.entering(ParallelTask.class.getName(), "isSplittable");
			boolean retVal  =(this.range.getGrainSize() < (this.range.getEnd() - this.range.getBegin() + 1));
			logger.fine("Returning "+retVal+ " as "+this.range.getGrainSize()+" is not less than "+(this.range.getEnd() - this.range.getBegin() + 1));
			return retVal;
		}
		
		public int getMaxChildCount(){
			int retVal = (this.range.getEnd() - this.range.getBegin() +1) /this.range.getGrainSize();
			retVal += ((this.range.getEnd() - this.range.getBegin() +1) % this.range.getGrainSize())>0?1:0;
			return retVal;
		}
		
		public Object clone() throws CloneNotSupportedException {
			ParallelTask retVal = (ParallelTask)super.clone();
			
			retVal.range = (BlockedRange)range.clone();
			
			return retVal;
		}
}
