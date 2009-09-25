/**
 * WriteTask.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.pipeline;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.impetus.labs.korus.addons.constructs.pipeline.PipelineQueue;
import com.impetus.labs.korus.addons.constructs.pipeline.PipelineTask;
import com.impetus.labs.korus.core.Message;

/**
 * 
 * WriteTask is used for writing the transformed string to the file.
 * 
 */
public class WriteTask extends PipelineTask
{

	public static final String END_OF_TASK = "~_~END_OF_Line~_~";

	// Override the service method present in PipelineTask
	public void service(Message message)
	{

		// get the input and output Queues
		PipelineQueue<Object> inputQ = this.getInputQueue();
		PipelineQueue<Object> outputQ = this.getOutputQueue();

		BufferedWriter out = null;

		try
		{

			// output file
			out = new BufferedWriter(new FileWriter("../files/writeFile.txt"));
			// get data from the queue.
			String str = (String) inputQ.take();
			do
			{
				// write the data to file.
				out.write(str + "\r" + "\n");
				str = (String) inputQ.take();
			} while (str != END_OF_TASK);

			// Put the End Result in the Queue (if Any)
			outputQ.put("Execution Result");

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				out.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}

}