/*******************************************************************************
 * Korus - http://code.google.com/p/korus
 * Copyright (C) 2009 Impetus Technologies, Inc.(http://www.impetus.com/)
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

import com.impetus.labs.korus.core.Message;
import com.impetus.labs.korus.core.Process;

// Import other necessary Business Logic Packages and Classes

/**
 * BuyProcess2 Extends Abstract Process Class
 */
public class BuyProcess2 extends Process
{

	// Overide the service Method
	public void service(Message requestMessage)
	{

		System.out.println("Inside Process2");
		// Get item name from the Message Object
		int priceOfItem = ((Integer) requestMessage.get("priceOfItem"))
				.intValue();

		// Rest of the Business Logic
		String itemRating = rateItem(priceOfItem);

		System.out.println("Rating of an Item: " + itemRating);

		// Forcefully Shutting the Korus Runtime if not needed
		System.exit(0);

	}

	/**
	 * Rates an item on basis of its cost
	 * 
	 * @param priceOfItem
	 *            price of the Item to be rated
	 * @return Rating of an Item
	 */
	private String rateItem(int priceOfItem)
	{
		// Some dummy code
		// Method Implementation ...
		String itemRating = "";
		if (priceOfItem > 50)
			itemRating = "Item is Expensive";
		else
			itemRating = "Item is Affordable";

		return itemRating;

	}
}
