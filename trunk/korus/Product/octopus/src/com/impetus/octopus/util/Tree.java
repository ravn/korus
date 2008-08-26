/**
 * Tree.java
 * 
 * Copyright 2008 Impetus Infotech India Pvt. Ltd. . All Rights Reserved.
 *
 * This software is proprietary information of Impetus Infotech, India.
 */
package com.impetus.octopus.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.impetus.octopus.BlockedRange;
import com.impetus.octopus.ParallelTask;

/**
 * @author Rajesh Nair <rnair@impetus.co.in>
 *
 */
public class Tree {

	Logger logger = Logger.getLogger(Tree.class.getName());
	
	private Node rootNode;
	
	public Tree(Node rootNode){
		this.rootNode = rootNode;
	}
	
	public void expand() throws CloneNotSupportedException{
		expandNode(rootNode);
	}
	
	private void expandNode(Node node) throws CloneNotSupportedException {
		logger.entering(Tree.class.getName(), "expandNode");
		ParallelTask task = node.getTask();
		if(task.isSplittable()){
			int maxChildCount = task.getMaxChildCount();
			logger.fine("MaxChildCount = "+maxChildCount);
			int begin = task.getRange().getBegin();
			int end = task.getRange().getEnd();
			int grainSize = task.getRange().getGrainSize();
			for (int i = 0; i < maxChildCount; i++) {
				int newBegin = grainSize*i +begin;
				int newEnd = grainSize * (i+1);
				newEnd = (end < newEnd) ? end :newEnd;
				BlockedRange newRange = new BlockedRange(newBegin,newEnd,grainSize);
				ParallelTask newTask = (ParallelTask)task.clone();
				newTask.setRange(newRange);
				Node newNode = new Node(newTask);
				node.addChild(newNode);
				expandNode(newNode);
			}
		}
		logger.exiting(Tree.class.getName(), "expandNode");
	}
	
	public void collapse(){
		Queue<Node> queue = new LinkedBlockingQueue<Node>();
		
	}
	
	private void collapseNode(Node node){
		rootNode.execute();
		getLeafNodeResults();
	}
	
	public List<Object> getLeafNodeResults(){
		ArrayList<Object> retVal = new ArrayList<Object>();
		walkLeaf(rootNode, retVal);
		return retVal;
	}
	
	private void walkLeaf(Node element, List<Object> list) {
		if(element.isLeaf()){
			ParallelTask task = element.getTask();
			Object result = task.getResult();
			while(result== null){
				try {
					task.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//at this point you can be sure 
			//result has been set
			list.add(result);
		} else {
			for(Node childNode : element.getChildNodes()){
				walkLeaf(childNode, list);
			}
		}
    }
	public void dump(){
		rootNode.dump();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
