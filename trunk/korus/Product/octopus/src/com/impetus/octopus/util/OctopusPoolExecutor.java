/**
 * 
 */
package com.impetus.octopus.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.impetus.octopus.ParallelTask;
import com.impetus.octopus.PipelineTask;

/**
 * @author sdutta
 * 
 */
public class OctopusPoolExecutor extends ThreadPoolExecutor {

	private static final Logger logger = Logger.getLogger(ParallelTask.class
			.getName());

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 */
	public OctopusPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 */
	public OctopusPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				threadFactory);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param handler
	 */
	public OctopusPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				handler);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 * @param handler
	 */
	public OctopusPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
			RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				threadFactory, handler);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param t
	 * @param r
	 */
	protected void beforeExecute(Thread t, Runnable r) {
		logger.entering(OctopusPoolExecutor.class.getName(), "beforeExecute");
		
		if (r instanceof PipelineTask) {
			((PipelineTask) r).setTaskStarted();
			
		}
		
		logger.exiting(OctopusPoolExecutor.class.getName(), "beforeExecute");
	}

	/**
	 * 
	 * @param t
	 * @param r
	 */
	protected void afterExecute(Runnable r, Throwable t) {

		logger.entering(OctopusPoolExecutor.class.getName(), "afterExecute");
		if (r instanceof PipelineTask) {
		((PipelineTask) r).setTaskDone(true);
		logger.fine("TaskDone" + ((PipelineTask) r).isDone());
		}
		
		logger.exiting(OctopusPoolExecutor.class.getName(), "afterExecute");

	}

}
