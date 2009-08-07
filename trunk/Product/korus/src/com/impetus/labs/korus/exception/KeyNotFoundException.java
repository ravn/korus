/**
 * KeyNotFoundException.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */

package com.impetus.labs.korus.exception;

/**
 * KeyNotFoundException is a custom exception class which 
 * throws an exception when any properties key is not found. 
 * 
 */
public class KeyNotFoundException extends KorusException
{

	/**
	 * /**
	 * Constructor for creating new KeyNotFoundException displays the
	 * exceptionString on the console.
	 *
	 * @param keyName
	 * 				KeyName to be shown to the programmer as a error message.
	 */
	public KeyNotFoundException(String keyName)
	{
		super(keyName);
		this.keyName = keyName;
	}
	
	/**
	 * To String implementation of KeyNotFoundException 
	 * @return String stating the Exception
	 */
	
	public String toString()
	{
		return "Key: "+ this.keyName +" is not defined in the PropertiesFile \nInitializing with default configuration" ; 
	}
	private String keyName="";
	
}
