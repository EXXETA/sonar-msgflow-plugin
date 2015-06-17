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
 * configuration of a Http Request Node.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class HttpRequestNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(HttpRequestNodeSensor.class);
	
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
	public HttpRequestNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getHttpRequestNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				
				/* general hints are given here */
				Issuable issuable1 = perspectives.as(Issuable.class, inputFile);
			    issuable1.addIssue(issuable1.newIssueBuilder()
			    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeHTTPHeader"))
			    	        	  .message("Make sure the HTTP header for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") contains a 'Host' element, e.g. InputRoot.HTTPRequestHeader.\"Host\".")
			    	        	  .line(1)
			    	        	  .build());
				
				Issuable issuable2 = perspectives.as(Issuable.class, inputFile);
			    issuable2.addIssue(issuable2.newIssueBuilder()
			    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeMessageLocationTree"))
			    	        	  .message("Make sure the value of 'Request message location tree*' (see Properties 'Advanced') for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") refers to the correct element, e.g. 'InputRoot.XMLNSC'. "
			    	        	  		+ "The current value is '" + msgFlowNode.getRequestMsgLocationInTree() + "'.")
			    	        	  .line(1)
			    	        	  .build());
				/* general hints are given here - end */
				
				if (!msgFlowNode.getInputTerminals().contains("InTerminal.in")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeInTerminal"))
				    	        	  .message("The in terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}

				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.failure")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeFailureTerminal"))
				    	        	  .message("The failure terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.out")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeOutTerminal"))
				    	        	  .message("The out terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
					
				if (!msgFlowNode.getOutputTerminals().contains("OutTerminal.error")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeErrorTerminal"))
				    	        	  .message("The error terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isBuildTreeUsingSchema() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeBuildTree"))
				    	        	  .message("Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isMixedContentRetainMode() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeRetainMixedContent"))
				    	        	  .message("Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isCommentsRetainMode() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeRetainComments"))
				    	        	  .message("Possible loss of data: 'Retain comments' under 'Parser Options' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.isValidateMaster() == false) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeValidation"))
				    	        	  .message("'Validate' under 'Validation' is not set to 'Content and Value' for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeMessageDomain"))
				    	        	  .message("'Message domain' under 'Response Message Parsing' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
				
				if (msgFlowNode.getMessageSetProperty().equals("")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "HttpRequestNodeMessageSet"))
				    	        	  .message("'Message set' under 'Response Message Parsing' is not set for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") (see Properties).")
				    	        	  .line(1)
				    	        	  .build());
				}
			}
		}
	}

}
