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

import java.util.Arrays;
import java.util.Iterator;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a File Output Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class FileOutputNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(FileOutputNodeSensor.class);
	
	/**
	 * Variable to hold file system information, e.g. the file names of the project files.
	 */
	private final FileSystem fs;
	
	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;
	
	/**
	  * Use of IoC to get FileSystem and ResourcePerspectives
	  */
	public FileOutputNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.resources.Project)
	 */
	/**
	 * The method defines the language of the file to be analysed.
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
		for (InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage("msgflow"))) {
			
			/* 
			 * retrieve the message flow object
			 */
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getFileOutputNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				
				if (!msgFlowNode.getInputTerminals().contains("InTerminal.in")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeInTerminal"))
				    	        	  .message("The in terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
				
				if (!msgFlowNode.getInputTerminals().contains("InTerminal.EOD") &&
					!msgFlowNode.getRecordDefinition().equals("")) { /* "Record is Whole File" - element "recordDefinition" does not exist */
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeInEODTerminal"))
				    	        	  .message("The EOD terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
				
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeFailureTerminal"))
				    	        	  .message("The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeOutTerminal"))
				    	        	  .message("The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.EOD")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeOutEODTerminal"))
				    	        	  .message("The EOD terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
				}
				
				if (msgFlowNode.isValidateMaster() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeValidation"))
				    	        	  .message("'Validate' under 'Validation' is not set to 'Content and Value' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .build());
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileOutputNodeMonitoringEvents"))
				    	        	  .message("There are no monitoring events defined or the "
				    	        	  		 + "existing events are disabled for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .build());
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "DisconnectedNode"))
				    	        	  .message("There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				
				if(msgFlowNode.getInputTerminals().size()<2){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "AllInputTerminalsNotConnected"))
				    	        	  .message("One or more input terminals of node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") are not connected.")
				    	        	  .build());
				}
			}
		}
	}

}
