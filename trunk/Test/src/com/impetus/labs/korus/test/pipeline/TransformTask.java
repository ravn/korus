/**
 * TransformTask.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.pipeline;

import com.impetus.labs.korus.addons.constructs.pipeline.PipelineQueue;
import com.impetus.labs.korus.addons.constructs.pipeline.PipelineTask;
import com.impetus.labs.korus.core.Message;

/**
 * TransformTask is used for transforming the string to upperCase which is read
 * from the file by ReadTask.
 */
public class TransformTask extends PipelineTask
{

	// Used to specify the end condition of execution
	public static final String END_OF_TASK = "~_~END_OF_Line~_~";

	// Override the service method present in PipelineTask
	public void service(Message message)
	{
		// This task has both a preceding task and a succeeding task hence
		// it will have an inputQueue as well as an outputQueue. InputQueue
		// of this task is outPutQueue of the preceding task and outputQueue
		// of this task is the inputQueue for the succeeding
		PipelineQueue<Object> inputQ = this.getInputQueue();
		PipelineQueue<Object> outputQ = this.getOutputQueue();

		try
		{
			// remove the element from the Queue
			String str = (String) inputQ.take();
			do
			{
				// Do the transformation
				str = str.toUpperCase();
				// Put it back in the outputQueue
				outputQ.put(str);
				// remove the element from the Queue again
				str = (String) inputQ.take();

			} while (str != END_OF_TASK);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// to specify end condition of the execution.
		outputQ.add(END_OF_TASK);
	}
}
