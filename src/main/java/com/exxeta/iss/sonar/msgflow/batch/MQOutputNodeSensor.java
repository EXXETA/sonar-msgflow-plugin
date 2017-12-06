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

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a MQ Output Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MQOutputNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);
	
	/**
	 * Variable to hold file system information, e.g. the file names of the project files.
	 */
	private final FileSystem fs;
	
	public final static String PATTERN_STRING = "^[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.(AI|AO)$";
	
	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;
	
	/**
	  * Use of IoC to get FileSystem and ResourcePerspectives
	  */
	public MQOutputNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.resources.Project)
	 */
	@Override
	public boolean shouldExecuteOnProject(Project arg0) {
		// This sensor is executed only when there are msgflow files
	    return fs.hasFiles(fs.predicates().hasLanguage("msgflow"));
	}

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.Sensor#analyse(org.sonar.api.resources.Project, org.sonar.api.batch.SensorContext)
	 */
	/**
	 * The method where the analysis of the connections and configuration of 
	 * the message flow node takes place.
	 */
	@Override
	public void analyse(Project arg0, SensorContext arg1) {
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			
			/* 
			 * retrieve the message flow object
			 */
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMqOutputNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				
				if (!msgFlowNode.getInputTerminals().contains("InTerminal.in")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "MQOutputNodeInTerminal"))
				    	        	  .message("The in terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
				
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "MQOutputNodeFailureTerminal"))
				    	        	  .message("The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "MQOutputNodeOutTerminal"))
				    	        	  .message("The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
				
				if (msgFlowNode.isValidateMaster() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "MQOutputNodeValidation"))
				    	        	  .message("'Validate' under 'Validation' is not set to 'Content and Value' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .build());
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "MQOutputNodeMonitoringEvents"))
				    	        	  .message("There are no monitoring events defined or the "
				    	        	  		 + "existing events are disabled for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .build());
				}
				
				if(!((String)msgFlowNode.getProperties().get("queueName")).equals(msgFlowNode.getName())){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    		.ruleKey(RuleKey.of("msgflow", "MQNodeNameMatchesQueueName"))
				    		.message("The name of MQ Node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") does not match the underlaying queue name.")
				    	        	  .build());
				}
				
				if(!((String)msgFlowNode.getProperties().get("transactionMode")).isEmpty()
						|| msgFlowNode.getProperties().get("transactionMode").equals("yes")
						|| msgFlowNode.getProperties().get("transactionMode").equals("no")) {

					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "MQNodeTxnMode"))
							.message("The 'Transaction Mode' property of " + msgFlowNode.getName() + "(type:"
									+ msgFlowNode.getType() + ") node is not set to Automatic.")
							.build());

				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "DisconnectedNode"))
				    	        	  .message("There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				
				if (!checkMQQueueName((String) msgFlowNode.getProperties().get("queueName"))) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(
							issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "queueNamingConvention"))
									.message("Naming convention for the queue specified on '"
											+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
											+ ") is not correct.")
									.build());
				}
			}
		}
	}
	public static boolean checkMQQueueName(String name) {

		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();

	}

}
