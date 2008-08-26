/**
 * EncryptTask.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.examples;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

import com.impetus.octopus.OctopusQueue;
import com.impetus.octopus.ParallelTask;
import com.impetus.octopus.PipelineTask;

/**
 * @author Saurabh Dutta <saurabh.dutta@impetus.co.in>
 *
 */
// used by the EncryptionPipeline Class
//Extend the PipelineTask
public class EncryptTask extends PipelineTask {

	private static final Logger logger = Logger.getLogger(EncryptTask.class
			.getName());

	private Cipher cipher = null ;
	/**
	 * 
	 */
	public EncryptTask() {
		// TODO Auto-generated constructor stub
		
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try
		{
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			
			Key key = kg.generateKey();
			
			KeyUtils.writeKey("c:/keystore.txt", key);
			
			//Key key = KeyUtils.readKey("c:/keystore.txt");
			
			this.cipher = Cipher.getInstance("DES");
			this.cipher.init(Cipher.ENCRYPT_MODE, key);
		}
		catch(NoSuchAlgorithmException nsae)
		{
			System.out.println("No Such Algorithm Exception " + nsae.getMessage());
		}
		catch(NoSuchPaddingException nspe)
		{
			System.out.println("No Such Padding Exception " + nspe.getMessage());
		}
		
		catch(IllegalStateException ise)
		{
			System.out.println("Illegal State Exception " + ise.getMessage());
		}
		catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			System.out.println("InvalidKeyException" + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private String encryptData(String data)
	{
				
		try
		{
			// Encode the string into bytes using utf-8
            byte[] utf8 = data.getBytes("UTF8");

            // Encrypt
            byte[] enc = this.cipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
		}
		catch(IllegalStateException ise)
		{
			System.out.println("Illegal State Exception " + ise.getMessage());
		}
		catch(IllegalBlockSizeException ibse)
		{
			System.out.println("Illegal Block Size Exception " + ibse.getMessage());
		}
		catch(BadPaddingException bpe)
		{
			System.out.println("Bad Padding Exception " + bpe.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("UnsupportedEncodingException" + e.getMessage());
		}
		return "Problem in encryption";

	}
	
	
	private String decryptData(String data)
	{
			
		try
		{
			// Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(data);

            // Decrypt
            byte[] utf8 = cipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
		
		}
		catch(IllegalStateException ise)
		{
			System.out.println("Illegal State Exception " + ise.getMessage());
		}
		catch(IllegalBlockSizeException ibse)
		{
			System.out.println("Illegal Block Size Exception " + ibse.getMessage());
		}
		catch(BadPaddingException bpe)
		{
			System.out.println("Bad Padding Exception " + bpe.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("UnsupportedEncodingException" + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException " + e.getMessage());
		}
		return "Problem in decryption";

	}
	
	@Override
	public void service() {
		logger.entering(EncryptTask.class.getName(), "service");
		String s = null;

		//System.out.println(" - this.hasStarted(): "+this.hasStarted());
		logger.finest("this.getPreviousTask().hasStarted(): "
				+ this.getPreviousTask().hasStarted());
		logger.finest("this.getPreviousTask().isDone(): "
				+ this.getPreviousTask().isDone());
		
		OctopusQueue<Object> inputQ = this.getInputQueue();
		OctopusQueue<Object> outputQ = this.getOutputQueue();
		
		
		try {
			// the previous task should be started
			this.getPreviousTask().hasStarted();
			
			// input queue is not empty
			while(inputQ.isEmpty() == false){
				String str = (String)inputQ.remove();
				
				String encryptedStr = encryptData(str);
				outputQ.put(encryptedStr);
				
				/*String decryptedStr = decryptData(str);
				outputQ.put(decryptedStr);*/				
			}
			
			// previous task should not be over
			while(this.getPreviousTask().isDone() == false || inputQ.isEmpty() == false){
				if(inputQ.isEmpty() == false){
				String str = (String)inputQ.take();
			
				String encryptedStr = encryptData(str);
				outputQ.put(encryptedStr);
								
				/*String decryptedStr = decryptData(str);
				outputQ.put(decryptedStr);*/
				
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.exiting(EncryptTask.class.getName(), "service");
	}

}
