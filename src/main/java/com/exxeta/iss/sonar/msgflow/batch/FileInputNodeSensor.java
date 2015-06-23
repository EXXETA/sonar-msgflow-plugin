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
 * configuration of a FileInput Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class FileInputNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(FileInputNodeSensor.class);
	
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
	public FileInputNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getFileInputNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeFailureTerminal"))
				    	        	  .message("The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeOutTerminal"))
				    	        	  .message("The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.EOD")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeEODTerminal"))
				    	        	  .message("The EOD terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.catch")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeCatchTerminal"))
				    	        	  .message("The catch terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isBuildTreeUsingSchema() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeBuildTree"))
				    	        	  .message("Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isMixedContentRetainMode() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeRetainMixedContent"))
				    	        	  .message("Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isCommentsRetainMode() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeRetainComments"))
				    	        	  .message("Possible loss of data: 'Retain comments' under 'Parser Options' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isValidateMaster() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeValidation"))
				    	        	  .message("'Validate' under 'Validation' is not set to 'Content and Value' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeMessageDomain"))
				    	        	  .message("'Message domain' under 'Input Message Parsing' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.getMessageSetProperty().equals("")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeMessageSet"))
				    	        	  .message("'Message set' under 'Input Message Parsing' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (!msgFlowNode.areMonitoringEventsEnabled()) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FileInputNodeMonitoringEvents"))
				    	        	  .message("There are no monitoring events defined or the "
				    	        	  		 + "existing events are disabled for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
			}
			
		}
	}

}
