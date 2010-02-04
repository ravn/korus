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
package com.impetus.labs.korus.util;

import java.util.Iterator;
import java.util.Set;

import com.impetus.labs.korus.core.message.RawMessage;
/**
 * StrinUtil is a Utility class for String formatting operations * 
 */
public class StringUtil
{

	/**
	 * Method to convert the message to request string.
	 * 
	 * @param message
	 *            Message containing the parameters for the process
	 * @return the final request String to be sent
	 */
	public static String messageToString(RawMessage message)
	{
		StringBuffer stringBuffer = new StringBuffer();
		Set keys = message.keySet();
		int noOfKeys = keys.size();

		if (noOfKeys <= 9)
		{
			stringBuffer.append("0" + noOfKeys);
		} else
			stringBuffer.append(noOfKeys);

		for (Iterator iterator = keys.iterator(); iterator.hasNext();)
		{
			String key = (String) iterator.next();
			String value = (String) message.get(key);
			String formattedKey = format(key);
			String formattedValue = format(value);
			stringBuffer.append(formattedKey).append(formattedValue);
		}
		return stringBuffer.toString();
	}

	/**
	 * Method to format the string in KorusRequest format
	 * 
	 * @param string
	 *            a string which is required to be formatted to construct the
	 *            final request string to be passed
	 * @return formatted String
	 */
	private static String format(String string)
	{

		int stringLength = string.length();
		String formattedDataLength = "" + stringLength;

		if (stringLength <= 9)
		{
			formattedDataLength = "000" + stringLength;
		} else if (10 <= stringLength && stringLength <= 99)
		{
			formattedDataLength = "00" + stringLength;
		} else if (100 <= stringLength && stringLength <= 999)
		{
			formattedDataLength = "0" + stringLength;
		}

		String formattedString = formattedDataLength + string;
		return formattedString;

	}
}
