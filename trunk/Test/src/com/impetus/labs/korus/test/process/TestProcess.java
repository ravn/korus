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
