/**
 * KeyUtils.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.examples;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */

/**
 * Utility Class for Reading and Writing keys using a file
 */
public class KeyUtils {
	
	/**
	 * Writes a Key to the file
	 * @param filename	File in which the key has to be written
	 * @param key The key to be writtten
	 */
	public static void writeKey(String filename, Key key) {

		ObjectOutputStream outputStream = null;

		try {

			//Construct the LineNumberReader object
			outputStream = new ObjectOutputStream(
					new FileOutputStream(filename));

			outputStream.writeObject(key);

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			//Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Reads a key from the file
	 * @param filename the file from which the key is to be read
	 * @return key written in the file
	 */
	public static Key readKey(String filename) {

		ObjectInputStream inputStream = null;
		Object obj = null;
		try {

			//Construct the ObjectInputStream object
			inputStream = new ObjectInputStream(new FileInputStream(filename));

			while ((obj = inputStream.readObject()) != null) {

				if (obj instanceof Key) {

					System.out.println(((Key) obj).toString());

				}

			}

		} catch (EOFException ex) { //This exception will be caught when EOF is reached
			System.out.println("End of file reached.");
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			//Close the ObjectInputStream
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return (Key) obj;
	}


}
