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
package com.impetus.labs.korus.exception;

/**
 * ProcessAlreadyExistsException is a custom exception class which 
 * throws an exception when name conflicts occur in the process registration. 
 * 
 */
public class ProcessAlreadyExistsException extends KorusException
{
	/**
	 * Constructor for creating new ProcessAlreadyExistsException displays the
	 * exceptionString on the console.
	 * 
	 * @param duplicateProcessName
	 *            String to be shown to the programmer as a error message.
	 */

	public ProcessAlreadyExistsException(String duplicateProcessName)
	{
		super(duplicateProcessName);
		this.duplicateProcessName = duplicateProcessName;
	}
	
	/**
	 * To String implementation of ProcessAlreadyExistsException 
	 * @return String stating the Exception
	 */
	public String toString()
	{
		return "Process already Registered with name: " + this.duplicateProcessName;
	}
	
	private String duplicateProcessName = "";
}
