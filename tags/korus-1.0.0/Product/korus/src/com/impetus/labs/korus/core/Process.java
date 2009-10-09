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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Process is a Lightweight alternative to Thread. Asynchronously executable
 * tasks can be made processes and can execute independently of of each other.
 * 
 */
public abstract class Process
{
	/**
	 * The service method should be implemented by all the classes inheriting
	 * the properties of a Process class
	 * 
	 * @param msg
	 *            Message Object to be passed to this Process
	 */
	public abstract void service(Message msg);

	/**
	 * Get the message from Process's messageQueue.
	 * 
	 * @return the message and removes it from Process's messageQueue.
	 */
	public Message getMessage()
	{
		try
		{
			return this.messageQueue.take();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Checks the message availability in the process's messageQueue.
	 * 
	 * @return the message but does not remove it from Process's messageQueue
	 */
	public Message peekMessage()
	{
		return this.messageQueue.peek();
	}

	/**
	 * Sets message to the process' messageQueue.
	 * 
	 * @param message
	 *            Message to be passed between the processes contains
	 *            ('KEY','VALUE') pairs.
	 */

	public void putMessage(Message message)
	{
		try
		{
			if (message != null)
			{
				this.messageQueue.put(message);
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<Message>();

}
