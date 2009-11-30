/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2009 Impetus Technologies, Inc.(http://www.impetus.com)
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Korus. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.impetus.labs.korus.addons.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import net.spy.memcached.MemcachedClient;

/**
 * A ContextStore lets you store key value pairs of string objects that
 * are accessible across different nodes.
 * 
 */
public class ContextStore
{
	private static MemcachedClient memcacheClient = null;

	private static int TTL = Integer.MAX_VALUE;
	private static String ipAddress = null;
	private static int port = 11211;

	static
	{
		try
		{
			readPropertiesFile();
			memcacheClient = new MemcachedClient(new InetSocketAddress(
					ipAddress, port));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Binds an string object to this context, using the name specified. If a
	 * String object of the same name is already bound to the context, 
	 * the object is replaced.
	 * 
	 * @param name
	 *            the name to which the String object is bound; cannot be null
	 * @param value
	 *            the value string object to be bound
	 */
	public static void setAttribute(final String name, final String value)
	{
		memcacheClient.set(name, TTL, value);
	}

	/**
	 * Returns the value bound with the specified name in this context, or null
	 * if no value is bound under the name.
	 * 
	 * @param name
	 *            a string specifying the name of the object
	 */
	public static Object getAttribute(final String name)
	{
		return memcacheClient.get(name);
	}

	/**
	 * Binds an object to this context, using the name specified. If an
	 * object of the same id is already bound to the context, the object is
	 * replaced.
	 * 
	 * @param name
	 *            the name to which the object is bound; cannot be null
	 * @param object
	 *            the object to be bound
	 */
	public static void setAttribute(final String name, final Object object)
	{
		memcacheClient.set(name, TTL, object);
	}

	/**
	 * Removes the object associated with the name
	 * 
	 * @param name
	 *            a string specifying the name of the object
	 */
	public static void removeAttribute(final String name)
	{
		memcacheClient.delete(name);
	}

	/**
	 * Read the properties file to identify the ipAddress and port number of the
	 * memcached server
	 */
	private static void readPropertiesFile()
	{
		String korusHome = System.getenv("KORUS_HOME");
		File propertiesFile = new File(korusHome
				+ "/properties/addons/contextStore.properties");

		Properties properties = new Properties();
		FileInputStream fileInputStream;
		try
		{
			fileInputStream = new FileInputStream(propertiesFile);
			properties.load(fileInputStream);

			ipAddress = properties.getProperty("IP_ADDRESS");
			port = Integer.parseInt(properties.getProperty("PORT"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
