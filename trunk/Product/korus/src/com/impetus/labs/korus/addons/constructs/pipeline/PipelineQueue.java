/**
 * PipelineQueue.java
 * 
 * Copyright 2009 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.labs.korus.addons.constructs.pipeline;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * PipelineQueue extends LinkedBlockingQueue to pass the object between two
 * pipeline tasks.
 */
public class PipelineQueue<Object> extends LinkedBlockingQueue<Object>
{
	
}