/**
 * 
 */
package com.impetus.octopus.examples;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.impetus.octopus.OctopusQueue;
import com.impetus.octopus.ParallelTask;
import com.impetus.octopus.PipelineTask;


/**
 * @author sdutta
 *
 */
public class ReadTask extends PipelineTask {

	private static final Logger logger = Logger.getLogger(ReadTask.class.getName());
	
	/**
	 * 
	 */
	
	public ReadTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void service() {
		logger.entering(ReadTask.class.getName(), "service");

		File f = new File("c:/testFile.txt");
		OctopusQueue<Object> outputQ = this.getOutputQueue();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line = null;

			while ((line = reader.readLine()) != null) {
				outputQ.add(line);					
			}
			
			/*for (Object object: this.getOutputQueue()) {					
				
				System.out.println("ReadTask - outputQueue: " +(String)object);
			}*/
			
			//this.getOutputQueue().setTaskDone(true);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.exiting(ReadTask.class.getName(), "service");

}
}
