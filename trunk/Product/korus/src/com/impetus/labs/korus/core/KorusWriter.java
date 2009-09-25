/**
 * KorusWriter.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.core;

import java.io.BufferedWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class KorusWriter implements Runnable
{
	public void run()
	{
		while (true)
		{
			try
			{
				SerializableMessage requestMessage = getRequestMessage();
				String data = requestMessage.getData();
				String nodeName = requestMessage.getNodeName();
				BufferedWriter bufferedWriter = KorusRuntime
						.getConnectedNodesMap().get(nodeName);
				bufferedWriter.write(data);
				bufferedWriter.flush();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Get the requestMessage from the requestMessageQueue
	 * @return the requestMessage
	 */
	private static SerializableMessage getRequestMessage()
	{
		SerializableMessage requestMessage = null;
		try
		{
			requestMessage = requestMessageQueue.take();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return requestMessage;
	}

	/**
	 * Set the request to the requestMessageQueue.
	 * 
	 * @param requestMessage
	 *            message to be passed
	 */
	public static void setRequestMessage(SerializableMessage requestMessage)
	{
		try
		{
			requestMessageQueue.put(requestMessage);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private static BlockingQueue<SerializableMessage> requestMessageQueue = new LinkedBlockingQueue<SerializableMessage>();
}
