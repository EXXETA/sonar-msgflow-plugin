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
 * configuration of a FileInput Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class FileInputNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(FileInputNodeSensor.class);
	
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getFileInputNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					createNewIssue(context, inputFile, "FileInputNodeFailureTerminal", 
							"The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					createNewIssue(context, inputFile, "FileInputNodeOutTerminal", 
							"The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.EOD")) {
					createNewIssue(context, inputFile, "FileInputNodeEODTerminal",
				    	    "The EOD terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.catch")) {
					createNewIssue(context, inputFile, "FileInputNodeCatchTerminal",
							"The catch terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
				
				if (msgFlowNode.isBuildTreeUsingSchema() == false) {
					createNewIssue(context, inputFile, "FileInputNodeBuildTree",
				    	    "Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set for '" 
				    	    + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.isMixedContentRetainMode() == false) {
					createNewIssue(context, inputFile, "FileInputNodeRetainMixedContent",
				    	    "Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set for '" + msgFlowNode.getName() 
				    	    + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.isCommentsRetainMode() == false) {
					createNewIssue(context, inputFile, "FileInputNodeRetainComments",
							"Possible loss of data: 'Retain comments' under 'Parser Options' is not set for '" 
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.isValidateMaster() == false) {
					createNewIssue(context, inputFile, "FileInputNodeValidation",
							"'Validate' under 'Validation' is not set to 'Content and Value' for '" + msgFlowNode.getName() 
							+ "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("")) {
					createNewIssue(context, inputFile, "FileInputNodeMessageDomain",
							"'Message domain' under 'Input Message Parsing' is not set for '" + msgFlowNode.getName() 
							+ "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				// The setting of a message set is only checked for the following domains:
				// MRM, XMLNSC and DataObject.
				if (msgFlowNode.getMessageSetProperty().equals("") &&
					(msgFlowNode.getMessageDomainProperty().equals("MRM") ||
					 msgFlowNode.getMessageDomainProperty().equals("XMLNSC") ||
					 msgFlowNode.getMessageDomainProperty().equals("DataObject"))) {

					createNewIssue(context, inputFile, "FileInputNodeMessageSet",
							"'Message set' under 'Input Message Parsing' is not set for '" + msgFlowNode.getName() 
							+ "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					createNewIssue(context, inputFile, "FileInputNodeMonitoringEvents",
							"There are no monitoring events defined or the existing events are disabled for '" 
						    + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("XMLNS")) {
					createNewIssue(context, inputFile, "XMLNSCoverXMLNS",
							"'Message domain' under 'Input Message Parsing' for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") is set as XMLNS. XMLNSC is preferred over XMLNS.");
				}
				
				if ((!((String) msgFlowNode.getProperties().get("componentLevel")).isEmpty())
						&& (((String) msgFlowNode.getProperties().get("componentLevel")).equals("node"))
						&& (!((String) msgFlowNode.getProperties().get("additionalInstances")).isEmpty())
						&& (Integer.parseInt((String)(msgFlowNode.getProperties().get("additionalInstances"))) > 0)) {
					createNewIssue(context, inputFile, "NodeLevelAdditionalInstances",
							"Additional Intances defined at the node level for"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
			}
		}
	}
}
