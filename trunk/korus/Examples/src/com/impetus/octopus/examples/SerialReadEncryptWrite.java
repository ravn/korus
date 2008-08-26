/**
 * SerialReadEncryptWrite.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.examples;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 * 
 */
public class SerialReadEncryptWrite {

	/**
	 * Fetch the entire contents of a text file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * @param aFile
	 *            is a file which already exists and can be read.
	 */
	static public String readContents(File aFile) {
		// ...checks on aFile are elided
		StringBuffer contents = new StringBuffer();

		// declared here only to make visible to finally clause
		BufferedReader input = null;
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			input = new BufferedReader(new FileReader(aFile));
			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS
			 * the newline. it returns null only for the END of the stream. it
			 * returns an empty String if two newlines appear in a row.
			 */
			while ((line = input.readLine()) != null) {
				contents.append(line);
				contents.append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (input != null) {
					// flush and close both "input" and its underlying
					// FileReader
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return contents.toString();
	}

	/**
	 * Change the contents of text file in its entirety, overwriting any
	 * existing text.
	 * 
	 * This style of implementation throws all exceptions to the caller.
	 * 
	 * @param aFile
	 *            is an existing file which can be written to.
	 * @throws IllegalArgumentException
	 *             if param does not comply.
	 * @throws FileNotFoundException
	 *             if the file does not exist.
	 * @throws IOException
	 *             if problem encountered during write.
	 */
	static public void writeContents(File aFile, String aContents)
			throws FileNotFoundException, IOException {
		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: "
					+ aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: "
					+ aFile);
		}

		// declared here only to make visible to finally clause; generic
		// reference
		Writer output = null;
		try {
			// use buffering
			// FileWriter always assumes default encoding is OK!
			output = new BufferedWriter(new FileWriter(aFile));
			output.write(aContents);
		} finally {
			// flush and close both "output" and its underlying FileWriter
			if (output != null)
				output.close();
		}
	}

	/**
	 * Simple test harness.
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String... aArguments) throws IOException,
			InterruptedException {
		long initialTime, finalTime;
		initialTime = System.currentTimeMillis();

		// these two files should exist on the drive
		File testFile = new File("c:/testFile.txt");
		File outputFile = new File("c:/writeFile.txt");

		initCipher();
		String data = readContents(testFile);
		String encryptedData = encryptData(data);
		writeContents(outputFile, encryptedData);

		// System.out.println("New file contents: " + readContents(testFile));
		finalTime = System.currentTimeMillis();
		System.out.println("Time Taken by SerialReadEncryptWrite: "
				+ ((finalTime - initialTime)));

	}
	
	/**
	 * Initialize the Cipher. A key is generated and used to encrypte the
	 * Data.
	 */
	public static void initCipher() {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			Key key = kg.generateKey();
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println("No Such Algorithm Exception "
					+ nsae.getMessage());
		} catch (NoSuchPaddingException nspe) {
			System.out
					.println("No Such Padding Exception " + nspe.getMessage());
		} catch (InvalidKeyException ike) {
			System.out.println("Invalid Key Exception " + ike.getMessage());
		} catch (IllegalStateException ise) {
			System.out.println("Illegal State Exception " + ise.getMessage());
		}
	}
	
	/**
	 * The data provided is encrypted using this function
	 * @param data Data to be encrypted
	 * @return encrypted data string
	 */
	public static String encryptData(String data) {

		try {
			byte[] plain_data = data.getBytes();
			String str_plain_data = new String(plain_data);
			//System.out.println("Plain data = " + str_plain_data);

			// encryption

			byte[] encrypted_data = cipher.doFinal(plain_data);

			// encode data
			String encoded_data = new sun.misc.BASE64Encoder()
					.encode(encrypted_data);
			//System.out.println("encoded:" + encoded_data);

			return encoded_data;

		} catch (IllegalStateException ise) {
			System.out.println("Illegal State Exception " + ise.getMessage());
		} catch (IllegalBlockSizeException ibse) {
			System.out.println("Illegal Block Size Exception "
					+ ibse.getMessage());
		} catch (BadPaddingException bpe) {
			System.out.println("Bad Padding Exception " + bpe.getMessage());
		}
		return "Problem in encryption";
	}

	private static Cipher cipher = null;
}
