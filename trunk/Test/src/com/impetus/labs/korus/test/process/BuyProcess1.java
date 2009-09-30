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
import com.impetus.labs.korus.core.Process;


// Import other necessary Business Logic Packages and Classes

/**
 * BuyProcess1 Extends Abstract Process Class
 */
public class BuyProcess1 extends Process {

	// Overide the service Method
	
	public void service(Message requestMessage) {
		// Get item name from the Message Object
		String itemName = (String) requestMessage.get("itemName");
		System.out.println("Inside Process1");
		System.out.println("Name of the Item: " + itemName);
		
		// Rest of the Business Logic
		int priceOfItem = fetchDetails(itemName);

		// Put the intermediate result in the Message Object for next 
		// process
		requestMessage.put("priceOfItem", priceOfItem);

		System.out.println("Passing Message to Process2...");
		// Send Message Asynchronously to the next Process on Local Node
		KorusRuntime.send("buyProcess2", requestMessage);
		
		// Send Message Asynchronously to the next Process on Remote Node
		// Set only String Objects in the Message in case of Remote Calls
		//KorusRuntime.send("hostname", "buyProcess2", requestMessage)
	}

	/**
	 * Method to Fetch Details of an item. To keep it simple just returning
	 * an integer called price
	 * @param itemName Name of the Item
	 * @return price of the Item
	 */
	private int fetchDetails(String itemName) {
		// Some dummy code
		// Method Implementation ...
		int price = 20;
		return price;
		

	}
}
