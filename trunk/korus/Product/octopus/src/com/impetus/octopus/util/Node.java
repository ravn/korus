/**
 * Node.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.impetus.octopus.ParallelTask;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public class Node {

	private ParallelTask task = null;
	
	private List<Node> childNodes = null;
	
	public Node(ParallelTask task){
		this.task = task;
		childNodes = new ArrayList<Node>();
	}
	
	public void addChild(Node node){
		childNodes.add(node);
	}

	public ParallelTask getTask() {
		return task;
	}

	public void setTask(ParallelTask task) {
		this.task = task;
	}

	public List<Node> getChildNodes() {
		return childNodes;
	}
	
	public boolean isLeaf(){
		return (childNodes == null || childNodes.size() == 0);
	}
	
	public void execute(){
		if(this.task.getResult() == null){
			if(isLeaf()){
				ThreadPool.getThreadPool().execute(this.task);
			} else {
				for (Iterator iter = childNodes.iterator(); iter.hasNext();) {
					Node element = (Node) iter.next();
					element.execute();
				}
			}
		} 
	}
	
	
	public void dump(){
		System.out.println(task.getRange().getBegin()+"-"+task.getRange().getEnd()+","+task.getRange().getGrainSize());
		if(childNodes != null && childNodes.size() > 0){
			for (Iterator iter = childNodes.iterator(); iter.hasNext();) {
				Node element = (Node) iter.next();
				element.dump();
			}
		} else {
			return;
		}
	}
}
