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
import java.util.regex.Pattern;

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
 * configuration of a MQ Input Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MQInputNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(MQInputNodeSensor.class);
	
	public final static String PATTERN_STRING = "^[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.(AI|AO)$";

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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMqInputNodes().iterator();

			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();

				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					createNewIssue(context, inputFile, "MQInputNodeFailureTerminal",
							"The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: "
							+ msgFlowNode.getType() + ") is not connected.");
				}

				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					createNewIssue(context, inputFile, "MQInputNodeOutTerminal",
							"The out terminal (output) for '" + msgFlowNode.getName() + "' (type: "
							+ msgFlowNode.getType() + ") is not connected.");
				}

				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.catch")) {
					createNewIssue(context, inputFile, "MQInputNodeCatchTerminal",
							"The catch terminal (output) for '" + msgFlowNode.getName() + "' (type: "
							+ msgFlowNode.getType() + ") is not connected.");
				}

				if (msgFlowNode.isBuildTreeUsingSchema() == false) {
					createNewIssue(context, inputFile, "MQInputNodeBuildTree",
							"Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") (see Properties).");
				}

				if (msgFlowNode.isMixedContentRetainMode() == false) {
					createNewIssue(context, inputFile, "MQInputNodeRetainMixedContent",
							"Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") (see Properties).");
				}

				if (msgFlowNode.isCommentsRetainMode() == false) {
					createNewIssue(context, inputFile, "MQInputNodeRetainComments",
							"Possible loss of data: 'Retain comments' under 'Parser Options' is not set for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") (see Properties).");
				}

				if (msgFlowNode.isValidateMaster() == false) {
					createNewIssue(context, inputFile, "MQInputNodeValidation",
							"'Validate' under 'Validation' is not set to 'Content and Value' for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") (see Properties).");
				}

				if (msgFlowNode.getMessageDomainProperty().equals("")) {
					createNewIssue(context, inputFile, "MQInputNodeMessageDomain",
							"'Message domain' under 'Input Message Parsing' is not set for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") (see Properties).");
				}

				// The setting of a message set is only checked for the
				// following domains: MRM, XMLNSC and DataObject.
				if (msgFlowNode.getMessageSetProperty().equals("")
						&& (msgFlowNode.getMessageDomainProperty().equals("MRM")
								|| msgFlowNode.getMessageDomainProperty().equals("XMLNSC")
								|| msgFlowNode.getMessageDomainProperty().equals("DataObject"))) {
					createNewIssue(context, inputFile, "MQInputNodeMessageSet",
							"'Message set' under 'Input Message Parsing' is not set for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") (see Properties).");
				}

				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					createNewIssue(context, inputFile, "MQInputNodeMonitoringEvents",
							"There are no monitoring events defined or the "
							+ "existing events are disabled for '" + msgFlowNode.getName() + "' (type: "
							+ msgFlowNode.getType() + ") (see Properties).");
				}

				if (!((String) msgFlowNode.getProperties().get("queueName")).equals(msgFlowNode.getName())) {
					createNewIssue(context, inputFile, "MQNodeNameMatchesQueueName",
							"The name of MQ Node '" + msgFlowNode.getName() + "' (type: "
							+ msgFlowNode.getType() + ") does not match the underlaying queue name.");
				}

				if (!msgFlowNode.getProperties().get("transactionMode").equals("automatic")) {
					createNewIssue(context, inputFile, "MQNodeTxnMode",
							"The 'Transaction Mode' property of " + msgFlowNode.getName() + "(type:"
							+ msgFlowNode.getType() + ") node is not set to Automatic.");
				}

				if (msgFlowNode.getMessageDomainProperty().equals("XMLNS")) {
					createNewIssue(context, inputFile, "XMLNSCoverXMLNS",
							"'Message domain' under 'Input Message Parsing' for '" + msgFlowNode.getName()
							+ "' (type: " + msgFlowNode.getType()
							+ ") is set as XMLNS. XMLNSC is preferred over XMLNS.");
				}

				if ((!((String) msgFlowNode.getProperties().get("componentLevel")).isEmpty())
						&& (((String) msgFlowNode.getProperties().get("componentLevel")).equals("node"))
						&& (!((String) msgFlowNode.getProperties().get("additionalInstances")).isEmpty())
						&& (Integer.parseInt((String)(msgFlowNode.getProperties().get("additionalInstances"))) > 0)) {
					createNewIssue(context, inputFile, "NodeLevelAdditionalInstances",
							"Additional Intances defined at the node level for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				if (!checkMQQueueName((String) msgFlowNode.getProperties().get("queueName"))) {
					createNewIssue(context, inputFile, "queueNamingConvention",
							"Naming convention for the queue specified on '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") is not correct.");
				}
			}
		}
	}
	
	public static boolean checkMQQueueName(String name) {
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
	}
}