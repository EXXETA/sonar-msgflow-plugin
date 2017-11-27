package com.exxeta.iss.sonar.msgflow.model;
/**
 * The class is a model of a message map
 * 
 * @author Arjav Shah
 */

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageMap {
	/**
	 * @param mappingSources
	 * @param mappings
	 * @param inputVar
	 * @param outputVar
	 * @param todoCount
	 */
	public MessageMap(String fileName, MessageMapParser parser) {
		
		mappingSources = new Mapping();
		mappings = new ArrayList<Mapping>();
		inputVar = new StringBuilder("");
		outputVar = new StringBuilder("");
		todoCount = new AtomicInteger(0);
		parser.parse(fileName, mappingSources, mappings, inputVar, outputVar, todoCount);
	}
	/**
	 * a list of the mapping of the mapping node
	 */
	private Mapping mappingSources;
	/**
	 * a list of the mapping of the mapping node
	 */
	private ArrayList<Mapping> mappings;
	/**
	 * a input variable of the mapping
	 */
	private StringBuilder inputVar;
	/**
	 * a input variable of the mapping
	 */
	private StringBuilder outputVar;
	/**
	 * a count of "TO DO" blocks of the mapping
	 */
	private AtomicInteger todoCount;
	
	/**
	 * returns the mapping of the sources of the map 
	 * 
	 * @return the mappingSources
	 */
	public Mapping getMappingSources() {
		return mappingSources;
	}
	/**
	 * the list of all the mappings of the message map 
	 * 
	 * @return the mappings
	 */
	public ArrayList<Mapping> getMappings() {
		return mappings;
	}
	/**
	 * returns the input variable of the mapping
	 * 
	 * @return the inputVar
	 */
	public StringBuilder getInputVar() {
		return inputVar;
	}
	/**
	 * returns the output variable of the mapping
	 * 
	 * @return the outputVar
	 */
	public StringBuilder getOutputVar() {
		return outputVar;
	}
	/**
	 * returns the count of todo blocks in the mapping
	 * 
	 * @return the todoCount
	 */
	public AtomicInteger getTodoCount() {
		return todoCount;
	}
	
}
