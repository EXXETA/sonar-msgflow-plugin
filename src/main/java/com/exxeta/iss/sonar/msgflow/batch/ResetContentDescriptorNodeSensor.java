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
 * configuration of a Reset Content Descriptor Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class ResetContentDescriptorNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(ResetContentDescriptorNodeSensor.class);
	
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getResetContentDescriptorNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();

				if (msgFlowNode.getMessageDomain().equals("")) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeMessageDomain",
							"'Message Domain' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).");
				}
				
				// The setting of a message set is only checked for the following domains:
				// MRM, XMLNSC and DataObject.
				if (msgFlowNode.getMessageSet().equals("") &&
					(msgFlowNode.getMessageDomain().equals("MRM") ||
					 msgFlowNode.getMessageDomain().equals("XMLNSC") ||
					 msgFlowNode.getMessageDomain().equals("DataObject"))) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeMessageSet",
							"'Message Set' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).");
				}
				
				if (msgFlowNode.isResetMessageDomain() == false) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeResetMessageDomain",
							"'Message Domain' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).");
				}
				
				// The resetting of a message set is only checked for the following domains:
				// MRM, XMLNSC and DataObject.
				if ((msgFlowNode.isResetMessageSet() == false) &&
					(msgFlowNode.getMessageDomain().equals("MRM") ||
					 msgFlowNode.getMessageDomain().equals("XMLNSC") ||
					 msgFlowNode.getMessageDomain().equals("DataObject"))) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeResetMessageSet",
							"'Reset Message Set' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).");
				}
				
				if (msgFlowNode.isResetMessageType() == false) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeResetMessageType",
							"'Reset Message Type' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).");
				}
				
				if (msgFlowNode.isResetMessageFormat() == false) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeResetMessageFormat",
							"'Reset Message Format' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).");
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					createNewIssue(context, inputFile, "ResetContentDescriptorNodeMonitoringEvents",
							"There are no monitoring events defined or the "
				    	    + "existing events are disabled for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (msgFlowNode.getMessageDomain().equals("XMLNS")) {
					createNewIssue(context, inputFile, "XMLNSCoverXMLNS",
							"'Message domain' under 'Basic' for '"
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
