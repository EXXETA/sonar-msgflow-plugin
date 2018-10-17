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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.check.ComputeNodeNameCheck;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a Compute Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class ComputeNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(ComputeNodeSensor);
	
	/**
	 * pattern to check the compute node name
	 */
	public final static String PATTERN_STRING = new ComputeNodeNameCheck().format;
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext.class)
	 */
	@Override
	public void execute(SensorContext context) {
		ArrayList<String> moduleNameExpressionList = new ArrayList<String>();
		Set<String> moduleSet = new TreeSet<String>();

		FileSystem fs = context.fileSystem();		
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getComputeNodes().iterator();
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				String moduleName = (String)msgFlowNode.getProperties().get("computeExpression");
				moduleNameExpressionList.add(moduleName);
				
				if(!moduleSet.add(moduleName)){
					createNewIssue(context, inputFile, "OneModuleMultipleNodes", 
							"Multiple Compute nodes refers to same module '" + moduleName + "'.");
				}

				if (!msgFlowNode.getInputTerminals().contains("InTerminal.in")) {
					createNewIssue(context, inputFile, "ComputeNodeInTerminal", 
							"The in terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
				
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					createNewIssue(context, inputFile, "ComputeNodeFailureTerminal", 
							"The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					createNewIssue(context, inputFile, "ComputeNodeOutTerminal", 
							"The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					createNewIssue(context, inputFile, "ComputeNodeMonitoringEvents", "There are no monitoring events defined or the "
    	        	  		 + "existing events are disabled for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).");
				}
				
				if (!checkComputeNodeName(msgFlowNode.getName())) {
					createNewIssue(context, inputFile, "ComputeNodeNameCheck", 
							"The name of '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") should be in UpperCamelCase without spaces.");
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					createNewIssue(context, inputFile, "DisconnectedNode", 
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				
				if(!msgFlowNode.getName().equals(msgFlowNode.getProperties().get("computeExpression"))){
					createNewIssue(context, inputFile, "NodeNameModuleName", 
							"The node name and the underlaying module name should match for '" 
									+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}				
			}
		}
	}

	public static boolean checkComputeNodeName(String name) {
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
	}
}