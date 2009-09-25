/**
 * PipelineTask.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.addons.constructs.pipeline;

import com.impetus.labs.korus.core.Message;
import com.impetus.labs.korus.core.Process;

/**
 * Only those classes can be added to the Pipeline which inherit this class
 */
public abstract class PipelineTask extends Process {
	/**
	 * service method should be implemented by all Pipeline Tasks
	 */
	public abstract void service(Message msg);

	/**
	 * To get the inputQueue of the pipelineTask.
	 * @return the inputQueue of the pipelineTask.
	 */
	public PipelineQueue<Object> getInputQueue() {
		return inputQueue;
	}

	/**
	 * Sets the input queue of this pipeline task. It is a queue which takes the 
	 * output of a previous pipeline task as its input. 
	 * @param inputQueue An Object  
	 */
	public void setInputQueue(PipelineQueue<Object> inputQueue) {
		this.inputQueue = inputQueue;
	}

	/**
	 * Gets the outputQueue of the pipelineTask. The ouputQueue of this Task is
	 * the input Queue of the next class.
	 * @return A KorusQueue Object
	 */
	public PipelineQueue<Object> getOutputQueue() {
		return outputQueue;
	}

	/**
	 * Sets the queue as the outputQueue of the PipelineTask. 
	 * @param outputQueue
	 */
	public void setOutputQueue(PipelineQueue<Object> outputQueue) {
		this.outputQueue = outputQueue;
	}

	/**
	 * Gets the next pipelineTask to execute.
	 * @return nextTask i.e. the next pipelineTask
	 */
	public PipelineTask getNextTask() {
		return nextTask;
	}

	/**
	 * Sets the pipelineTask as the nextTask of this pipelineTask.
	 * @param nextTask i.e. the next pipelineTask
	 */

	public void setNextTask(PipelineTask nextTask) {
		this.nextTask = nextTask;
	}

	/**
	 * Gets the previous pipelineTask to execute.
	 * @return previousTask i.e. the next previousTask
	 */
	public PipelineTask getPreviousTask() {
		return previousTask;
	}

	/**
	 * Sets the pipelineTask as the previousTask of this pipelineTask.
	 * @param previousTask i.e. the next previousTask
	 */
	public void setPreviousTask(PipelineTask previousTask) {
		this.previousTask = previousTask;
	}

	private PipelineQueue<Object> inputQueue = null;

	private PipelineQueue<Object> outputQueue = null;

	private PipelineTask nextTask = null;

	private PipelineTask previousTask = null;

}
