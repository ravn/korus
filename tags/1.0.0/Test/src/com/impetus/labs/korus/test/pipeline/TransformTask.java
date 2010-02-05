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

import com.impetus.labs.korus.addons.constructs.pipeline.PipelineQueue;
import com.impetus.labs.korus.addons.constructs.pipeline.PipelineTask;

/**
 * TransformTask is used for transforming the string to upperCase which is read
 * from the file by ReadTask.
 */
public class TransformTask extends PipelineTask
{

	// Used to specify the end condition of execution
	public static final String END_OF_TASK = "~_~END_OF_Line~_~";

	// Override the service method present in PipelineTask
	public void service()
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
