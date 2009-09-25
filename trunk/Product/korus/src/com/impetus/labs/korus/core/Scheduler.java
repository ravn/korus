/**
 * Scheduler.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Scheduler is responsible of taking a process from its processQueue, 
 * then getting an executer from list and set the process to the executer's
 * processQueue.
 */
public class Scheduler extends Thread {

	public void run() {
		while (true) {
			// 1. Pick a Process
			Process process = getProcess();
			// 2. Select a Executer
			Executer executer = KorusRuntime.getNextExecuter();
			// 3. Set the process to the executer processQueue
			executer.setProcess(process);
		}
	}

	/**
	 * Sets a Process to the Scheduler's processQueue.
	 * 
	 * @param process
	 *            process is the smallest part of any execution.
	 */
	public static void setProcess(Process process) {
		try {
			processQueue.put(process);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a Process from the Scheduler's processQueue.
	 * 
	 * @return the<code> process</code> from Scheduler's
	 *         <code>processQueue</code>.
	 */
	public static Process getProcess() {
		try {
			return processQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static BlockingQueue<Process> processQueue = new LinkedBlockingQueue<Process>();

}
