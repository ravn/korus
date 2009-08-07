/**
 * KorusException.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

package com.impetus.labs.korus.exception;

/**
 * KorusException is a custom exception class which 
 * throws an exception when any conflicts occur. 
 * 
 */
public class KorusException extends Exception
{
	/**
	 * Constructor for creating new KorusException displays the
	 * exceptionString on the console.
	 * 
	 * @param ExceptionString
	 *            String to be shown to the programmer as a error message.
	 */

	public KorusException(String ExceptionString)
	{
		super(ExceptionString);
		this.ExceptionString = ExceptionString;
	}
	
	/**
	 * To String implementation of KorusException 
	 * @return String stating the Exception
	 */
	public String toString()
	{
		return this.ExceptionString;
	}
	
	private String ExceptionString = "";
}