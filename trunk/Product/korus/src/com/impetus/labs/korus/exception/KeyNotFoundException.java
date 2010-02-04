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
