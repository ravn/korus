/**
 * ProcessAlreadyExistsException.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

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