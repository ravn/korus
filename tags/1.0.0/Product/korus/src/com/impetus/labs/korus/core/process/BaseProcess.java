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
package com.impetus.labs.korus.core.process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.impetus.labs.korus.core.message.RawMessage;

public abstract class BaseProcess
{
	/**
	 * service(rawMessage) method to be overridden by inheriting classes.
	 * @param rawMessage
	 */
	public abstract void service(RawMessage rawMessage);
	
	/**
	 * Get the message from Process's messageQueue.
	 * 
	 * @return the message and removes it from Process's messageQueue.
	 */
	public RawMessage getMessage()
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
	public RawMessage peekMessage()
	{
		return this.messageQueue.peek();
	}

	/**
	 * Sets message to the process' messageQueue.
	 * 
	 * @param message
	 *            Message to be passed between the processes contains ('KEY','VALUE')
	 *            pairs.
	 */

	public void putMessage(RawMessage message)
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

	private BlockingQueue<RawMessage> messageQueue = new LinkedBlockingQueue<RawMessage>();

}
