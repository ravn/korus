/**
 * ThreadPool.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.impetus.octopus.ParallelTask;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public class ThreadPool {
	
	private static OctopusPoolExecutor pool = null;
	private static final Logger logger = Logger.getLogger(ThreadPool.class.getName());
	
	private static void initializeThreadPool(){
		
		pool = new OctopusPoolExecutor(20,20,120 , TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
	}
	
	public static OctopusPoolExecutor getThreadPool(){
		if(pool == null){
			initializeThreadPool();
		}
		return pool;
	}

}
