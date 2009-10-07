/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2009 Impetus Technologies, Inc.
 * This file is part of Korus.
 * Korus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation (http://www.gnu.org/licenses/gpl.html)
 * 
 * Korus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *    
 * You should have received a copy of the GNU General Public License
 * along with Korus.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.impetus.labs.korus.test.process;

import com.impetus.labs.korus.core.KorusRuntime;
import com.impetus.labs.korus.core.Message;
import com.impetus.labs.korus.exception.ProcessAlreadyExistsException;

/**
 * Tests message Passing Between Processes
 *
 */
public class TestProcess {
	
	public static void main(String args[])
	{
	
	// Create two Processes
	BuyProcess1 buyProcess1 = new BuyProcess1();
	BuyProcess2 buyProcess2 = new BuyProcess2();
	
	// Register them with the Korus Runtime
	try
	{
		KorusRuntime.registerProcess("buyProcess1", buyProcess1);
		KorusRuntime.registerProcess("buyProcess2", buyProcess2);
	} catch (ProcessAlreadyExistsException e)
	{			
		e.printStackTrace();
	}
	
	Message msg = new Message();
	String itemName = "Calculator";
	msg.put("itemName", itemName);
	KorusRuntime.send("buyProcess1", msg);
	
	}
}
