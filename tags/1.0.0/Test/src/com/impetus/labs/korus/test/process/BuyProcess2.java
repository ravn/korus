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
package com.impetus.labs.korus.test.process;

import com.impetus.labs.korus.core.message.Message;
import com.impetus.labs.korus.core.process.Process;

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
		// Get book and author name from the Message Object
		String bookName = requestMessage.get("bookName");
		String authorName = requestMessage.get("author");
		// Rest of the Business Logic
		String bookRating = rateBook(bookName, authorName);

		System.out.println("Rating of an Book: " + bookRating);

		// Forcefully Shutting the Korus Runtime if not needed
		System.exit(0);

	}

	/**
	 * Rates an item on basis of its cost
	 * 
	 * @param bookName
	 *            name of the book to be rated
	 * @param authorName
	 *            name of the author of the book
	 * @return Rating of an book
	 */
	private String rateBook(String bookName, String authorName)
	{

		String bookRating = "";
		if (bookName.equals("Five Point Someone")
				&& authorName.equals("Chetan Bhagat"))
			bookRating = "India's best seller";
		else
			bookRating = "not known";

		return bookRating;

	}
}
