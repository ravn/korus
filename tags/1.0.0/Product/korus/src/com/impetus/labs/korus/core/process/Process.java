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

import com.impetus.labs.korus.core.message.Message;
import com.impetus.labs.korus.core.message.RawMessage;


/**
 * Process is a Lightweight alternative to Thread. Asynchronously executable
 * tasks can be made processes and can execute independently of each
 * other. 
 *
 */
public abstract class Process extends BaseProcess
{
	/**
	 * Override the service(rawMessage) method of BaseProcess so that classes extending
	 * Process implement the service(Message) method, thereby
	 * suppressing the use of service(rawMessage) method.
	 */
	public void service(RawMessage rawMessage)
	{
		Message message = (Message)rawMessage;
		service(message);
	}
	/**
	 * The service method should be implemented by all the classes inheriting
	 * the properties of a Process class
	 * @param message Message Object to be passed to this Process
	 */
	public abstract void service(Message message);

	
	
}
