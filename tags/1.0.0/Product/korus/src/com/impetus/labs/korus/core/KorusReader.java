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
package com.impetus.labs.korus.core;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

import com.impetus.labs.korus.core.message.Message;
/**
 * KorusReader is used to read data from
 * Socket channel and Send data to appropriate Process.
 * It is involved in inter-node communication.
 */
public class KorusReader implements Runnable
{
	public void run()
	{
		try
		{
			CharsetDecoder decoder = charset.newDecoder();
			// Create the server socket channel
			ServerSocketChannel serverSocketChannel = ServerSocketChannel
					.open();
			// nonblocking I/O
			serverSocketChannel.configureBlocking(false);
			// host-port 7935
			serverSocketChannel.socket().bind(new java.net.InetSocketAddress(KorusRuntime.getJavaPort()));
			// Create the selector
			Selector selector = Selector.open();
			// Recording server to selector (type OP_ACCEPT)
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			// Infinite server loop
			for (;;)
			{

				// Waiting for events
				selector.select();
				// Get keys
				Set keys = selector.selectedKeys();

				Iterator iter = keys.iterator();

				// For each keys...
				while (iter.hasNext())
				{

					SelectionKey key = (SelectionKey) iter.next();
					// Remove the current key
					iter.remove();

					// if isAccetable = true
					// then a MemcacheClient required a connection
					if (key.isAcceptable())
					{
						// get MemcacheClient socket channel
						SocketChannel client = serverSocketChannel.accept();
						// Non Blocking I/O
						client.configureBlocking(false);
						// recording to the selector (reading)
						client.register(selector, SelectionKey.OP_READ);
						continue;
					}

					// if isReadable = true
					// then the server is ready to read
					if (key.isReadable())
					{
						Message message = new Message();
						String keyName = null;
						String valueData = null;

						SocketChannel client = (SocketChannel) key.channel();
						int BUFFER_SIZE = 2;
						ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
						// Read byte coming from the MemcacheClient
						try
						{
							client.read(buffer);
							buffer.flip();

							CharBuffer charBuffer = decoder.decode(buffer);
							int noOfParams = Integer.parseInt(charBuffer
									.toString());

							for (int i = 0; i < noOfParams; i++)
							{
								// KeyLength
								ByteBuffer lengthOfKey = ByteBuffer
										.allocate(LENGTH);
								client.read(lengthOfKey);
								lengthOfKey.flip();

								CharBuffer keyCharBuffer = decoder
										.decode(lengthOfKey);
								int keyLength = Integer.parseInt(keyCharBuffer
										.toString());

								// KeyName
								ByteBuffer keyNameBuffer = ByteBuffer
										.allocate(keyLength);
								client.read(keyNameBuffer);
								keyNameBuffer.flip();

								CharBuffer keyNameCharBuffer = decoder
										.decode(keyNameBuffer);
								keyName = keyNameCharBuffer.toString();

								// ValueLength
								ByteBuffer lengthOfValue = ByteBuffer
										.allocate(LENGTH);
								client.read(lengthOfValue);
								lengthOfValue.flip();
								CharBuffer valueCharBuffer = decoder
										.decode(lengthOfValue);
								int valueLength = Integer
										.parseInt(valueCharBuffer.toString());

								// ValueData
								ByteBuffer valueNameBuffer = ByteBuffer
										.allocate(valueLength);
								client.read(valueNameBuffer);
								valueNameBuffer.flip();
								CharBuffer valueNameCharBuffer = decoder
										.decode(valueNameBuffer);
								valueData = valueNameCharBuffer.toString();

								message.put(keyName, valueData);

							}
							String processName = (String) message.get("action");
							KorusRuntime.send(processName,message,KorusRuntime.getExecutionMode());
												
						}
						catch (Exception e)
						{
							String clientAddress = client.socket()
									.getInetAddress().getHostAddress();
							KorusRuntime.getConnectedNodesMap().remove(
									clientAddress);
							key.cancel();
							client.close();
							continue;
						}

					}
				}
			}
		} catch (Exception e)
		{

			e.printStackTrace();
		}
	}

	private static final int LENGTH = 4;
	private static Charset charset = Charset.forName("ISO-8859-1");
}
