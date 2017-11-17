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

import java.util.ArrayList;

/**
 * The class is a model of a message flow model containing a list of 
 * message flow nodes. 
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlow {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MessageFlow.class);
	
	/**
	 * a list of Collector Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> collectorNodes;
	
	/**
	 * a list of Compute Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> computeNodes;

	/**
	 * a list of File Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> fileInputNodes;
	
	/**
	 * a list of File Output Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> fileOutputNodes;
	
	/**
	 * a list of Http Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> httpInputNodes;
	
	/**
	 * a list of Http Request Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> httpRequestNodes;
	
	/**
	 * a list of Http Reply Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> httpReplyNodes;
	
	/**
	 * a list of MQ Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqInputNodes;
	
	/**
	 * a list of MQ Output Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqOutputNodes;

	/**
	 * a list of MQ Get Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqGetNodes;

	/**
	 * a list of MQ Header Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqHeaderNodes;
	
	/**
	 * a list of MQ Reply Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> mqReplyNodes;
	
	/**
	 * a list of Reset Content Descriptor Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> resetContentDescriptorNodes;
	
	/**
	 * a list of Soap Input Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> soapInputNodes;
	
	/**
	 * a list of Soap Request Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> soapRequestNodes;
	
	/**
	 * a list of Timeout Control Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> timeoutControlNodes;
	
	/**
	 * a list of Timeout Notification Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> timeoutNotificationNodes;
	
	/**
	 * a list of Try Catch Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> tryCatchNodes;
	
	/**
	 * a list of IMS Request Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> imsRequestNodes;
	
	/**
	 * a list of Filter Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> filterNodes;
	
	/**
	 * a list of trace Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> traceNodes;
	
	/**
	 * a list of label Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> labelNodes;
	
	/**
	 * a list of routeToLabel Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> routeToLabelNodes;
	
	/**
	 * a list of aggregateControl Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> aggregateControlNodes;
	
	/**
	 * a list of miscellaneous Nodes of a message flow
	 */
	private ArrayList<MessageFlowNode> miscellaneousNodes;
	
	/**
	 * a list of Connections of a message flow
	 */
	private ArrayList<MessageFlowConnection> connections;
	
	/**
	 * a list of comment notes of a message flow
	 */
	private ArrayList<MessageFlowCommentNote> comments;
	/**
	 * a short description of a message flow
	 */
	private StringBuilder shortDescription;
	/**
	 * a long description of a message flow
	 */
	private StringBuilder longDescription;
	

	/**
	 * Constructor
	 * 
	 * Parses message flow files and adds the result to the message flow model.("Updated for implementation of new rules")
	 */
	public MessageFlow(String file,
					   MessageFlowParser messageFlowParser) {
		collectorNodes				= new ArrayList<MessageFlowNode>();
		computeNodes				= new ArrayList<MessageFlowNode>();
		fileInputNodes				= new ArrayList<MessageFlowNode>();
		fileOutputNodes				= new ArrayList<MessageFlowNode>();
		httpInputNodes				= new ArrayList<MessageFlowNode>();
		httpRequestNodes			= new ArrayList<MessageFlowNode>();
		httpReplyNodes				= new ArrayList<MessageFlowNode>();
		mqInputNodes				= new ArrayList<MessageFlowNode>();
		mqOutputNodes				= new ArrayList<MessageFlowNode>();
		mqGetNodes					= new ArrayList<MessageFlowNode>();
		mqHeaderNodes				= new ArrayList<MessageFlowNode>();
		mqReplyNodes				= new ArrayList<MessageFlowNode>();
		resetContentDescriptorNodes	= new ArrayList<MessageFlowNode>();
		soapInputNodes				= new ArrayList<MessageFlowNode>();
		soapRequestNodes			= new ArrayList<MessageFlowNode>();
		timeoutControlNodes			= new ArrayList<MessageFlowNode>();
		timeoutNotificationNodes	= new ArrayList<MessageFlowNode>();
		tryCatchNodes				= new ArrayList<MessageFlowNode>();
		imsRequestNodes				= new ArrayList<MessageFlowNode>();
		filterNodes					= new ArrayList<MessageFlowNode>();
		traceNodes					= new ArrayList<MessageFlowNode>();
		labelNodes					= new ArrayList<MessageFlowNode>();
		routeToLabelNodes			= new ArrayList<MessageFlowNode>();
		aggregateControlNodes		= new ArrayList<MessageFlowNode>();
		miscellaneousNodes			= new ArrayList<MessageFlowNode>();
		connections					= new ArrayList<MessageFlowConnection>();
		comments					= new ArrayList<MessageFlowCommentNote>();
		shortDescription			= new StringBuilder("");
		longDescription				= new StringBuilder("");
		messageFlowParser.parse(file,
								collectorNodes,
								computeNodes,
								fileInputNodes,
								fileOutputNodes,
								httpInputNodes,
								httpRequestNodes,
								httpReplyNodes,
								mqInputNodes,
								mqOutputNodes,
								mqGetNodes,
								mqHeaderNodes,
								mqReplyNodes,
								resetContentDescriptorNodes,
								soapInputNodes,
								soapRequestNodes,
								timeoutControlNodes,
								timeoutNotificationNodes,
								tryCatchNodes,
								imsRequestNodes,
								filterNodes,
								traceNodes,
								labelNodes,
								routeToLabelNodes,
								aggregateControlNodes,
								miscellaneousNodes,
								connections,
								comments,
								shortDescription,
								longDescription);

	}
	
	/**
	 * The method returns a list of the Collector Nodes of Message Flow.
	 * 
	 * @return a list of the Collector Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getCollectorNodes() {
		return collectorNodes;
	}

	/**
	 * The method returns a list of the Compute Nodes of Message Flow.
	 * 
	 * @return a list of the Compute Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getComputeNodes() {
		return computeNodes;
	}

	/**
	 * The method returns a list of the File Input Nodes of Message Flow.
	 * 
	 * @return a list of the File Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getFileInputNodes() {
		return fileInputNodes;
	}

	/**
	 * The method returns a list of the File Output Nodes of Message Flow.
	 * 
	 * @return a list of the File Output Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getFileOutputNodes() {
		return fileOutputNodes;
	}

	/**
	 * The method returns a list of the Http Input Nodes of Message Flow.
	 * 
	 * @return a list of the Http Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getHttpInputNodes() {
		return httpInputNodes;
	}

	/**
	 * The method returns a list of the Http Request Nodes of Message Flow.
	 * 
	 * @return a list of the Http Request Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getHttpRequestNodes() {
		return httpRequestNodes;
	}

	/**
	 * The method returns a list of the Http Reply Nodes of Message Flow.
	 * 
	 * @return the httpReplyNodes
	 */
	public ArrayList<MessageFlowNode> getHttpReplyNodes() {
		return httpReplyNodes;
	}

	/**
	 * The method returns a list of the MQ Input Nodes of Message Flow.
	 * 
	 * @return a list of the MQ Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqInputNodes() {
		return mqInputNodes;
	}

	/**
	 * The method returns a list of the MQ Output Nodes of Message Flow.
	 * 
	 * @return a list of the MQ Output Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqOutputNodes() {
		return mqOutputNodes;
	}

	/**
	 * The method returns a list of the MQGet Nodes of Message Flow.
	 * 
	 * @return a list of the MQGet Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqGetNodes() {
		return mqGetNodes;
	}

	/**
	 * The method returns a list of the MQHeader Nodes of Message Flow.
	 * 
	 * @return a list of the MQHeader Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getMqHeaderNodes() {
		return mqHeaderNodes;
	}

	/**
	 * The method returns a list of MQReply Nodes of Message Flow
	 * 
	 * @return the mqReplyNodes
	 */
	public ArrayList<MessageFlowNode> getMqReplyNodes() {
		return mqReplyNodes;
	}

	/**
	 * The method returns a list of the Reset Content Descriptor Nodes of Message Flow.
	 * 
	 * @return a list of the Reset Content Descriptor Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getResetContentDescriptorNodes() {
		return resetContentDescriptorNodes;
	}
	
	/**
	 * The method returns a list of the Soap Input Nodes of Message Flow.
	 * 
	 * @return a list of the Soap Input Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getSoapInputNodes() {
		return soapInputNodes;
	}

	/**
	 * The method returns a list of the Soap Request Nodes of Message Flow.
	 * 
	 * @return a list of the Soap Request Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getSoapRequestNodes() {
		return soapRequestNodes;
	}

	/**
	 * The method returns a list of the Timeout Control Nodes of Message Flow.
	 * 
	 * @return a list of the Timeout Control Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTimeoutControlNodes() {
		return timeoutControlNodes;
	}

	/**
	 * The method returns a list of the Timeout Notification Nodes of Message Flow.
	 * 
	 * @return a list of the Timeout Notification Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTimeoutNotificationNodes() {
		return timeoutNotificationNodes;
	}
	
	/**
	 * The method returns a list of the Try Catch Nodes of Message Flow.
	 * 
	 * @return a list of the Try Catch Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTryCatchNodes() {
		return tryCatchNodes;
	}
	
	/**
	 * The method returns a list of IMS Request nodes of Message Flow
	 * 
	 * @return a list of the IMS Request Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getImsRequestNodes() {
		return imsRequestNodes;
	}

	/**
	 * The method returns a list of IMS Request nodes of Message Flow
	 * 
	 * @return a list of the filter Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getFilterNodes() {
		return filterNodes;
	}

	/**
	 * The method returns a list of Trace nodes of Message Flow
	 * 
	 * @return a list of trace Nodes of Message Flow
	 */
	public ArrayList<MessageFlowNode> getTraceNodes() {
		return traceNodes;
	}

	/**
	 * The method returns a list of label nodes of Message Flow
	 * 
	 * @return the labelNodes
	 */
	public ArrayList<MessageFlowNode> getLabelNodes() {
		return labelNodes;
	}

	/**
	 * The method returns a list of routeToLabel Nodes of Message Flow
	 * 
	 * @return the routeToLabelNodes
	 */
	public ArrayList<MessageFlowNode> getRouteToLabelNodes() {
		return routeToLabelNodes;
	}

	/**
	 * The method returns a list of aggregateControl nodes
	 * 
	 * @return a list of aggregateControl nodes of MessageFlow
	 */
	public ArrayList<MessageFlowNode> getAggregateControlNodes() {
		return aggregateControlNodes;
	}

	/**
	 * The method returns a list of miscellaneous/uncategorized nodes
	 * 
	 * @return a list of miscellaneous/uncategorized nodes of MessageFlow
	 */
	public ArrayList<MessageFlowNode> getMiscellaneousNodes() {
		return miscellaneousNodes;
	}

	/**
	 * The method returns a list of the connections of Message Flow
	 * 
	 * @return a list of connections of the message flow
	 */
	public ArrayList<MessageFlowConnection> getConnections() {
		return connections;
	}

	/**
	 * The method returns a list of comment object of Message Flow
	 * 
	 * @return a list of comment objects
	 */
	public ArrayList<MessageFlowCommentNote> getComments() {
		return comments;
	}

	/**
	 * The method returns a short description of a Message Flow
	 * 
	 * @return the short description of the message flow
	 */
	public StringBuilder getShortDescription() {
		return shortDescription;
	}

	/**
	 * The method returns a long description of a Message Flow
	 * 
	 * @return the long description of the message flow
	 */
	public StringBuilder getLongDescription() {
		return longDescription;
	}
}
