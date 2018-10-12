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
package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Iterator;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a Http Request Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class HttpRequestNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(HttpRequestNodeSensor.class);
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getHttpRequestNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				
				// general hints are given here
				createNewIssue(context, inputFile, "HttpRequestNodeHTTPHeader",
						"Make sure the HTTP header for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() 
						+ ") contains a 'Host' element, e.g. InputRoot.HTTPRequestHeader.\"Host\".");
				
				createNewIssue(context, inputFile, "HttpRequestNodeMessageLocationTree",
						"Make sure the value of 'Request message location tree*' (see Properties 'Advanced') for '" + msgFlowNode.getName() 
						+ "' (type: " + msgFlowNode.getType() + ") refers to the correct element, e.g. 'InputRoot.XMLNSC'. "
			    	    + "The current value is '" + msgFlowNode.getRequestMsgLocationInTree() + "'.");
				// general hints are given here - end
				
				if (!msgFlowNode.getInputTerminals().contains("InTerminal.in")) {
					createNewIssue(context, inputFile, "HttpRequestNodeInTerminal",
							"The in terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.", 1);
				}

				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					createNewIssue(context, inputFile, "HttpRequestNodeFailureTerminal",
							"The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					createNewIssue(context, inputFile, "HttpRequestNodeOutTerminal",
							"The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.error")) {
					createNewIssue(context, inputFile, "HttpRequestNodeErrorTerminal",
							"The error terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
				
				if (msgFlowNode.isBuildTreeUsingSchema() == false) {
					createNewIssue(context, inputFile, "HttpRequestNodeBuildTree",
							"Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set for '" + msgFlowNode.getName() 
							+ "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.isMixedContentRetainMode() == false) {
					createNewIssue(context, inputFile, "HttpRequestNodeRetainMixedContent",
							"Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set for '" + msgFlowNode.getName() 
							+ "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.isCommentsRetainMode() == false) {
					createNewIssue(context, inputFile, "HttpRequestNodeRetainComments",
							"Possible loss of data: 'Retain comments' under 'Parser Options' is not set for '" + msgFlowNode.getName() 
							+ "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.isValidateMaster() == false) {
					createNewIssue(context, inputFile, "HttpRequestNodeValidation",
							"'Validate' under 'Validation' is not set to 'Content and Value' for '" + msgFlowNode.getName() + "' (type: " 
							+ msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("")) {
					createNewIssue(context, inputFile, "HttpRequestNodeMessageDomain",
							"'Message domain' under 'Response Message Parsing' is not set for '" + msgFlowNode.getName() + "' (type: " 
					        + msgFlowNode.getType() + ") (see Properties).");
				}
				
				// The setting of a message set is only checked for the following domains:
				// MRM, XMLNSC and DataObject.
				if (msgFlowNode.getMessageSetProperty().equals("") &&
					(msgFlowNode.getMessageDomainProperty().equals("MRM") ||
					 msgFlowNode.getMessageDomainProperty().equals("XMLNSC") ||
					 msgFlowNode.getMessageDomainProperty().equals("DataObject"))) {
					createNewIssue(context, inputFile, "HttpRequestNodeMessageSet",
							"'Message set' under 'Response Message Parsing' is not set for '" + msgFlowNode.getName() + "' (type: " 
						    + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					createNewIssue(context, inputFile, "HttpRequestNodeMonitoringEvents",
							"There are no monitoring events defined or the existing events are disabled for '" 
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("XMLNS")) {
					createNewIssue(context, inputFile, "XMLNSCoverXMLNS",
							"'Message domain' under 'Response Message Parsing' for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") is set as XMLNS. XMLNSC is preferred over XMLNS.");
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					createNewIssue(context, inputFile, "DisconnectedNode",
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
			}
		}
	}
}
