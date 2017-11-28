/*
 * Sonar Message Flow Plugin
 * Copyright (C) 2015 Hendrik Scholz and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.msgflow.model;

import java.util.HashMap;

/**
 * The class holds the list of message flows (internal message flow model) of a 
 * single message flow project.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowProject {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MessageFlowProject.class);
	
	/**
	 * a HashMap holding the file name and the message flow object
	 */
	private static HashMap<String, MessageFlow> msgflowMap;
	
	/**
	 * a HashMap holding the file name and the message map object
	 */
	private static HashMap<String, MessageMap> msgmapMap;

	/**
	 * the one and only instance of the MessageFlowProject
	 */
	private static MessageFlowProject instance;
	
	/**
	 * Constructor
	 * 
	 * Direct instantiation is not possible.
	 */
	private MessageFlowProject() {
	}
	
	/**
	 * Creates an instance of a MessageFlowProject and initialises the HashMap
	 * 
	 * @return an instance of a MessageFlowProject
	 */
	public static MessageFlowProject getInstance() {
		if (instance == null) {
			instance = new MessageFlowProject();
			msgflowMap = new HashMap<String, MessageFlow>();
			msgmapMap = new HashMap<String, MessageMap>();
		}
		
		return instance;
	}
	
	/**
	 * The method retrieves the message flow object using the file name.
	 * 
	 * If the file name is not in the map, the file is parsed and the 
	 * corresponding message flow object is put to the map. If the file name 
	 * is in the map, the existing message flow object is returned. Every file 
	 * is just parsed once.
	 * 
	 * @param the file name for which the message flow object should be retrieved
	 * 
	 * @return the message flow object
	 */
	public MessageFlow getMessageFlow(String fileName) {
		/*
		 * retrieves the message flow object using the file name
		 * 
		 * If the message flow is not in the map, parse the file and put the
		 * message flow object to the map.
		 */
		if (msgflowMap.containsKey(fileName) == false) {
			msgflowMap.put(fileName,
					   	   new MessageFlow(fileName, new MessageFlowParser()));
		}

		return msgflowMap.get(fileName);
	}
	
	public MessageMap getMessageMap(String fileName) {
		/*
		 * retrieves the message map object using the file name
		 * 
		 * If the message map is not in the map, parse the file and put the
		 * message map object to the map.
		 */
		if (msgmapMap.containsKey(fileName) == false) {
			msgmapMap.put(fileName,
					   	   new MessageMap(fileName, new MessageMapParser()));
		}

		return msgmapMap.get(fileName);
	}
}
