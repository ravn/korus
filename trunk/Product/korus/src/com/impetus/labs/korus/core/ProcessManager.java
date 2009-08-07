/**
 * ProcessManager.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import com.impetus.labs.korus.exception.KeyNotFoundException;
import com.impetus.labs.korus.exception.KorusException;
import com.impetus.labs.korus.exception.ProcessAlreadyExistsException;

public class ProcessManager
{
	
	
	private static boolean isWebRequest = false;

	private static int numberOfNewRequestExecuters = 0;

	private static int numberOfNewRequestSchedulers = 0;

	private static int numberOfCoreSchedulers = 1;

	private static int numberOfCoreExecuters = 1;

	private static int numberOfCPUCores = 0;

	private static HashMap<String, Process> registeredProcessMap = new HashMap<String, Process>();

	private static Executer[] coreExecuterList = null;

	private static Scheduler[] coreSchedulerList = null;

	private static NewRequestExecuter[] newRequestExecuterList = null;

	private static NewRequestScheduler[] newRequestSchedulerList = null;

	private static int index = 0;

	static
	{
		// Determine no. of cores and start coreExecuters
		numberOfCPUCores = Runtime.getRuntime().availableProcessors();

		numberOfCoreExecuters = numberOfCPUCores;

		try
		{
			readPropertiesFile();
		} catch (Exception e)
		{

			e.printStackTrace();
		}

		// Start the Core Executers/Schedulers
		coreExecuterList = new Executer[numberOfCoreExecuters];
		for (int i = 0; i < coreExecuterList.length; i++)
		{
			coreExecuterList[i] = new Executer();
			coreExecuterList[i].start();
		}

		coreSchedulerList = new Scheduler[numberOfCoreSchedulers];
		for (int i = 0; i < coreSchedulerList.length; i++)
		{
			coreSchedulerList[i] = new Scheduler();
			coreSchedulerList[i].start();
		}
		if (isWebRequest)
		{
			// Start the newRequest Executers/Schedulers
			newRequestExecuterList = new NewRequestExecuter[numberOfNewRequestExecuters];
			for (int i = 0; i < newRequestExecuterList.length; i++)
			{
				newRequestExecuterList[i] = new NewRequestExecuter();
				newRequestExecuterList[i].start();
			}

			newRequestSchedulerList = new NewRequestScheduler[numberOfNewRequestSchedulers];
			for (int i = 0; i < newRequestSchedulerList.length; i++)
			{
				newRequestSchedulerList[i] = new NewRequestScheduler();
				newRequestSchedulerList[i].start();
			}
		}
	}

	/**
	 * Get the next executer from the coreExecuterList.
	 * 
	 * @return the executer from the coreExecuterList.
	 */
	public static Executer getNextExecuter()
	{
		Executer executer = coreExecuterList[index % numberOfCoreExecuters];
		index++;
		return executer;
	}

	/**
	 * Get the next scheduler from the coreSchedulerList.
	 * 
	 * @return the scheduler from the coreSchedulerList.
	 */
	public static Scheduler getNextCoreScheduler()
	{
		Scheduler scheduler = coreSchedulerList[index % numberOfCoreSchedulers];
		index++;
		return scheduler;
	}

	/**
	 * Get the next newRequestExecuter from the newRequestExecuterList.
	 * 
	 * @return the next newRequestExecuter from the newRequestExecuterList.
	 */
	public static NewRequestExecuter getNextNewRequestExecuter()
	{
		NewRequestExecuter globalExecuter = newRequestExecuterList[index
				% numberOfNewRequestExecuters];
		index++;
		return globalExecuter;
	}

	/**
	 * Get the next newRequestScheduler from the newRequestSchedulerList.
	 * 
	 * @return the next newRequestScheduler from the newRequestSchedulerList.
	 */
	public static NewRequestScheduler getNextGlobalScheduler()
	{
		NewRequestScheduler globalScheduler = newRequestSchedulerList[index
				% numberOfNewRequestSchedulers];
		index++;
		return globalScheduler;
	}

	/**
	 * Start the Process with registeredProcessName to handle the new requests.
	 * 
	 * @param processName
	 *            Name of the process by which it is registered in the
	 *            registeredProcessMap.
	 * @param message
	 *            Message to be passed to the process contains ('KEY','VALUE')
	 *            pairs.
	 */

	public static void start(String processName, Message message)
	{
		// 1. Select process from registered process list
		Process process = getRegisteredProcess(processName);
		if (process == null)
		{
			System.out
					.println("Message sent to an unregistered Process..........."
							+ processName);
		} else
		{
			// 2. Put message in process's messageQueue
			process.putMessage(message);
			// 3. Put this Process in newRequestSchedulers processQueue
			NewRequestScheduler.setProcess(process);
		}

	}

	/**
	 * Start the Process using processID to handle the new requests.
	 * 
	 * @param process
	 *            process to which to be started.
	 * @param message
	 *            Message to be passed to the process contains ('KEY','VALUE')
	 *            pairs.
	 */

	public static void start(Process process, Message message)
	{
		// 1. Put message in process's messageQueue
		process.putMessage(message);
		// 2. Put this Process in newRequestSchedulers processQueue
		NewRequestScheduler.setProcess(process);
	}

	/**
	 * Send message to a registered process.
	 * 
	 * @param processName
	 *            Name of the process by which it is registered in the
	 *            registeredProcessMap.
	 * @param message
	 *            Message to be passed to the process contains ('KEY','VALUE')
	 *            pairs.
	 */
	public synchronized static void send(String processName, Message message)
	{
		// 1. Select process from registered process list
		Process process = getRegisteredProcess(processName);
		if (process == null)
		{
			System.out
					.println("Message sent to an unregistered Process..........."
							+ processName);
		} else
		{
			// 2. Put message in process's messageQueue
			process.putMessage(message);
			// 3. Put this Process in Schedulers processQueue
			Scheduler.setProcess(process);
		}

	}

	/**
	 * Send message to a process using(processID).
	 * 
	 * @param process
	 *            process to which message to be sent.
	 * @param message
	 *            Message to be passed to the process contains ('KEY','VALUE')
	 *            pairs.
	 */
	public static void send(Process process, Message message)
	{
		// 1. Put message in process's messageQueue
		process.putMessage(message);
		// 2. Put this Process in Schedulers processQueue
		Scheduler.setProcess(process);
	}

	/**
	 * Get the Process from registeredProcessMap if registered.
	 * 
	 * @param processName
	 *            Name of the process by which it is registered in the
	 *            registeredProcessMap.
	 * @return the Process from registeredProcessMap if registered else returns
	 *         null.
	 */
	public static Process getRegisteredProcess(String processName)
	{
		return registeredProcessMap.get(processName);
	}

	/**
	 * Register the Process with unique name in the registeredProcessMap.
	 * 
	 * @param processName
	 *            processName a unique name of the process by which the process
	 *            to be register in the registeredProcessMap.
	 * @param process
	 *            The process which is to be registered with a unique name.
	 * @throws ProcessAlreadyExistsException
	 */
	public static void registerProcess(String processName, Process process)
			throws ProcessAlreadyExistsException
	{
		if (registeredProcessMap.get(processName) == null)
			registeredProcessMap.put(processName, process);
		else
			throw new ProcessAlreadyExistsException(processName);

	}

	/**
	 * Method to read the properties file to initialize the configuration
	 * parameters.
	 * 
	 * @throws KeyNotFoundException
	 * @throws KorusException
	 */

	public static void readPropertiesFile() throws KeyNotFoundException,
			KorusException
	{
		try
		{
			String korusHome = System.getenv("KORUS_HOME");
			
			File propertiesFile = new File(korusHome+"/properties/korus.properties");

			Properties properties = new Properties();
			FileInputStream fileInputStream = new FileInputStream(
					propertiesFile);
			properties.load(fileInputStream);

			// Check for Kind of request if WEB_REQUEST='true'
			// and if KEYS not found or incorrect initialization
			// Initialize the advance processing parameters with default value 1

			if (properties.containsKey("WEB_REQUEST"))
			{
				isWebRequest = Boolean.parseBoolean(properties
						.getProperty("WEB_REQUEST"));
				if (isWebRequest)
				{
					if (properties
							.containsKey("NUMBER_OF_NEW_REQUEST_EXECUTERS"))
					{
						numberOfNewRequestExecuters = Integer
								.parseInt(properties
										.getProperty("NUMBER_OF_NEW_REQUEST_EXECUTERS"));
						if (numberOfNewRequestExecuters <= 0)
						{
							numberOfNewRequestExecuters = 1;
							throw new KorusException(
									"The value of parameter NUMBER_OF_NEW_REQUEST_EXECUTERS must be greater than 0\n"
											+ "setting it to default value 1");
						}
					} else
						throw new KeyNotFoundException(
								"NUMBER_OF_NEW_REQUEST_EXECUTERS");

					if (properties
							.containsKey("NUMBER_OF_NEW_REQUEST_SCHEDULERS"))
					{
						numberOfNewRequestSchedulers = Integer
								.parseInt(properties
										.getProperty("NUMBER_OF_NEW_REQUEST_SCHEDULERS"));
						if (numberOfNewRequestSchedulers <= 0)
						{
							numberOfNewRequestSchedulers = 1;
							throw new KorusException(
									"The value of parameter NUMBER_OF_NEW_REQUEST_SCHEDULERS must be greater than 0\n"
											+ "setting it to default value 1");
						}
					} else
						throw new KeyNotFoundException(
								"NUMBER_OF_NEW_REQUEST_SCHEDULERS");
				}
			}

			// Check for NUMBER_OF_CORE_SCHEDULERS if not found or incorrect
			// initialization start with default value 1.

			if (properties.containsKey("NUMBER_OF_CORE_SCHEDULERS"))
			{
				numberOfCoreSchedulers = Integer.parseInt(properties
						.getProperty("NUMBER_OF_CORE_SCHEDULERS"));

				if (numberOfCoreSchedulers <= 0)
				{
					numberOfCoreSchedulers = 1;
					throw new KorusException(
							"The value of parameter NUMBER_OF_CORE_SCHEDULERS must be greater than 0\n"
									+ "setting it to default value 1");
				}
			} else
				throw new KeyNotFoundException("NUMBER_OF_CORE_SCHEDULERS");

			// Check for NUMBER_OF_CORE_EXECUTERS if not found or incorrect
			// initialization start with default value equals to the number Of
			// CPUCores

			if (properties.containsKey("NUMBER_OF_CORE_EXECUTERS"))
			{
				numberOfCoreExecuters = Integer.parseInt(properties
						.getProperty("NUMBER_OF_CORE_EXECUTERS"));
				if (numberOfCoreExecuters <= 0)
				{
					numberOfCoreExecuters = numberOfCPUCores;
					throw new KorusException(
							"The value of parameter NUMBER_OF_CORE_EXECUTERS must be greater than 0\n"
									+ "setting it to default value equals to the number Of CPUCores");

				}
			} else
				throw new KeyNotFoundException("NUMBER_OF_CORE_EXECUTERS");
		} catch (NumberFormatException e)
		{
			System.out.println(e.getMessage());
			throw new KorusException(
					"Properties File Format error!\nDefine parameters(Case-sensitive) in (KEY=VALUE)format where KEY is a 'String' and VALUE is an 'integer'");
		}

		catch (IOException e)
		{
			System.out.println(e.getMessage());
			throw new KorusException(
					"Properties File not found!\nStarting with default configuration");

		}
	}
}
