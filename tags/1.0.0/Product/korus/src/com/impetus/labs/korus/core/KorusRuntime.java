/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2009 Impetus Technologies, Inc.(http://www.impetus.com/)
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * 
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *    
 * You should have received a copy of the GNU General Public License
 * along with Korus.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.impetus.labs.korus.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Properties;

import com.impetus.labs.korus.exception.KeyNotFoundException;
import com.impetus.labs.korus.exception.KorusException;
import com.impetus.labs.korus.exception.ProcessAlreadyExistsException;
import com.impetus.labs.korus.util.StringUtil;

/**
 * KorusRuntime is used to initialize Korus Runtime which includes initializing
 * Executers, Schedulers and also managing communication between Processes and
 * Nodes.
 */
public class KorusRuntime
{
	private static final int JAVA_PORT = 7935;

	private static final int ERLANG_PORT = 7936;

	private static boolean isWebRequest = false;

	private static boolean isDistributed = false;

	private static int numberOfNewRequestExecuters = 0;

	private static int numberOfNewRequestSchedulers = 0;

	private static int numberOfCoreSchedulers = 1;

	private static int numberOfCoreExecuters = 0;

	private static int numberOfCPUCores = 0;

	private static HashMap<String, Process> registeredProcessMap = new HashMap<String, Process>();

	private static Executer[] coreExecuterList = null;

	private static Scheduler[] coreSchedulerList = null;

	private static NewRequestExecuter[] newRequestExecuterList = null;

	private static NewRequestScheduler[] newRequestSchedulerList = null;

	private static int index = 0;

	private static HashMap<String, BufferedWriter> connectedNodesMap = new HashMap<String, BufferedWriter>();

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

		// Web request scenario handling
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

		// InterNode Interaction and Cross Language Compatibility
		if (isDistributed)
		{
			// Start KourusReader Thread
			Runnable korusReader = new KorusReader();
			Thread korusReaderThread = new Thread(korusReader);
			korusReaderThread.start();

			// Start KorusWriter Thread
			Runnable korusWriter = new KorusWriter();
			Thread korusWriterThread = new Thread(korusWriter);
			korusWriterThread.start();

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
		NewRequestExecuter newRequestExecuter = newRequestExecuterList[index
				% numberOfNewRequestExecuters];
		index++;
		return newRequestExecuter;
	}

	/**
	 * Get the next newRequestScheduler from the newRequestSchedulerList.
	 * 
	 * @return the next newRequestScheduler from the newRequestSchedulerList.
	 */
	public static NewRequestScheduler getNextNewRequestScheduler()
	{
		NewRequestScheduler newRequestScheduler = newRequestSchedulerList[index
				% numberOfNewRequestSchedulers];
		index++;
		return newRequestScheduler;
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
			System.out.println("Message sent to an unregistered Process: "
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
			System.out.println("Message sent to an unregistered Process: "
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
	 * Method to send the message to remoteNode process to execute the part of
	 * the request.
	 * 
	 * @param nodeName
	 *            to which request to be sent
	 * @param processName
	 *            process name of the remote node to be called
	 * @param msg
	 *            A hashmap containing the parameters for the process
	 */
	public static void send(String nodeName, String processName, Message msg)
	{
		send(nodeName, processName, msg, JAVA_PORT);
	}

	/**
	 * Method to send response data to erlangNode
	 * 
	 * @param nodeName
	 *            The name of the node from which a request to process has been
	 *            sent to korus
	 * @param processName
	 *            Name of the process at erlang node which will collect the
	 *            response
	 * @param msg
	 *            A hashmap containing the parameters for the process
	 */
	public static void sendToErlang(String nodeName, String processName,
			Message msg)
	{
		send(nodeName, processName, msg, ERLANG_PORT);
	}

	/**
	 * Send data to the node.
	 * 
	 * @param nodeName
	 *            The name of the node from which a request to process has been
	 *            sent to korus
	 * @param processName
	 *            Name of the process at erlang node which will collect the
	 *            response
	 * @param msg
	 *            A hashmap containing the parameters for the process
	 * @param port
	 *            Port to connect with.
	 */
	private static void send(String nodeName, String processName, Message msg,
			int port)
	{
		if (isDistributed)
		{
			if (connectedNodesMap.get(nodeName) == null)
			{
				connect(nodeName, port);
			}
			msg.put("action", processName);
			String request = StringUtil.messageToString(msg);
			SerializableMessage message = new SerializableMessage(nodeName,
					request);
			KorusWriter.setRequestMessage(message);
		} else
		{
			System.out
					.println("KorusRuntime is not initalized in DISTRIBUTED_MODE to enable it, set DISTRIBUTED_MODE=true in properties file");
		}
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
		{
			registeredProcessMap.put(processName, process);
		}

		else
		{
			throw new ProcessAlreadyExistsException(processName);
		}

	}

	/**
	 * Method to connect to the given ipAddress and put the bufferedWriter in
	 * the hashmap to reuse the connection.
	 * 
	 * @param ipAddress
	 *            ipAddress of the remoteNode
	 * @param port
	 *            port to be used for connection.
	 */
	private static void connect(String ipAddress, int port)
	{
		BufferedWriter bufferedWriter = null;
		Socket clientSocket = null;
		try
		{
			// connect to the Socket
			clientSocket = new Socket(ipAddress, port);
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(
					clientSocket.getOutputStream()));
			connectedNodesMap.put(ipAddress, bufferedWriter);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Get the connectedNodesMap
	 * 
	 * @return a hashmap containing the bufferedWriter of connected nodes.
	 */
	public static HashMap<String, BufferedWriter> getConnectedNodesMap()
	{
		return connectedNodesMap;
	}

	/**
	 * Get the Port
	 * 
	 * @return the port
	 */
	public static int getPort()
	{
		return JAVA_PORT;
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

			File propertiesFile = new File(korusHome
					+ "/properties/korus.properties");

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
					{
						throw new KeyNotFoundException(
								"NUMBER_OF_NEW_REQUEST_EXECUTERS");
					}

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
					{
						throw new KeyNotFoundException(
								"NUMBER_OF_NEW_REQUEST_SCHEDULERS");
					}
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
			{
				throw new KeyNotFoundException("NUMBER_OF_CORE_SCHEDULERS");
			}
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
			{
				throw new KeyNotFoundException("NUMBER_OF_CORE_EXECUTERS");
			}
			// check if DISTRIBUTED_MODE is 'true'
			// else initialize with false

			if (properties.containsKey("DISTRIBUTED_MODE"))
			{
				isDistributed = Boolean.parseBoolean(properties
						.getProperty("DISTRIBUTED_MODE"));
			} else
			{
				throw new KeyNotFoundException("DISTRIBUTED_MODE");
			}
		}

		catch (NumberFormatException e)
		{
			throw new KorusException(
					"Properties File Format error!\nDefine parameters(Case-sensitive) in (KEY=VALUE)format where KEY is a 'String' and VALUE is an 'integer'");
		}

		catch (IOException e)
		{
			System.out
					.println("Properties File not found!\nStarting with default configuration");

		}
	}

}
