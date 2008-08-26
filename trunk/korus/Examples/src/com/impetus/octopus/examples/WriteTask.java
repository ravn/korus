/**
 * WriteTask.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.examples;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import com.impetus.octopus.OctopusQueue;
import com.impetus.octopus.PipelineTask;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */

//Used by PipelineTest
//Extend the PipelineTask
public class WriteTask extends PipelineTask {

	// No argument constructor of the WriteTask	
	public WriteTask() {

	}

	// Overide the service method present in PipelineTask
	public void service() {

		// Since this is the last Task to be executed, it will just have
		// one inputQueue and no outputQueue.
		OctopusQueue<Object> inputQ = this.getInputQueue();
		BufferedWriter out = null;

		try {
			
			// output file
			out = new BufferedWriter(new FileWriter("C:\\writeFile.txt"));

			// the previous task should be started
			this.getPreviousTask().hasStarted();

			// Condition 1: input queue is not empty
			while (inputQ.isEmpty() == false) {
				// read date from the Queue
				String str = (String) inputQ.remove();
				// write it using the BufferedWriter
				out.write(str + "\r" + "\n");
			}

			// Condition 2: previous task should not be over
			while (this.getPreviousTask().isDone() == false
					|| inputQ.isEmpty() == false) {
				String str = null;
				if (inputQ.isEmpty() == false) {
					// read date from the Queue
					str = (String) inputQ.remove();
					// write it using the BufferedWriter
					out.write(str + "\r" + "\n");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
