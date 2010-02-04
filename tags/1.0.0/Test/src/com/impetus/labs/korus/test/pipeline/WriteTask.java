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
package com.impetus.labs.korus.test.pipeline;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.impetus.labs.korus.addons.constructs.pipeline.PipelineQueue;
import com.impetus.labs.korus.addons.constructs.pipeline.PipelineTask;

/**
 * 
 * WriteTask is used for writing the transformed string to the file.
 * 
 */
public class WriteTask extends PipelineTask
{

	public static final String END_OF_TASK = "~_~END_OF_Line~_~";

	// Override the service method present in PipelineTask
	public void service()
	{

		// get the input and output Queues
		PipelineQueue<Object> inputQ = this.getInputQueue();
		PipelineQueue<Object> outputQ = this.getOutputQueue();

		BufferedWriter out = null;

		try
		{
			// output file
			out = new BufferedWriter(new FileWriter(
					"../files/parallelWriteFile.txt"));
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
