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
package com.impetus.labs.korus.addons.constructs.parallelfor;

import com.impetus.labs.korus.core.message.RawMessage;

/**
 * 
 * BlockedRange is a single unit of block that can be executed in a single
 * iteration. Suppose, we have an iteration space from 0 to n. Then, an object
 * of blocked range will be constructed whose first argument will be the start
 * of the iteration and second argument will be end of the iteration. A third
 * argument grainSize will also have to be specified which breaks these
 * BlockedRanges into units which can be executed by a single core or processor.
 * <br/><br/><code> BlockedRange range= new BlockedRange(1,10000,1250); </code>
 * <br/><br/>will finally construct 10000/1250 = 8 BlockedRanges
 */
public class BlockedRange extends RawMessage
{
	private int begin;
	private int end;
	private int grainSize;

	/**
	 * Constructs a new BlockedRange.
	 * 
	 * @param begin
	 *            Begin value of the BlockedRange.
	 * @param end
	 *            End value of the BlockedRange.
	 * @param grainSize
	 *            Grain Size is a number between begin and end which specifies
	 *            the number of parts into which the Parallel Execution will be
	 *            split. Ideally the grainSize is end/2 for a dual core machine,
	 *            end/4 for a quad core machine and so on.
	 */
	public BlockedRange(int begin, int end, int grainSize)
	{
		this.begin = begin;
		this.end = end;
		this.grainSize = grainSize;
	}

	/**
	 * Get the begin value of the BlockedRange.
	 * 
	 * @return the begin value of the BlockedRange.
	 */
	public int getBegin()
	{
		return begin;
	}

	/**
	 * Set the begin value of the BlockedRange.
	 * 
	 * @param begin
	 *            Begin value of the BlockedRange.
	 */
	public void setBegin(int begin)
	{
		this.begin = begin;
	}

	/**
	 * Get the end value of the BlockedRange.
	 * 
	 * @return the end value of the BlockedRange.
	 */
	public int getEnd()
	{
		return end;
	}

	/**
	 * Set the end value of the BlockedRange.
	 * 
	 * @param end
	 *            End value of the BlockedRange.
	 */
	public void setEnd(int end)
	{
		this.end = end;
	}

	/**
	 * Get the grainSize of the BlockedRange.
	 * 
	 * @return the grainSize of the BlockedRange
	 */
	public int getGrainSize()
	{
		return grainSize;
	}

	/**
	 * Set the grainSize of the BlockedRange.
	 * 
	 * @param grainSize
	 *            GrainSize of the BlockedRange.
	 */
	public void setGrainSize(int grainSize)
	{
		this.grainSize = grainSize;
	}

	/**
	 * toString method
	 * 
	 * @return the <code>begin - end</code> and <code>grainSize</code>.
	 */
	public String toString()
	{
		return this.begin + "-" + this.end + "," + this.grainSize;
	}
}
