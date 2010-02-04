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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.impetus.labs.korus.addons.constructs.pipeline.PipelineQueue;
import com.impetus.labs.korus.addons.constructs.pipeline.PipelineTask;

/**
 * 
 * This class Reads data from a file in chunks and passes on the the next task
 * i.e. Transform Task in this case
 * 
 */
public class ReadTask extends PipelineTask
{

	// Used to specify the end condition
	public static final String END_OF_TASK = "~_~END_OF_Line~_~";

	// Override the service method present in PipelineTask
	public void service()
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
