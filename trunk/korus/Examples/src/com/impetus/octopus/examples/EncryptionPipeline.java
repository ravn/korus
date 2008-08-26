/**
 * EncryptionPipeline.java
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
public class EncryptionPipeline {

	
	public static void main(String[] args) {
		
		
		// Create a Pipeline
		Pipeline pipeline = new Pipeline("Pipeline Test");
		
		// Create objects Tasks which extend PipelineTasks
		ReadTask readTask = new ReadTask();
		EncryptTask encryptTask = new EncryptTask();
		WriteTask writeTask = new WriteTask();
		
		// Add these tasks to the pipeline
		pipeline.add(readTask);
		pipeline.add(encryptTask);
		pipeline.add(writeTask);
		
		// Join these tasks in order to know the order of execution of the tasks
		pipeline.join(readTask, encryptTask);
		pipeline.join(encryptTask, writeTask);
		
		// Execute the Pipeline
		pipeline.execute();
		
		// Clean up the Pipeline
		pipeline.cleanup();		

	}

}
