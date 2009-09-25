/**
 * StringUtil.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.util;

import java.util.Iterator;
import java.util.Set;

import com.impetus.labs.korus.core.Message;

public class StringUtil
{

	/**
	 * Method to convert the message hashmap to request string.
	 * 
	 * @param messsage
	 *            A hashmap containing the parameters for the process
	 * @return the final request String to be sent
	 */
	public static String messageToString(Message message)
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
