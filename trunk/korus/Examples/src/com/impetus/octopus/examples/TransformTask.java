/**
 * 
 */
package com.impetus.octopus.examples;

import java.util.logging.Logger;

import com.impetus.octopus.OctopusQueue;
import com.impetus.octopus.ParallelTask;
import com.impetus.octopus.PipelineTask;

/**
 * @author sdutta
 *
 */
public class TransformTask extends PipelineTask {

	private static final Logger logger = Logger.getLogger(TransformTask.class
			.getName());

	/**
	 * 
	 */
	public TransformTask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void service() {
		logger.entering(TransformTask.class.getName(), "service");
		String s = null;

		//System.out.println(" - this.hasStarted(): "+this.hasStarted());
		logger.finest("this.getPreviousTask().hasStarted(): "
				+ this.getPreviousTask().hasStarted());
		logger.finest("this.getPreviousTask().isDone(): "
				+ this.getPreviousTask().isDone());
		
		OctopusQueue<Object> inputQ = this.getInputQueue();
		OctopusQueue<Object> outputQ = this.getOutputQueue();
		
		
		try {
			// the previous task should be started
			this.getPreviousTask().hasStarted();
			
			// input queue is not empty
			while(inputQ.isEmpty() == false){
				String str = (String)inputQ.remove();
				str = str.toUpperCase();
				outputQ.put(str);
				
			}
			
			// previous task should not be over
			while(this.getPreviousTask().isDone() == false || inputQ.isEmpty() == false){
				if(inputQ.isEmpty() == false){
				String str = (String)inputQ.take();
				str = str.toUpperCase();
				outputQ.put(str);
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		
		logger.exiting(TransformTask.class.getName(), "service");
	}

}
