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
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.impetus.labs.korus.core.message.SerializableMessage;
/**
 * KorusWriter is used to write data over
 * Socket channel. It is involved in inter-node communication.
 */
public class KorusWriter implements Runnable 
{
	public void run() 
	{
		while (true)
		{
			BufferedWriter bufferedWriter = null;
			SerializableMessage requestMessage = getRequestMessage();
			String data = requestMessage.getData();
			String nodeName = requestMessage.getNodeName();
			try
			{

				bufferedWriter = KorusRuntime.getConnectedNodesMap().get(
						nodeName);
				if (bufferedWriter != null)
				{
					bufferedWriter.write(data);
					bufferedWriter.flush();
				}

			} catch (IOException e)
			{
				KorusRuntime.getConnectedNodesMap().remove(nodeName);
				e.printStackTrace();
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Get the requestMessage from the requestMessageQueue
	 * 
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
