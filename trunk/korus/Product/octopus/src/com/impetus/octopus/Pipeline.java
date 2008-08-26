package com.impetus.octopus;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import com.impetus.octopus.util.TaskList;

/**
 * 
 * @author sdutta
 *
 */
public class Pipeline {
	private static final Logger logger = Logger.getLogger(Pipeline.class.getName());
		
	/**
	 * 
	 * @param identity
	 */
	public Pipeline(String identity)
	{
		logger.entering(Pipeline.class.getName(), "service");
		this.identity = identity;
		taskList = new TaskList();
		logger.exiting(Pipeline.class.getName(), "service");
	}
	
	public void add(PipelineTask pipelineTask)
	{
		logger.entering(Pipeline.class.getName(), "add");
	// tasklist.add
		taskList.add(pipelineTask);
		logger.exiting(Pipeline.class.getName(), "add");
		
	}
	
	public void join(PipelineTask pipelineTaskProd, PipelineTask pipelineTaskCon){
		logger.entering(Pipeline.class.getName(), "join");
		// create new blocking queue
		OctopusQueue<Object> blockingQueue = new OctopusQueue<Object>();
		
		
	// associate it with the tasks input and output
		pipelineTaskProd.setOutputQueue(blockingQueue);
		pipelineTaskProd.setNextTask(pipelineTaskCon);
		
		pipelineTaskCon.setInputQueue(blockingQueue);
		pipelineTaskCon.setPreviousTask(pipelineTaskProd);
		
		logger.exiting(Pipeline.class.getName(), "join");
	}
	
	public void execute()
	{
		logger.entering(Pipeline.class.getName(), "execute");
		taskList.execute();
		logger.exiting(Pipeline.class.getName(), "execute");
	}
	
	public void cleanup(){
		taskList.cleanup();
	}
	// create an instance of tasklist
	
	private TaskList taskList = null;
	String identity = null;
	
}
