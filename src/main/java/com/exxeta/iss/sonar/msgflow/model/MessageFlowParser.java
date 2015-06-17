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

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The class parses the message flow files (.msgflow). The XML data is 
 * transformed into an internal message flow model. 
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowParser {
	
	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MessageFlowParser.class);
	
	/**
	 * Parses the message flow file (.msgflow) and creates the message flow node model.
	 * 
	 * @param fileName the file name of the message flow file to be parsed
	 * @param collectorNodes the list of Collector Nodes to which the new message flow node should be added
	 * @param computeNodes the list of Compute Nodes to which the new message flow node should be added
	 * @param fileInputNodes the list of File Input Nodes to which the new message flow node should be added
	 * @param fileOutputNodes the list of File Output Nodes to which the new message flow node should be added
	 * @param httpInputNodes the list of Http Input Nodes to which the new message flow node should be added
	 * @param httpRequestNodes the list of Http Request Nodes to which the new message flow node should be added
	 * @param mqInputNodes the list of MQ Input Nodes to which the new message flow node should be added
	 * @param mqOutputNodes the list of MQ Output Nodes to which the new message flow node should be added
	 * @param resetContentDescriptorNodes the list of Reset Content Descriptor Nodes to which the new message flow node should be added
	 * @param soapInputNodes the list of Soap Input Nodes to which the new message flow node should be added
	 * @param soapRequestNodes the list of Soap Request Nodes to which the new message flow node should be added
	 * @param timeoutControlNodes the list of Timeout Control Nodes to which the new message flow node should be added
	 * @param timeoutNotificationNodes the list of Timeout Notification Nodes to which the new message flow node should be added
	 * @param tryCatchNodes the list of Try Catch Nodes to which the new message flow node should be added
	 */
	public void parse(String fileName,
					  ArrayList<MessageFlowNode> collectorNodes,
					  ArrayList<MessageFlowNode> computeNodes,
					  ArrayList<MessageFlowNode> fileInputNodes,
					  ArrayList<MessageFlowNode> fileOutputNodes,
					  ArrayList<MessageFlowNode> httpInputNodes,
					  ArrayList<MessageFlowNode> httpRequestNodes,
					  ArrayList<MessageFlowNode> mqInputNodes,
					  ArrayList<MessageFlowNode> mqOutputNodes,
					  ArrayList<MessageFlowNode> resetContentDescriptorNodes,
					  ArrayList<MessageFlowNode> soapInputNodes,
					  ArrayList<MessageFlowNode> soapRequestNodes,
					  ArrayList<MessageFlowNode> timeoutControlNodes,
					  ArrayList<MessageFlowNode> timeoutNotificationNodes,
					  ArrayList<MessageFlowNode> tryCatchNodes) {
		LOG.debug("START");

		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
		
			XPathExpression numberOfNodes = XPathFactory.newInstance().newXPath().compile("count(//nodes)");
			int non = Integer.parseInt((String)numberOfNodes.evaluate(document, XPathConstants.STRING));
			
			for (; non > 0; non--) {
				LOG.debug("Prepare expressions - START");
				
				XPathExpression idExpr							= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@id");
				XPathExpression nameExpr						= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/translation/@string");
				XPathExpression typeExpr						= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@type");
				XPathExpression buildTreeUsingSchemaExpr		= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@parserXmlnscBuildTreeUsingXMLSchema");
				XPathExpression mixedContentRetainModeExpr		= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@parserXmlnscMixedContentRetainMode");
				XPathExpression commentsRetainModeExpr			= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@parserXmlnscCommentsRetainMode");
				XPathExpression validateMasterExpr				= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@validateMaster");
				XPathExpression messageDomainPropertyExpr		= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@messageDomainProperty");
				XPathExpression messageSetPropertyExpr			= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@messageSetProperty");
				XPathExpression requestMsgLocationInTreeExpr	= XPathFactory.newInstance().newXPath().compile("//nodes[" + non + "]/@requestMsgLocationInTree");

				XPathExpression messageDomainExpr				= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@messageDomain");
				XPathExpression messageSetExpr					= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@messageSet");
				XPathExpression resetMessageDomainExpr			= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@resetMessageDomain");
				XPathExpression resetMessageSetExpr				= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@resetMessageSet");
				XPathExpression resetMessageTypeExpr			= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@resetMessageType");
				XPathExpression resetMessageFormatExpr			= XPathFactory.newInstance().newXPath().compile("//nodes[" + non +  "]/@resetMessageFormat");
				
				LOG.debug("Prepare expressions - END");
				LOG.debug("Evaluate expressions - START");

				String id						= (String)idExpr.evaluate(document, XPathConstants.STRING);
				String name						= (String)nameExpr.evaluate(document, XPathConstants.STRING);
				String type						= (String)typeExpr.evaluate(document, XPathConstants.STRING);
				
				LOG.debug("id: " + id);
				LOG.debug("name: " + name);
				LOG.debug("type: " + type);

				if (type.contains("ComIbm") == false) {
					/* if the node is not a ComIbm node */
					LOG.debug("omitted node of type " + type);
					continue;
				}

				String messageDomainProperty	= (String)messageDomainPropertyExpr.evaluate(document, XPathConstants.STRING);
				String messageSetProperty		= (String)messageSetPropertyExpr.evaluate(document, XPathConstants.STRING);
				String requestMsgLocationInTree	= (String)requestMsgLocationInTreeExpr.evaluate(document, XPathConstants.STRING);
				String messageDomain			= (String)messageDomainExpr.evaluate(document, XPathConstants.STRING);
				String messageSet				= (String)messageSetExpr.evaluate(document, XPathConstants.STRING);
				type = type.substring(0, type.indexOf(".")).replace("ComIbm", "");
				boolean buildTreeUsingSchema 	= Boolean.parseBoolean((String)buildTreeUsingSchemaExpr.evaluate(document, XPathConstants.STRING));
				boolean mixedContentRetainMode	= ((String)mixedContentRetainModeExpr.evaluate(document, XPathConstants.STRING)).equals("all");
				boolean commentsRetainMode		= ((String)commentsRetainModeExpr.evaluate(document, XPathConstants.STRING)).equals("all");
				boolean validateMaster			= ((String)validateMasterExpr.evaluate(document, XPathConstants.STRING)).equals("contentAndValue");
				boolean resetMessageDomain	 	= Boolean.parseBoolean((String)resetMessageDomainExpr.evaluate(document, XPathConstants.STRING));
				boolean resetMessageSet 		= Boolean.parseBoolean((String)resetMessageSetExpr.evaluate(document, XPathConstants.STRING));
				boolean resetMessageType 		= Boolean.parseBoolean((String)resetMessageTypeExpr.evaluate(document, XPathConstants.STRING));
				boolean resetMessageFormat 		= Boolean.parseBoolean((String)resetMessageFormatExpr.evaluate(document, XPathConstants.STRING));
				
				XPathExpression numberOfInputTerminals = XPathFactory.newInstance().newXPath().compile("count(//connections[@targetNode='" + id + "'])");
				int noit = Integer.parseInt((String)numberOfInputTerminals.evaluate(document, XPathConstants.STRING));

				XPathExpression numberOfOutputTerminals = XPathFactory.newInstance().newXPath().compile("count(//connections[@sourceNode='" + id + "'])");
				int noot = Integer.parseInt((String)numberOfOutputTerminals.evaluate(document, XPathConstants.STRING));
				
				ArrayList<String> inputTerminals = new ArrayList<String>();
				ArrayList<String> outputTerminals = new ArrayList<String>();
				
				for (; noit > 0; noit--) {
					XPathExpression inputTerminalExpr = XPathFactory.newInstance().newXPath().compile("//connections[@targetNode='" + id + "'][" + noit + "]/@targetTerminalName");
					inputTerminals.add(((String)inputTerminalExpr.evaluate(document, XPathConstants.STRING)));
				}
				
				for (; noot > 0; noot--) {
					XPathExpression outputTerminalExpr = XPathFactory.newInstance().newXPath().compile("//connections[@sourceNode='" + id + "'][" + noot + "]/@sourceTerminalName");
					outputTerminals.add(((String)outputTerminalExpr.evaluate(document, XPathConstants.STRING)));
				}

				LOG.debug("Evaluate expressions - END");
				LOG.debug("Fill nodes - START");
				
				if (type.equals("Collector")) {
					/* Collector */
					LOG.debug("Collector");

					collectorNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("Compute")) {
					/* Compute */
					LOG.debug("Compute");
					
					computeNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("FileInput")) {
					LOG.debug("FileInput");
					
					/* FileInput */
					fileInputNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("FileOutput")) {
					LOG.debug("FileOutput");
					
					/* FileOutput */
					fileOutputNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("WSInput")) {
					LOG.debug("WSInput");
					
					/* HTTPInput */
					httpInputNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("WSRequest")) {
					LOG.debug("WSRequest");
					
					/* HTTPRequest */
					httpRequestNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("MQInput")) {
					LOG.debug("MQInput");
					
					/* MQInput */
					mqInputNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("MQOutput")) {
					LOG.debug("MQOutput");
					
					/* MQOutput */
					mqOutputNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("ResetContentDescriptor")) {
					LOG.debug("ResetContentDescriptor");
					
					/* ResetContentDescriptor */
					resetContentDescriptorNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("SOAPInput")) {
					LOG.debug("SOAPInput");
					
					/* SOAPInput */
					soapInputNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("SOAPRequest")) {
					LOG.debug("SOAPRequest");
					
					/* SOAPRequest */
					soapRequestNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("TimeoutControl")) {
					LOG.debug("TimeoutControl");
					
					/* TimeoutControl */
					timeoutControlNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("TimeoutNotification")) {
					LOG.debug("TimeoutNotification");
					
					/* TimeoutNotification */
					timeoutNotificationNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				} else if (type.equals("TryCatch")) {
					LOG.debug("TryCatch");
					
					/* TryCatch */
					tryCatchNodes.add(new MessageFlowNode(id, name, type, buildTreeUsingSchema, mixedContentRetainMode, commentsRetainMode, validateMaster, messageDomainProperty, messageSetProperty, requestMsgLocationInTree, messageDomain, messageSet, resetMessageDomain, resetMessageSet, resetMessageType, resetMessageFormat, inputTerminals, outputTerminals));
				}
				
				LOG.debug("Fill nodes - END");
			}
		} catch (XPathExpressionException e) {
			LOG.error(e.getMessage());
		} catch (SAXException e) {
			LOG.error(e.getMessage());
		} catch (ParserConfigurationException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		
		LOG.debug("END");
	}
}
