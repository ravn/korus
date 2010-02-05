/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2010 Impetus Technologies, Inc.(http://www.impetus.com)
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
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

import com.impetus.labs.korus.core.executer.Executer;
import com.impetus.labs.korus.core.executer.RequestExecuter;
import com.impetus.labs.korus.core.message.Message;
import com.impetus.labs.korus.core.message.RawMessage;
import com.impetus.labs.korus.core.message.SerializableMessage;
import com.impetus.labs.korus.core.process.BaseProcess;
import com.impetus.labs.korus.core.process.Process;
import com.impetus.labs.korus.core.scheduler.RequestScheduler;
import com.impetus.labs.korus.core.scheduler.Scheduler;
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

	private static int numberOfRequestExecuters = 0;

	private static int numberOfRequestSchedulers = 0;

	private static int numberOfCoreSchedulers = 1;

	private static int numberOfCoreExecuters = 0;

	private static int numberOfCPUCores = 0;

	private static HashMap<String, BaseProcess> registeredProcessMap = new HashMap<String, BaseProcess>();

	private static Executer[] coreExecuterList = null;

	private static Scheduler[] coreSchedulerList = null;

	private static RequestExecuter[] requestExecuterList = null;

	private static RequestScheduler[] requestSchedulerList = null;

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
			requestExecuterList = new RequestExecuter[numberOfRequestExecuters];
			for (int i = 0; i < requestExecuterList.length; i++)
			{
				requestExecuterList[i] = new RequestExecuter();
				requestExecuterList[i].start();
			}

			requestSchedulerList = new RequestScheduler[numberOfRequestSchedulers];
			for (int i = 0; i < requestSchedulerList.length; i++)
			{
				requestSchedulerList[i] = new RequestScheduler();
				requestSchedulerList[i].start();
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
	 * Get the next requestExecuter from the requestExecuterList.
	 * 
	 * @return the next requestExecuter from the requestExecuterList.
	 */
	public static RequestExecuter getNextRequestExecuter()
	{
		RequestExecuter requestExecuter = requestExecuterList[index
				% numberOfRequestExecuters];
		index++;
		return requestExecuter;
	}

	/**
	 * Get the next requestScheduler from the requestSchedulerList.
	 * 
	 * @return the next requestScheduler from the requestSchedulerList.
	 */
	public static RequestScheduler getNextRequestScheduler()
	{
		RequestScheduler requestScheduler = requestSchedulerList[index
				% numberOfRequestSchedulers];
		index++;
		return requestScheduler;
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
		BaseProcess process = getRegisteredProcess(processName);
		if (process == null)
		{
			System.out.println("Message sent to an unregistered Process: "
					+ processName);
		} else
		{
			// 2. Put message in process's messageQueue
			process.putMessage(message);
			// 3. Put this Process in Scheduler's processQueue
			Scheduler.setProcess(process);
		}

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
	 * @param isFirst
	 *            isFirst to identify the first part of request and give
	 *            priority over other sub-parts.
	 */
	public synchronized static void send(String processName, Message message,
			boolean isFirst)
	{

		// 1. Select process from registered process list
		BaseProcess process = getRegisteredProcess(processName);
		if (process == null)
		{
			System.out.println("Message sent to an unregistered Process: "
					+ processName);
		} else
		{
			// 2. Put message in process's messageQueue
			process.putMessage(message);
			if (isFirst)
			{
				// 3. Put this Process in Scheduler's processQueue
				RequestScheduler.setProcess(process);
			} else
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
	 * @param message
	 *            Message containing the parameters for the process
	 */
	public static void send(String nodeName, String processName, Message message)
	{
		send(nodeName, processName, message, JAVA_PORT);
	}

	/**
	 * Method to send response data to erlangNode
	 * 
	 * @param nodeName
	 *            The name of the node from which a request to process has been
	 *            sent to Korus
	 * @param processName
	 *            Name of the process at erlang node which will collect the
	 *            response
	 * @param message
	 *            Message containing the parameters for the process
	 */
	public static void sendToErlang(String nodeName, String processName,
			Message message)
	{
		send(nodeName, processName, message, ERLANG_PORT);
	}

	/**
	 * Send data to the node.
	 * 
	 * @param nodeName
	 *            The name of the node from which a request to process has been
	 *            sent to Korus
	 * @param processName
	 *            Name of the process at erlang node which will collect the
	 *            response
	 * @param rawMessage
	 *            Message containing the parameters for the process
	 * @param port
	 *            Port to connect with.
	 */
	public static void send(String nodeName, String processName,
			RawMessage rawMessage, int port)
	{
		if (isDistributed)
		{
			if (connectedNodesMap.get(nodeName) == null)
			{
				connect(nodeName, port);
			}
			rawMessage.put("action", processName);
			String request = StringUtil.messageToString(rawMessage);
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
	public static BaseProcess getRegisteredProcess(String processName)
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
	public static void registerProcess(String processName, BaseProcess process)
			throws ProcessAlreadyExistsException
	{
		if (registeredProcessMap.get(processName) == null)
			registeredProcessMap.put(processName, process);
		else
			throw new ProcessAlreadyExistsException(processName);

	}

	/**
	 * Method to connect to the given ipAddress and put the bufferedWriter in
	 * the Hashmap to reuse the connection.
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
	 * @return A Hashmap containing the bufferedWriter of connected nodes.
	 */
	public static HashMap<String, BufferedWriter> getConnectedNodesMap()
	{
		return connectedNodesMap;
	}

	/**
	 * @return the JAVA_PORT
	 */
	public static int getJavaPort()
	{
		return JAVA_PORT;
	}

	/**
	 * @return the ERLANG_PORT
	 */
	public static int getErlangPort()
	{
		return ERLANG_PORT;
	}

	/**
	 * Method to read the properties file to initialize the configuration
	 * parameters.
	 * 
	 * @throws KorusException
	 */

	public static void readPropertiesFile() throws KorusException
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

			// check if DISTRIBUTED_MODE is 'true'
			// else initialize with false
			if (properties.containsKey("DISTRIBUTED_MODE"))
			{
				isDistributed = Boolean.parseBoolean(properties
						.getProperty("DISTRIBUTED_MODE"));
			} else
			{
				keyNotFound("DISTRIBUTED_MODE");
			}

			// Check for Kind of request if WEB_REQUEST='true'
			// and if KEYS not found or incorrect initialization
			// Initialize the advance processing parameters with default value 1

			if (properties.containsKey("WEB_REQUEST"))
			{
				isWebRequest = Boolean.parseBoolean(properties
						.getProperty("WEB_REQUEST"));
				if (isWebRequest)
				{
					if (properties.containsKey("NUMBER_OF_REQUEST_EXECUTERS"))
					{
						numberOfRequestExecuters = Integer.parseInt(properties
								.getProperty("NUMBER_OF_REQUEST_EXECUTERS"));
						if (numberOfRequestExecuters <= 0)
						{
							numberOfRequestExecuters = 1;
							System.out
									.println("The value of parameter NUMBER_OF_REQUEST_EXECUTERS in properties file must be greater than 0\n"
											+ "setting it to default value 1");
						}
					} else
					{

						keyNotFound("NUMBER_OF_REQUEST_EXECUTERS");
					}
					if (properties.containsKey("NUMBER_OF_REQUEST_SCHEDULERS"))
					{
						numberOfRequestSchedulers = Integer.parseInt(properties
								.getProperty("NUMBER_OF_REQUEST_SCHEDULERS"));
						if (numberOfRequestSchedulers <= 0)
						{
							numberOfRequestSchedulers = 1;
							System.out
									.println("The value of parameter NUMBER_OF_REQUEST_SCHEDULERS in properties file must be greater than 0\n"
											+ "setting it to default value 1");
						}
					} else
					{
						keyNotFound("NUMBER_OF_REQUEST_SCHEDULERS");
					}
				}
			} else
			{
				keyNotFound("WEB_REQUEST");
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
					System.out
							.println("The value of parameter NUMBER_OF_CORE_SCHEDULERS in properties file must be greater than 0\n"
									+ "setting it to default value 1");
				}
			} else
			{
				keyNotFound("NUMBER_OF_CORE_SCHEDULERS");
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
					System.out
							.println("The value of parameter NUMBER_OF_CORE_EXECUTERS in properties file must be greater than 0\n"
									+ "setting it to default value equals to the number Of CPUCores");

				}
			} else
			{
				keyNotFound("NUMBER_OF_CORE_EXECUTERS");
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
		System.out.println("Korus Runtime parameters");
		System.out.println("WEB_REQUEST Mode: " + isWebRequest);
		System.out.println("DISTRIBUTED Mode: " + isDistributed);
		System.out.println("NUMBER_OF_CORE_SCHEDULERS: "
				+ numberOfCoreSchedulers);
		System.out
				.println("NUMBER_OF_CORE_EXECUTERS: " + numberOfCoreExecuters);
		System.out.println("NUMBER_OF_REQUEST_EXECUTERS: "
				+ numberOfRequestExecuters);
		System.out.println("NUMBER_OF_REQUEST_SCHEDULERS: "
				+ numberOfRequestSchedulers);

	}

	/**
	 * To load the static KorusRuntime
	 */
	public static void load()
	{
		// load the static block
	}

	public static void keyNotFound(String key)
	{
		System.out
				.println("Key: "
						+ key
						+ " is not defined in the PropertiesFile \nInitializing with default configuration.");
	}

	public static boolean getExecutionMode()
	{
			return isWebRequest;
	}

}
