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
package com.exxeta.iss.sonar.msgflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.debt.internal.DefaultDebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;

import com.exxeta.iss.sonar.msgflow.batch.FilterNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.IMSRequestNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQInputNodeSensor;
//import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;
//import com.google.gson.Gson;

/**
 * The class defines the rules. The name, the description, the severity and the
 * tag of the rules is define in the current class.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowRulesDefinition implements RulesDefinition {

	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MessageFlowRulesDefinition.class);
//	private final Gson gson = new Gson();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sonar.api.server.rule.RulesDefinition#define(org.sonar.api.server.
	 * rule.RulesDefinition.Context)
	 */
	/**
	 * The method defines the rules for the plug-in. Names, descriptions,
	 * severities and tags for the rules are set in the method.
	 * 
	 * @param context
	 *            the Sonar context
	 */
	@Override
	public void define(Context context) {
		LOG.debug("create repository");

		NewRepository repository = context
				.createRepository(MessageFlowCheckList.REPOSITORY_KEY, MessageFlowLanguage.KEY)
				.setName(MessageFlowCheckList.REPOSITORY_NAME);
		
//		new AnnotationBasedRulesDefinition(repository, MessageFlowLanguage.KEY)
//			.addRuleClasses(false, MessageFlowCheckList.getChecks());
//		
//		for (NewRule rule : repository.rules()) {
//		      String metadataKey = rule.key();
//		      LOG.debug("Loading metadata : "+metadataKey);
//		      // Setting internal key is essential for rule templates (see SONAR-6162), and it is not done by AnnotationBasedRulesDefinition from sslr-squid-bridge version 2.5.1:
//		      rule.setInternalKey(metadataKey);
//		      addMetadata(rule, metadataKey);
//		    }

		/*
		 * CollectorNodeControlTerminal
		 */
		repository.createRule("CollectorNodeControlTerminal")
				.setName("Collector Node - The control terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the control terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * CollectorNodeFailureTerminal
		 */
		repository.createRule("CollectorNodeFailureTerminal")
				.setName("Collector Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * CollectorNodeOutTerminal
		 */
		repository.createRule("CollectorNodeOutTerminal")
				.setName("Collector Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * CollectorNodeExpireTerminal
		 */
		repository.createRule("CollectorNodeExpireTerminal")
				.setName("Collector Node - The expire terminal (output) is not connected.")
				.setHtmlDescription("The expire terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * CollectorNodeCatchTerminal
		 */
		repository.createRule("CollectorNodeCatchTerminal")
				.setName("Collector Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * CollectorNodeMonitoringEvents
		 */
		repository.createRule("CollectorNodeMonitoringEvents")
				.setName("Collector Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ComputeNodeInTerminal
		 */
		repository.createRule("ComputeNodeInTerminal")
				.setName("Compute Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ComputeNodeFailureTerminal
		 */
		repository.createRule("ComputeNodeFailureTerminal")
				.setName("Compute Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ComputeNodeOutTerminal
		 */
		repository.createRule("ComputeNodeOutTerminal")
				.setName("Compute Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ComputeNodeMonitoringEvents
		 */
		repository.createRule("ComputeNodeMonitoringEvents")
				.setName("Compute Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeFailureTerminal
		 */
		repository.createRule("FileInputNodeFailureTerminal")
				.setName("File Input Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeOutTerminal
		 */
		repository.createRule("FileInputNodeOutTerminal")
				.setName("File Input Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeEODTerminal
		 */
		repository.createRule("FileInputNodeEODTerminal")
				.setName("File Input Node - The EOD terminal (output) is not connected.")
				.setHtmlDescription(
						"If all messages in a file are processed, the end-of-data message is sent to this terminal. Connect the terminal if you need the end-of-data message.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeCatchTerminal
		 */
		repository.createRule("FileInputNodeCatchTerminal")
				.setName("File Input Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeBuildTree
		 */
		repository.createRule("FileInputNodeBuildTree")
				.setName(
						"File Input Node - Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"XML schema types are retained only if this option is enabled. Validation has to be enabled in order to select this option. (see validation)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeRetainMixedContent
		 */
		repository.createRule("FileInputNodeRetainMixedContent")
				.setName(
						"File Input Node - Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain mixed content, this checkbox should be enabled to have access to the content.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeRetainComments
		 */
		repository.createRule("FileInputNodeRetainComments")
				.setName(
						"File Input Node - Possible loss of data: 'Retain comments' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain comments, this checkbox should be enabled to have access to the comments.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeValidation
		 */
		repository.createRule("FileInputNodeValidation")
				.setName(
						"File Input Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeMessageDomain
		 */
		repository.createRule("FileInputNodeMessageDomain")
				.setName(
						"File Input Node - 'Message domain' under 'Input Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message domain should be set in order to get the message tree parsed for later access to the message. (see also message set settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeMessageSet
		 */
		repository.createRule("FileInputNodeMessageSet")
				.setName("File Input Node - 'Message set' under 'Input Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message set should be set in order to get the message tree parsed for later access to the message. (see also message domain settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileInputNodeMonitoringEvents
		 */
		repository.createRule("FileInputNodeMonitoringEvents")
				.setName("File Input Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeInTerminal
		 */
		repository.createRule("FileOutputNodeInTerminal")
				.setName("File Output Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeEODTerminal
		 */
		repository.createRule("FileOutputNodeInEODTerminal")
				.setName("File Output Node - The EOD terminal (input) is not connected.")
				.setHtmlDescription(
						"The terminal is used to signal end-of-data. Therefore, the terminal should be connected in order to trigger the end-if-file processing.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeFailureTerminal
		 */
		repository.createRule("FileOutputNodeFailureTerminal")
				.setName("File Output Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeOutTerminal
		 */
		repository.createRule("FileOutputNodeOutTerminal")
				.setName("File Output Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeEODTerminal
		 */
		repository.createRule("FileOutputNodeOutEODTerminal")
				.setName("File Output Node - The EOD terminal (output) is not connected.")
				.setHtmlDescription(
						"If the file is processed correctly, this terminal is used to send an end-of-data message. Connect the terminal, if you need the result of the processing.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeValidation
		 */
		repository.createRule("FileOutputNodeValidation")
				.setName(
						"File Output Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * FileOutputNodeMonitoringEvents
		 */
		repository.createRule("FileOutputNodeMonitoringEvents")
				.setName("File Output Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeFailureTerminal
		 */
		repository.createRule("HttpInputNodeFailureTerminal")
				.setName("Http Input Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeOutTerminal
		 */
		repository.createRule("HttpInputNodeOutTerminal")
				.setName("Http Input Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeTimeoutTerminal
		 */
		repository.createRule("HttpInputNodeTimeoutTerminal")
				.setName("Http Input Node - The timeout terminal (output) is not connected.")
				.setHtmlDescription(
						"The timeout terminal should be connected for a correct error handling in case of a timeout.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeCatchTerminal
		 */
		repository.createRule("HttpInputNodeCatchTerminal")
				.setName("Http Input Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeBuildTree
		 */
		repository.createRule("HttpInputNodeBuildTree")
				.setName(
						"Http Input Node - Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"XML schema types are retained only if this option is enabled. Validation has to be enabled in order to select this option. (see validation)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeRetainMixedContent
		 */
		repository.createRule("HttpInputNodeRetainMixedContent")
				.setName(
						"Http Input Node - Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain mixed content, this checkbox should be enabled to have access to the content.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeRetainComments
		 */
		repository.createRule("HttpInputNodeRetainComments")
				.setName(
						"Http Input Node - Possible loss of data: 'Retain comments' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain comments, this checkbox should be enabled to have access to the comments.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeValidation
		 */
		repository.createRule("HttpInputNodeValidation")
				.setName(
						"Http Input Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeMessageDomain
		 */
		repository.createRule("HttpInputNodeMessageDomain")
				.setName(
						"Http Input Node - 'Message domain' under 'Input Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message domain should be set in order to get the message tree parsed for later access to the message. (see also message set settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeMessageSet
		 */
		repository.createRule("HttpInputNodeMessageSet")
				.setName("Http Input Node - 'Message set' under 'Input Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message set should be set in order to get the message tree parsed for later access to the message. (see also message domain settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpInputNodeMonitoringEvents
		 */
		repository.createRule("HttpInputNodeMonitoringEvents")
				.setName("Http Input Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeHTTPHeader
		 */
		repository.createRule("HttpRequestNodeHTTPHeader")
				.setName(
						"Http Request Node - Make sure the HTTP header contains a 'Host' element, e.g. InputRoot.HTTPRequestHeader.\"Host\".")
				.setHtmlDescription(
						"The HTTP header should contain certain values, e.g. the value for the host. Make sure you set the values in your ESQL code.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeMessageLocationTree
		 */
		repository.createRule("HttpRequestNodeMessageLocationTree")
				.setName(
						"Http Request Node - Make sure the value of 'Request message location tree*' (see Properties 'Advanced') refers to the correct element, e.g. 'InputRoot.XMLNSC'.")
				.setHtmlDescription(
						"The HTTP payload has a certain position in the message tree (see your ESQL code). Make sure you set the correct path in the property section.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeInTerminal
		 */
		repository.createRule("HttpRequestNodeInTerminal")
				.setName("Http Request Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeFailureTerminal
		 */
		repository.createRule("HttpRequestNodeFailureTerminal")
				.setName("Http Request Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeOutTerminal
		 */
		repository.createRule("HttpRequestNodeOutTerminal")
				.setName("Http Request Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeErrorTerminal
		 */
		repository.createRule("HttpRequestNodeErrorTerminal")
				.setName("Http Request Node - The error terminal (output) is not connected.")
				.setHtmlDescription("The error terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeBuildTree
		 */
		repository.createRule("HttpRequestNodeBuildTree")
				.setName(
						"Http Request Node - Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"XML schema types are retained only if this option is enabled. Validation has to be enabled in order to select this option. (see validation)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeRetainMixedContent
		 */
		repository.createRule("HttpRequestNodeRetainMixedContent")
				.setName(
						"Http Request Node - Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain mixed content, this checkbox should be enabled to have access to the content.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeRetainComments
		 */
		repository.createRule("HttpRequestNodeRetainComments")
				.setName(
						"Http Request Node - Possible loss of data: 'Retain comments' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain comments, this checkbox should be enabled to have access to the comments.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeValidation
		 */
		repository.createRule("HttpRequestNodeValidation")
				.setName(
						"Http Request Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeMessageDomain
		 */
		repository.createRule("HttpRequestNodeMessageDomain")
				.setName(
						"Http Request Node - 'Message domain' under 'Response Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message domain should be set in order to get the message tree parsed for later access to the message. (see also message set settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeMessageSet
		 */
		repository.createRule("HttpRequestNodeMessageSet")
				.setName(
						"Http Request Node - 'Message set' under 'Response Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message set should be set in order to get the message tree parsed for later access to the message. (see also message domain settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * HttpRequestNodeMonitoringEvents
		 */
		repository.createRule("HttpRequestNodeMonitoringEvents")
				.setName("Http Request Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeFailureTerminal
		 */
		repository.createRule("MQInputNodeFailureTerminal")
				.setName("MQ Input Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeOutTerminal
		 */
		repository.createRule("MQInputNodeOutTerminal")
				.setName("MQ Input Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeCatchTerminal
		 */
		repository.createRule("MQInputNodeCatchTerminal")
				.setName("MQ Input Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeBuildTree
		 */
		repository.createRule("MQInputNodeBuildTree")
				.setName(
						"MQ Input Node - Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"XML schema types are retained only if this option is enabled. Validation has to be enabled in order to select this option. (see validation)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeRetainMixedContent
		 */
		repository.createRule("MQInputNodeRetainMixedContent")
				.setName(
						"MQ Input Node - Possible loss of data: 'Retain mixed content' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain mixed content, this checkbox should be enabled to have access to the content.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeRetainComments
		 */
		repository.createRule("MQInputNodeRetainComments")
				.setName(
						"MQ Input Node - Possible loss of data: 'Retain comments' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"If messages contain comments, this checkbox should be enabled to have access to the comments.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeValidation
		 */
		repository.createRule("MQInputNodeValidation")
				.setName(
						"MQ Input Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeMessageDomain
		 */
		repository.createRule("MQInputNodeMessageDomain")
				.setName("MQ Input Node - 'Message domain' under 'Input Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message domain should be set in order to get the message tree parsed for later access to the message. (see also message set settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeMessageSet
		 */
		repository.createRule("MQInputNodeMessageSet")
				.setName("MQ Input Node - 'Message set' under 'Input Message Parsing' is not set (see Properties).")
				.setHtmlDescription(
						"The message set should be set in order to get the message tree parsed for later access to the message. (see also message domain settings)")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQInputNodeMonitoringEvents
		 */
		repository.createRule("MQInputNodeMonitoringEvents")
				.setName("MQ Input Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQOutputNodeInTerminal
		 */
		repository.createRule("MQOutputNodeInTerminal")
				.setName("MQ Output Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQOutputNodeFailureTerminal
		 */
		repository.createRule("MQOutputNodeFailureTerminal")
				.setName("MQ Output Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQOutputNodeOutTerminal
		 */
		repository.createRule("MQOutputNodeOutTerminal")
				.setName("MQ Output Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQOutputNodeValidation
		 */
		repository.createRule("MQOutputNodeValidation")
				.setName(
						"MQ Output Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * MQOutputNodeMonitoringEvents
		 */
		repository.createRule("MQOutputNodeMonitoringEvents")
				.setName("MQ Output Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeMessageDomain
		 */
		repository.createRule("ResetContentDescriptorNodeMessageDomain")
				.setName("Reset Content Descriptor Node - 'Message Domain' under 'Basic' is not set (see Properties).")
				.setHtmlDescription(
						"The message domain should be set in order to get the message tree parsed for later access to the message. (see also message set settings)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeMessageSet
		 */
		repository.createRule("ResetContentDescriptorNodeMessageSet")
				.setName("Reset Content Descriptor Node - 'Message Set' under 'Basic' is not set (see Properties).")
				.setHtmlDescription(
						"The message set should be set in order to get the message tree parsed for later access to the message. (see also message domain settings)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeResetMessageDomain
		 */
		repository.createRule("ResetContentDescriptorNodeResetMessageDomain")
				.setName("Reset Content Descriptor Node - 'Message Domain' under 'Basic' is not set (see Properties).")
				.setHtmlDescription(
						"Reset should be enabled in order to get the message tree parsed again using the given message domain.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeResetMessageSet
		 */
		repository.createRule("ResetContentDescriptorNodeResetMessageSet")
				.setName(
						"Reset Content Descriptor Node - 'Reset Message Set' under 'Basic' is not set (see Properties).")
				.setHtmlDescription(
						"Reset should be enabled in order to get the message tree parsed again using the given message set.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeResetMessageType
		 */
		repository.createRule("ResetContentDescriptorNodeResetMessageType")
				.setName(
						"Reset Content Descriptor Node - 'Reset Message Type' under 'Basic' is not set (see Properties).")
				.setHtmlDescription(
						"Reset should be enabled in order to get the message tree parsed again using the given message type.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeResetMessageFormat
		 */
		repository.createRule("ResetContentDescriptorNodeResetMessageFormat")
				.setName(
						"Reset Content Descriptor Node - 'Reset Message Format' under 'Basic' is not set (see Properties).")
				.setHtmlDescription(
						"Reset should be enabled in order to get the message tree parsed again using the given message format.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * ResetContentDescriptorNodeMonitoringEvents
		 */
		repository.createRule("ResetContentDescriptorNodeMonitoringEvents")
				.setName("Reset Content Descriptor Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeFailureTerminal
		 */
		repository.createRule("SoapInputNodeFailureTerminal")
				.setName("Soap Input Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeOutTerminal
		 */
		repository.createRule("SoapInputNodeOutTerminal")
				.setName("Soap Input Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeTimeoutTerminal
		 */
		repository.createRule("SoapInputNodeTimeoutTerminal")
				.setName("Soap Input Node - The timeout terminal (output) is not connected.")
				.setHtmlDescription(
						"The timeout terminal should be connected for a correct error handling in case of a timeout.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeCatchTerminal
		 */
		repository.createRule("SoapInputNodeCatchTerminal")
				.setName("Soap Input Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeBuildTree
		 */
		repository.createRule("SoapInputNodeBuildTree")
				.setName(
						"Soap Input Node - Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set (see Properties).")
				.setHtmlDescription(
						"XML schema types are retained only if this option is enabled. Validation has to be enabled in order to select this option. (see validation)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeValidation
		 */
		repository.createRule("SoapInputNodeValidation")
				.setName(
						"Soap Input Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapInputNodeMonitoringEvents
		 */
		repository.createRule("SoapInputNodeMonitoringEvents")
				.setName("Soap Input Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeInTerminal
		 */
		repository.createRule("SoapRequestNodeInTerminal")
				.setName("Soap Request Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeOutTerminal
		 */
		repository.createRule("SoapRequestNodeOutTerminal")
				.setName("Soap Request Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeFailureTerminal
		 */
		repository.createRule("SoapRequestNodeFailureTerminal")
				.setName("Soap Request Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeFaultTerminal
		 */
		repository.createRule("SoapRequestNodeFaultTerminal")
				.setName("Soap Request Node - The fault terminal (output) is not connected.")
				.setHtmlDescription("The fault terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeBuildTree
		 */
		repository.createRule("SoapRequestNodeBuildTree")
				.setName(
						"Soap Request Node - Loss of data types: 'Build tree using XML schema data types' under 'Parser Options' is not set for (see Properties).")
				.setHtmlDescription(
						"XML schema types are retained only if this option is enabled. Validation has to be enabled in order to select this option. (see validation)")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeValidation
		 */
		repository.createRule("SoapRequestNodeValidation")
				.setName(
						"Soap Request Node - 'Validate' under 'Validation' is not set to 'Content and Value' (see Properties).")
				.setHtmlDescription(
						"Validation should be enabled to make sure incoming resp. outgoing messages are valid according to the given message set.")
				.setSeverity(Severity.INFO).setTags(Tags.BAD_PRACTICE);

		/*
		 * SoapRequestNodeMonitoringEvents
		 */
		repository.createRule("SoapRequestNodeMonitoringEvents")
				.setName("Soap Request Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutControlNodeInTerminal
		 */
		repository.createRule("TimeoutControlNodeInTerminal")
				.setName("Timeout Control Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutControlNodeFailureTerminal
		 */
		repository.createRule("TimeoutControlNodeFailureTerminal")
				.setName("Timeout Control Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutControlNodeOutTerminal
		 */
		repository.createRule("TimeoutControlNodeOutTerminal")
				.setName("Timeout Control Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutControlNodeMonitoringEvents
		 */
		repository.createRule("TimeoutControlNodeMonitoringEvents")
				.setName("Timeout Control Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutNotificationNodeFailureTerminal
		 */
		repository.createRule("TimeoutNotificationNodeFailureTerminal")
				.setName("Timeout Notification Node - The failure terminal (output) is not connected.")
				.setHtmlDescription("The failure terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutNotificationNodeOutTerminal
		 */
		repository.createRule("TimeoutNotificationNodeOutTerminal")
				.setName("Timeout Notification Node - The out terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the out terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutNotificationNodeCatchTerminal
		 */
		repository.createRule("TimeoutNotificationNodeCatchTerminal")
				.setName("Timeout Notification Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TimeoutNotificationNodeMonitoringEvents
		 */
		repository.createRule("TimeoutNotificationNodeMonitoringEvents")
				.setName("Timeout Notification Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TryCatchNodeTryCatch
		 */
		repository.createRule("TryCatchNodeTryCatch")
				.setName(
						"Try Catch Node - No TryCatch node found in message flow. Check exception handling of the message flow.")
				.setHtmlDescription(
						"Using a Try Catch Node is a good way to implement the exception handling for a message flow.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TryCatchNodeInTerminal
		 */
		repository.createRule("TryCatchNodeInTerminal")
				.setName("Try Catch Node - The in terminal (input) is not connected.")
				.setHtmlDescription(
						"Data is received by the in terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TryCatchNodeTryTerminal
		 */
		repository.createRule("TryCatchNodeTryTerminal")
				.setName("Try Catch Node - The try terminal (output) is not connected.")
				.setHtmlDescription(
						"Data is passed on to the next node over the try terminal. Therefore, this terminal should be connected.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TryCatchNodeCatchTerminal
		 */
		repository.createRule("TryCatchNodeCatchTerminal")
				.setName("Try Catch Node - The catch terminal (output) is not connected.")
				.setHtmlDescription("The catch terminal should be connected for a correct error handling.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);

		/*
		 * TryCatchNodeMonitoringEvents
		 */
		repository.createRule("TryCatchNodeMonitoringEvents")
				.setName("Try Catch Node - No active monitoring events found.")
				.setHtmlDescription(
						"Events are used to support transaction monitoring, transaction auditing and business process monitoring. "
								+ "Enable monitoring events if you need that support.")
				.setSeverity(Severity.MINOR).setTags(Tags.BAD_PRACTICE);
		
		//newly implemented rules starts
		/*
		 * SelfConnectingNodes
		 */
		repository.createRule("SelfConnectingNodes").setName("All Nodes - Self Connecting Nodes were found.")
				.setHtmlDescription("Use of Self Connecting node is discouraged.").setSeverity(Severity.MAJOR)
				.setTags(Tags.PERFORMANCE).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"4h"));

		/*
		 * MQNodeNameMatchesQueueName
		 */
		repository.createRule("MQNodeNameMatchesQueueName")
				.setName("MQ Nodes - MQ Node name and the underlaying queue name should match.")
				.setHtmlDescription("MQ Nodes Should be named using the underlaying queue name for clarity.")
				.setSeverity(Severity.MINOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));

		/*
		 * IMSRequestDescription
		 */
		repository.createRule("IMSRequestDescription")
				.setName("IMS Request Node - IMSRequest nodes should have the short and long description.")
				.setHtmlDescription(
						"Enter the short and Long Description for the IMS Request Node to include the transaction name and view number of the transaction being invoked.")
				.setSeverity(Severity.MAJOR).setTags(Tags.READABILITY).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * IMSRequestNodeDefinedProperties
		 */
		repository.createRule("IMSRequestNodeDefinedProperties")
				.setName("IMS Request Node - 'Use Node Defined Properties' option is checked for IMSRequest.")
				.setHtmlDescription(
						"Uncheck the option 'Use connection properties defined on Node'. Instead set Configurable service to 'IMSConnectService'")
				.setSeverity(Severity.BLOCKER).setTags(Tags.PITFALL).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * IMSRequestCommitMode
		 */
		repository.createRule("IMSRequestCommitMode")
				.setName("IMS Request Node - Wrong value set for the property 'CommitMode' for IMSRequest.")
				.setHtmlDescription("Ensure Commit mode is set to 0:COMMIT_THEN_SEND.").setSeverity(Severity.CRITICAL)
				.setTags(Tags.PERFORMANCE).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * IMSRequestMessageDomain
		 */
		repository.createRule("IMSRequestMessageDomain")
				.setName("IMS Request Node - Wrong message domain property for IMSRequest .")
				.setHtmlDescription("Ensure Message Domain is set to 'BLOB'.").setSeverity(Severity.MAJOR)
				.setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));
		/*
		 * MessageFlowComments
		 */
		repository.createRule("MessageFlowDescription")
				.setName("Message Flow - Message flow description is not Present.")
				.setHtmlDescription("Always mention flow description inside the message flow.")
				.setSeverity(Severity.MAJOR).setTags(Tags.READABILITY).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * MQHeaderNodeDetection
		 */
		repository.createRule("MQHeaderNodeDetection")
				.setName("MQ Header Node - Usage of MQ Header node is discouraged.")
				.setHtmlDescription(
						"MQ Header node should not be used unless the standards are agreed. Instead manipulate the hedaers using ESQL.")
				.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * FilterNodeNameCheck
		 */
		repository.createRule("FilterNodeNameCheck")
				.setName("Filter Node - Incorrect naming convention for Filter Node.")
				.setHtmlDescription("Filter Node Name should follow " + FilterNodeSensor.PATTERN_STRING + " pattern.")
				.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * IMSRequestNodeName
		 */

		repository.createRule("IMSRequestNodeName")
				.setName("IMS Request Node - Incorrect naming convention for IMS Request Node.")
				.setHtmlDescription(
						"IMS Request Node Name should follow " + IMSRequestNodeSensor.PATTERN_STRING + " pattern.")
				.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));
		/*
		 * MQNodeTxnMode
		 */
		
		repository.createRule("MQNodeTxnMode").setName("MQ Nodes - Transacion Mode should be set to automatic.")
				.setHtmlDescription("For MQ nodes - MQ Input, MQ Output, MQ Get, MQReply, transaction mode property "
						+ "should be set to Automatic to use transacion mode in case of persistent messages.")
				.setSeverity(Severity.MAJOR).addTags(Tags.BAD_PRACTICE).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null, "15 min"));
		
		/*
		 * HttpReplyGenerateDefaultHttpHeadersCheck
		 */

		repository.createRule("HttpReplyGenerateDefaultHttpHeadersCheck")
				.setName("HTTP Reply - "
						+ "'Generate default HTTP headers from reply or response' property should be checked.")
				.setHtmlDescription("'Generate default HTTP headers from reply or response' property "
						+ "should be checked for HTTP Reply node to generate default headers automatically.")
				.setSeverity(Severity.MAJOR).addTags(Tags.BAD_PRACTICE).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));

		/*
		 * HttpReplyIgnoreTransportFailuresCheck
		 */

		repository.createRule("HttpReplyIgnoreTransportFailuresCheck")
				.setName("HTTP Reply - " + "'Ignore transport failures' property should be checked.")
				.setHtmlDescription("'Ignore transport failures' property " + "should be checked for HTTP Reply node.")
				.setSeverity(Severity.MAJOR).addTags(Tags.BAD_PRACTICE).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"15 min"));

		/*
		 * ComputeNodeNameCheck
		 */

		repository.createRule("ComputeNodeNameCheck")
				.setName("Compute Node - Incorrect naming convention for Compute Node.")
				.setHtmlDescription("Compute Node Name should be in UpperCamelCase without spaces so that underlying module can be named normally. (example : TransformRequest)")
				.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));
//				.createParam("format").setName("ModuleNamePattern").setDefaultValue(new ComputeNodeNameCheck().format).setDescription("Regular Expression");
		
		/*
		 * TraceNodeDetection
		 */
		
		repository.createRule("TraceNodeDetection")
				.setName("Trace Node - Trace node should not be used.")
				.setHtmlDescription(
						"Trace Node should not be used in production code.")
				.setSeverity(Severity.MINOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"5 min"));
		/*
		 * XMLNSCoverXMLNS
		 */
		repository.createRule("XMLNSCoverXMLNS")
		.setName("Message Domain - XMLNSC message domain is preferred over XMLNS.")
		.setHtmlDescription(
				"Have preference for XMLNSC over XMLNS.")
		.setSeverity(Severity.MAJOR).setTags(Tags.PERFORMANCE).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"20 min"));
		/*
		 * DisconnectedNode
		 */
		repository.createRule("DisconnectedNode")
		.setName("All Nodes - There is no input connection to the node.")
		.setHtmlDescription(
				"There is no input connection to this node. The code may not be reachable or functioning.")
		.setSeverity(Severity.MAJOR).setTags(Tags.CORRECTNESS,Tags.READABILITY).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"20 min"));
		/*
		 * MQReplyWithoutMQInput
		 */
		repository.createRule("MQReplyWithoutMQInput")
				.setName("MQ Reply Node - Flow with MQ Reply node without MQ Input node.")
				.setHtmlDescription("The Flow contains MQ Reply node without MQ Input node.")
				.setSeverity(Severity.CRITICAL).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"1h 30 min"));

		/*
		 * LabelWithoutConnections
		 */
		repository.createRule("LabelWithoutConnections")
				.setName("Label Node - Label has no associated processing logic attached.")
				.setHtmlDescription("Label has no associated processing logic attached.").setSeverity(Severity.MAJOR)
				.setTags(Tags.COMPLETENESS).setDebtRemediationFunction(new DefaultDebtRemediationFunction(
						DebtRemediationFunction.Type.CONSTANT_ISSUE, null, "30 min"));

		/*
		 * AllInputTerminalsNotConnected
		 */
		repository.createRule("AllInputTerminalsNotConnected")
				.setName("Not all input terminals connected. Resources may not be processed correctly.")
				.setHtmlDescription("Not all input terminals connected. Resources may not be processed correctly.")
				.setSeverity(Severity.MAJOR).setTags(Tags.COMPLETENESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));

		/*
		 * LabelWithoutRouteTo
		 */
		repository.createRule("LabelWithoutRouteTo")
				.setName("RouteToLabel Node - Flow does not have RouteToLabel and Label node in the flow.")
				.setHtmlDescription(
						"Flow does not have RouteToLabel and Label node in the flow. "
						+ "Generally RouteToLabel and Label are placed in the same flow to make it more readable.")
				.setSeverity(Severity.MAJOR).setTags(Tags.CORRECTNESS, Tags.READABILITY).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"20 min"));

		/*
		 * FilterNodeConnection
		 */
		repository.createRule("FilterNodeConnection")
				.setName("Filter Node - All the output terminals are not connected.")
				.setHtmlDescription(
						"For a filter node true, false and unknown terminals should be connected."
						+ "If a filter node does not have the output terminals connected, "
						+ "it may lead to abnormal termination during the message flow execution.")
				.setSeverity(Severity.CRITICAL).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null, "1h"));
		/*
		 * SoapRequestTimeOut
		 */
		repository.createRule("SoapRequestTimeOut")
				.setName("Soap Request Node - Request timeout should be a positive integer.")
				.setHtmlDescription("'Request timeout' property under HTTP Transport should be set as positive integer.")
				.setSeverity(Severity.CRITICAL).setTags(Tags.PERFORMANCE).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null, "15 min"));
		/*
		 * MessageFlowInconsistentReply
		 */
		repository.createRule("MessageFlowInconsistentReply")
				.setName("Message Flow - The Message Flow does not reply to the requests consistently.")
				.setHtmlDescription(
						"The Message Flow does not reply to the requests consistently. One or more paths in the flow does not end with the Reply node.")
				.setSeverity(Severity.CRITICAL).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));
		/*
		 * SOAPAsyncNodeFault
		 */
		repository.createRule("SOAPAsyncNodeFault")
				.setName("SOAP Asynchronous Response - The 'fault' terminal should be connected.")
				.setHtmlDescription(
						"'fault' terminal for SOAP Asynchronous Response Node should be connected.")
				.setSeverity(Severity.CRITICAL).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));
		
		/*
		 * aggregateControlNodeTimeout
		 */
		repository.createRule("aggregateControlNodeTimeout")
				.setName("Aggregate Control Node - The Timeout Interval property should be set to a finite value.")
				.setHtmlDescription(
						"The timeout property should be set to a finite value. Otherwise, message flow execution may never completes in case all replies do not arrive.")
				.setSeverity(Severity.CRITICAL).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"1h 30 min"));
		
		/*
		 * ComputeNodeNameModuleName
		 */
		repository.createRule("NodeNameModuleName")
				.setName("Compute/Filter/Database Node - Node name and the underlaying module name should be same.")
				.setHtmlDescription("The ESQL module should be named after the compute node.")
				.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));

		/*
		 * OneModuleMultipleNodes
		 */
		repository.createRule("OneModuleMultipleNodes")
				.setName("Compute/Filter/Database Node - A Module should not be referred by multiple nodes.")
				.setHtmlDescription(
						"The ESQL module should not be used by more than one node. Extract common code into reusable procedures and functions.")
				.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"30 min"));
		
		/*
		 * NodeLevelAdditionalInstances
		 */
		repository.createRule("NodeLevelAdditionalInstances")
		.setName("Input Nodes - Additional instances defined at the node level.")
		.setHtmlDescription(
				"Additional Intances were defined at the input node(s).")
		.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"5 min"));
		
		/*
		 * UnusedSubFlow
		 */
		repository.createRule("UnusedSubFlow")
		.setName("Sub Flows - Unused subflows should be removed.")
		.setHtmlDescription(
				"Subflow which is not referenced anywhere should be removed.")
		.setSeverity(Severity.MINOR).setTags(Tags.COMPLETENESS,Tags.READABILITY).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"30 min"));
		
		/*
		 * mappingNodePropertiesMissing
		 */
		repository.createRule("mappingNodePropertiesMissing")
		.setName("Massage Map - Properties mapping missing in the message map.")
		.setHtmlDescription(
				"Message Map should have mapping of the properties, so the properties would not be blank after the execution completes from the node.")
		.setSeverity(Severity.MAJOR).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"15 min"));
		
		/*
		 * todoFoundInMapping
		 */
		repository.createRule("todoFoundInMapping")
				.setName("Massage Map - TODO was block found in the message map.")
				.setHtmlDescription(
						"One or more TODO blocks were found. Message Map should not have TODO blocks in it.")
				.setSeverity(Severity.MAJOR).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
						new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
								"10 min"));
		
		/*
		 * queueNamingConvention
		 */
		repository.createRule("queueNamingConvention")
		.setName("MQ Nodes - TODO was block found in the message map.")
		.setHtmlDescription(
				"MQ nodes should access alias queues. The naming convetion for alias queues is '"+MQInputNodeSensor.PATTERN_STRING+"'.")
		.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"15 min"));
		
		/*
		 * InconsistentRouteNode
		 */
		repository.createRule("InconsistentRouteNode")
		.setName("Route Node - The connections from Route Node are inconsistent.")
		.setHtmlDescription(
				"All the terminals specified in the filter table should be connected, otherwise it may cause abnormal termination and message might be lost.")
		.setSeverity(Severity.CRITICAL).setTags(Tags.CORRECTNESS).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"1 h"));
		
		/*
		 * DeprecatedNodeCheck
		 */
		
		repository.createRule("DeprecatedNodeCheck")
		.setName("Deprecated Nodes - Deprecated nodes should not be used.")
		.setHtmlDescription(
				"Usage of deprecated nodes in the message flow is discouraged.")
		.setSeverity(Severity.MINOR).setTags(Tags.DEPRECATED,Tags.STANDARD).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"5 min"));
		/*
		 * DSNWithoutDBCall
		 */
		repository.createRule("DSNWithoutDBCall")
		.setName("Compute Node - Data Source property of compute node is specified without DB call.")
		.setHtmlDescription(
				"Data Source should not be specified if compute node is not interacting with Database.")
		.setSeverity(Severity.MAJOR).setTags(Tags.CORRECTNESS,Tags.PERFORMANCE).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"10 min"));
		
		/*
		 * MavenProjectNamingConventions
		 */
		repository.createRule("MavenProjectNamingConventions")
		.setName("Project Naming - Maven project should follow the naming conventions.")
		.setHtmlDescription(
				"The maven project artifacts and modules should follow Naming conventions.")
		.setSeverity(Severity.MAJOR).setTags(Tags.STANDARD).setDebtRemediationFunction(
				new DefaultDebtRemediationFunction(DebtRemediationFunction.Type.CONSTANT_ISSUE, null,
						"10 min"));
		// add more rules here

		repository.done();

		LOG.debug("created repository: " + repository.toString());
	}
	
//	 @Nullable
//	  private static String readRuleDefinitionResource(String fileName) {
//		 LOG.debug("Opening file "+fileName);
//	    URL resource = MessageFlowRulesDefinition.class.getResource("/org/sonar/l10n/msgflow/rules/" + fileName);
//	    if (resource == null) {
//	      return null;
//	    }
//	    try {
//	      return Resources.toString(resource, Charsets.UTF_8);
//	    } catch (IOException e) {
//	      throw new IllegalStateException("Failed to read: " + resource, e);
//	    }
//	  }
//
//
//	  private void addMetadata(NewRule rule, String metadataKey) {
//		LOG.debug("Adding metadata for "+metadataKey);
//	    String json = readRuleDefinitionResource(metadataKey + ".json");
//	    if (json != null) {
//	      RuleMetadata metadata = gson.fromJson(json, RuleMetadata.class);
//	      rule.setSeverity(metadata.defaultSeverity.toUpperCase(Locale.US));
//	      rule.setName(metadata.title);
//	      rule.setTags(metadata.tags);
////	      rule.setType(RuleType.valueOf(metadata.type));
//	      rule.setStatus(RuleStatus.valueOf(metadata.status.toUpperCase(Locale.US)));
//	      rule.setHtmlDescription(metadata.htmldesc);
//
//	      if (metadata.remediation != null) {
//	        // metadata.remediation is null for template rules
//	        rule.setDebtRemediationFunction(metadata.remediation.remediationFunction(rule.debtRemediationFunctions()));
////	        rule.setGapDescription(metadata.remediation.linearDesc);
//	      }
//	    }
//	  }
//
//	  private static class RuleMetadata {
//	    String title;
//	    String status;
//	    String htmldesc;
//	    @Nullable
//	    Remediation remediation;
//
//	    String[] tags;
//	    String defaultSeverity;
//	  }
//
//	  private static class Remediation {
//	    String func;
//	    String constantCost;
//	    String linearOffset;
//	    String linearFactor;
//
//	    private DebtRemediationFunction remediationFunction(DebtRemediationFunctions drf) {
//	      if (func.startsWith("Constant")) {
//	        return drf.constantPerIssue(constantCost.replace("mn", "min"));
//	      }
//	      if ("Linear".equals(func)) {
//	        return drf.linear(linearFactor.replace("mn", "min"));
//	      }
//	      return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
//	    }
//	  }
	
}
