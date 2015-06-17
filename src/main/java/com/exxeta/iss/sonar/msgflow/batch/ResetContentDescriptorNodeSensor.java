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
 * configuration of a Reset Content Descriptor Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class ResetContentDescriptorNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(ResetContentDescriptorNodeSensor.class);
	
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
	public ResetContentDescriptorNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMqOutputNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();

				if (msgFlowNode.getMessageDomain().equals("")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "ResetContentDescriptorNodeMessageDomain"))
				    	        	  .message("'Message Domain' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (!msgFlowNode.getMessageSet().equals("")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "ResetContentDescriptorNodeMessageSet"))
				    	        	  .message("'Message Set' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isResetMessageDomain() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "ResetContentDescriptorNodeResetMessageDomain"))
				    	        	  .message("'Message Domain' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isResetMessageSet() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "ResetContentDescriptorNodeResetMessageSet"))
				    	        	  .message("'Reset Message Set' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isResetMessageType() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "ResetContentDescriptorNodeResetMessageType"))
				    	        	  .message("'Reset Message Type' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isResetMessageFormat() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "ResetContentDescriptorNodeResetMessageFormat"))
				    	        	  .message("'Reset Message Format' under 'Basic' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not set (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
			}
		}
	}

}
