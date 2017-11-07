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
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;

/**
 * The class creates a profile and adds the existing rules to the created profile.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowProfile extends ProfileDefinition {
	
	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MessageFlowProfile.class);
	
	/* (non-Javadoc)
	 * @see org.sonar.api.profiles.ProfileDefinition#createProfile(org.sonar.api.utils.ValidationMessages)
	 */
	/**
	 * The method creates a new profile. The defined rules are activated in the method.
	 * 
	 * @return a new RulesProfile
	 */
	@Override
	public RulesProfile createProfile(ValidationMessages validationMessages) {
		LOG.debug("create profile");
		
		RulesProfile profile = RulesProfile.create(MessageFlowCheckList.SONAR_WAY_PROFILE, MessageFlowLanguage.KEY);

		/* CollectorNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "CollectorNodeControlTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "CollectorNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "CollectorNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "CollectorNodeExpireTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "CollectorNodeCatchTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "CollectorNodeMonitoringEvents"), null);
		
		/* ComputeNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ComputeNodeInTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ComputeNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ComputeNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ComputeNodeMonitoringEvents"), null);
		
		/* File Input Node rules */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeOutTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeEODTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeCatchTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeBuildTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeRetainMixedContent"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeRetainComments"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeMessageDomain"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeMessageSet"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileInputNodeMonitoringEvents"), null);
		
		/* FileOutputNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeInTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeInEODTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeFailureTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeOutTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeOutEODTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FileOutputNodeMonitoringEvents"), null);

		/* HttpInputNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeTimeoutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeCatchTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeBuildTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeRetainMixedContent"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeRetainComments"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeMessageDomain"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeMessageSet"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpInputNodeMonitoringEvents"), null);

		/* HttpRequestNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeHTTPHeader"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeMessageLocationTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeInTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeErrorTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeBuildTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeRetainMixedContent"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeRetainComments"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeMessageDomain"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeMessageSet"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpRequestNodeMonitoringEvents"), null);
		
		/* MQInputNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeCatchTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeBuildTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeRetainMixedContent"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeRetainComments"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeMessageDomain"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeMessageSet"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQInputNodeMonitoringEvents"), null);

		/* MQOutputNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQOutputNodeInTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQOutputNodeFailureTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQOutputNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQOutputNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQOutputNodeMonitoringEvents"), null);

		/* ResetContentDescriptorNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeMessageDomain"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeMessageSet"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeResetMessageDomain"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeResetMessageSet"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeResetMessageType"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeResetMessageFormat"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ResetContentDescriptorNodeMonitoringEvents"), null);

		/* SoapInputNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeTimeoutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeCatchTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeBuildTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapInputNodeMonitoringEvents"), null);

		/* SoapRequestNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeInTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeFaultTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeBuildTree"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeValidation"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SoapRequestNodeMonitoringEvents"), null);

		/* TimeoutControlNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutControlNodeInTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutControlNodeFailureTerminal"), null);
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutControlNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutControlNodeMonitoringEvents"), null);

		/* TimeoutNotificationNode */
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutNotificationNodeFailureTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutNotificationNodeOutTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutNotificationNodeCatchTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TimeoutNotificationNodeMonitoringEvents"), null);

		/* TryCatchNode */
		//profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TryCatchNodeTryCatch"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TryCatchNodeInTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TryCatchNodeTryTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TryCatchNodeCatchTerminal"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TryCatchNodeMonitoringEvents"), null);
		
		/* Below rules are being added here*/
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "SelfConnectingNodes"), null);
//		MQ Node names should match the under laying queue name - (Not active rule. Activate if applicable)
//		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQNodeNameMatchesQueueName"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "IMSRequestDescription"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "IMSRequestNodeDefinedProperties"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "IMSRequestCommitMode"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "IMSRequestMessageDomain"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MessageFlowDescription"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQHeaderNodeDetection"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "FilterNodeNameCheck"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "IMSRequestNodeName"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "MQNodeTxnMode"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpReplyIgnoreTransportFailuresCheck"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "HttpReplyGenerateDefaultHttpHeadersCheck"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "ComputeNodeNameCheck"), null);
		profile.activateRule(Rule.create(MessageFlowCheckList.REPOSITORY_KEY, "TraceNodeDetection"), null);
		
		// add more rules here
		
		LOG.debug("created profile: " + profile.toString());
		
		return profile;
	}

}
