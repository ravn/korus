package com.impetus.octopus;

import java.util.List;
import java.util.logging.Logger;


import com.impetus.octopus.util.TaskList;

public abstract class PipelineTask extends ParallelTask {

	private static final Logger logger = Logger.getLogger(PipelineTask.class.getName());
	
	public PipelineTask()
	{
		this.status = QueueStatus.NOT_STARTED;
	}
		
@Override
	public final Object summarize(List<Object> intermediateResults) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public OctopusQueue<Object> getInputQueue() {
		return inputQueue;
		
	}

	public void setInputQueue(OctopusQueue<Object> inputQueue) {
		this.inputQueue = inputQueue;
	}
									
	public OctopusQueue<Object> getOutputQueue() {
		return outputQueue;
	}

	public void setOutputQueue(OctopusQueue<Object> outputQueue) {
		this.outputQueue = outputQueue;
	}
	
	public void setTaskDone(boolean done)
	 {
		 this.status = QueueStatus.DONE;
	 }
	 
	 public boolean isDone()
	 {
		 return (this.status == QueueStatus.DONE);
	 }
	 
	 public void setTaskStarted() {
		 this.status = QueueStatus.STARTED;
		 
	 }
	 
	 public boolean hasStarted() {
		 synchronized(this){
		 while(status == QueueStatus.NOT_STARTED)
		 {
			 try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 }
		 return ((status == QueueStatus.STARTED) || (status == QueueStatus.DONE));
	 }
	 
	 public PipelineTask getNextTask() {
			return nextTask;
		}

		public void setNextTask(PipelineTask nextTask) {
			this.nextTask = nextTask;
		}

		public PipelineTask getPreviousTask() {
			return previousTask;
		}

		public void setPreviousTask(PipelineTask previousTask) {
			this.previousTask = previousTask;
		}
	 
	private QueueStatus status = QueueStatus.NOT_STARTED;
	public static enum QueueStatus { NOT_STARTED, STARTED, DONE}
	private OctopusQueue<Object> inputQueue = null;
	private OctopusQueue<Object> outputQueue = null;
	
	
	private PipelineTask nextTask = null;
	private PipelineTask previousTask = null;

	
	
	
	
}
