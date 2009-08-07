/**
 * PipelineTest.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.test.pipeline;

import com.impetus.labs.korus.constructs.pipeline.Pipeline;

/**
 * A Program to demonstrate the working of the pipeline.
 * 
 */
public class PipelineTest
{

	public static void main(String args[])
	{

		long initialTime, finalTime;
		initialTime = System.currentTimeMillis();

		// Create the object of the pipeline.
		Pipeline pipeline = new Pipeline("pipeline test");

		// Create objects Tasks which extend PipelineTasks
		ReadTask task1 = new ReadTask();
		TransformTask task2 = new TransformTask();
		WriteTask task3 = new WriteTask();

		// Add these tasks to the pipeline
		pipeline.add("readTask", task1);
		pipeline.add("transformTask", task2);
		pipeline.add("writeTask", task3);

		// Join these tasks in order to know the order of execution of the tasks
		pipeline.join(task1, task2);
		pipeline.join(task2, task3);

		// Execute the Pipeline
		pipeline.execute();

		// To wait till execution lasts. Skip this line if you do not want to
		// wait for the execution of pipeline to finish.
		pipeline.getResult();

		finalTime = System.currentTimeMillis();
		System.out.println("Time Taken by ParallelRTW: "
				+ (finalTime - initialTime));
		
		//shutdown when task is over
		pipeline.cleanUp();

	}



}
