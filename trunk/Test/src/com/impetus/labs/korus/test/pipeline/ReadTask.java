/**
 * ReadTask.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.impetus.labs.korus.addons.constructs.pipeline.PipelineQueue;
import com.impetus.labs.korus.addons.constructs.pipeline.PipelineTask;
import com.impetus.labs.korus.core.Message;

/**
 * 
 * This class Reads data from a file in chunks and passes on the the
 * next task i.e. Transform Task in this case
 *
 */
public class ReadTask extends PipelineTask
{

	// Used to specify the end condition
	public static final String END_OF_TASK = "~_~END_OF_Line~_~";

	// Override the service method present in PipelineTask
	public void service(Message msg)
	{
		File f = new File("../files/testFile.txt");
		PipelineQueue<Object> outputQ = this.getOutputQueue();

		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = null;

			while ((line = reader.readLine()) != null)
			{
				outputQ.add(line);
			}

			outputQ.add(END_OF_TASK);

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
