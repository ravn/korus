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
