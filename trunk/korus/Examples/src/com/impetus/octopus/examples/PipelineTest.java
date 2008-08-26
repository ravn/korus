/**
 * PipelineTest.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.examples;
/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */
import com.impetus.octopus.Pipeline;

/**
 * Test class for Pipeline
 *
 */
public class PipelineTest {

	
	public static void main(String[] args) {
		
		// Create a Pipeline
		Pipeline pipeline = new Pipeline("Pipeline Test");
		long startTime = System.currentTimeMillis();
		
		// Create objects Tasks which extend PipelineTasks
		ReadTask readTask = new ReadTask();
		TransformTask transformTask = new TransformTask();
		WriteTask writeTask = new WriteTask();
		
		// Add these tasks to the pipeline
		pipeline.add(readTask);
		pipeline.add(transformTask);
		pipeline.add(writeTask);
		
		// Join these tasks in order to know the order of execution of the tasks
		pipeline.join(readTask, transformTask);
		pipeline.join(transformTask, writeTask);
		
		// Execute the Pipeline
		pipeline.execute();
		
		// Clean up the Pipeline
		pipeline.cleanup();		
		
		long endTime = System.currentTimeMillis();
		System.out.println("Total Time :" + (endTime-startTime) );
	}

}
