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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;

/**
 * The class ... TODO: add comment
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowProfileTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.MessageFlowProfile#createProfile(org.sonar.api.utils.ValidationMessages)}.
	 */
	@Test
	public final void testCreateProfileValidationMessages() {
		MessageFlowProfile mfp = new MessageFlowProfile();
		RulesProfile rp = mfp.createProfile(ValidationMessages.create());
		
		/* Currently, there should be 87 rules. */
		assertEquals(101, rp.getActiveRules().size());
		
		ArrayList<String> ruleKeys = new ArrayList<String>();
		
		Iterator<org.sonar.api.rules.ActiveRule> ars = rp.getActiveRules().iterator();
		
		while (ars.hasNext()) {
			ruleKeys.add(ars.next().getRuleKey());
		}
		
		assertTrue("CollectorNodeControlTerminal is not defined as a rule.", ruleKeys.contains("CollectorNodeControlTerminal"));
		assertTrue("CollectorNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("CollectorNodeFailureTerminal"));
		assertTrue("CollectorNodeOutTerminal is not defined as a rule.", ruleKeys.contains("CollectorNodeOutTerminal"));
		assertTrue("CollectorNodeExpireTerminal is not defined as a rule.", ruleKeys.contains("CollectorNodeExpireTerminal"));
		assertTrue("CollectorNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("CollectorNodeCatchTerminal"));
		assertTrue("CollectorNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("CollectorNodeMonitoringEvents"));
		assertTrue("ComputeNodeInTerminal is not defined as a rule.", ruleKeys.contains("ComputeNodeInTerminal"));
		assertTrue("ComputeNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("ComputeNodeFailureTerminal"));
		assertTrue("ComputeNodeOutTerminal is not defined as a rule.", ruleKeys.contains("ComputeNodeOutTerminal"));
		assertTrue("ComputeNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("ComputeNodeMonitoringEvents"));
		assertTrue("FileInputNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("FileInputNodeFailureTerminal"));
		assertTrue("FileInputNodeOutTerminal is not defined as a rule.", ruleKeys.contains("FileInputNodeOutTerminal"));
		assertTrue("FileInputNodeEODTerminal is not defined as a rule.", ruleKeys.contains("FileInputNodeEODTerminal"));
		assertTrue("FileInputNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("FileInputNodeCatchTerminal"));
		assertTrue("FileInputNodeBuildTree is not defined as a rule.", ruleKeys.contains("FileInputNodeBuildTree"));
		assertTrue("FileInputNodeRetainMixedContent is not defined as a rule.", ruleKeys.contains("FileInputNodeRetainMixedContent"));
		assertTrue("FileInputNodeRetainComments is not defined as a rule.", ruleKeys.contains("FileInputNodeRetainComments"));
		assertTrue("FileInputNodeValidation is not defined as a rule.", ruleKeys.contains("FileInputNodeValidation"));
		assertTrue("FileInputNodeMessageDomain is not defined as a rule.", ruleKeys.contains("FileInputNodeMessageDomain"));
		assertTrue("FileInputNodeMessageSet is not defined as a rule.", ruleKeys.contains("FileInputNodeMessageSet"));
		assertTrue("FileInputNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("FileInputNodeMonitoringEvents"));
		assertTrue("FileOutputNodeInTerminal is not defined as a rule.", ruleKeys.contains("FileOutputNodeInTerminal"));
		assertTrue("FileOutputNodeInEODTerminal is not defined as a rule.", ruleKeys.contains("FileOutputNodeInEODTerminal"));
		assertTrue("FileOutputNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("FileOutputNodeFailureTerminal"));
		assertTrue("FileOutputNodeOutTerminal is not defined as a rule.", ruleKeys.contains("FileOutputNodeOutTerminal"));
		assertTrue("FileOutputNodeOutEODTerminal is not defined as a rule.", ruleKeys.contains("FileOutputNodeOutEODTerminal"));
		assertTrue("FileOutputNodeValidation is not defined as a rule.", ruleKeys.contains("FileOutputNodeValidation"));
		assertTrue("FileOutputNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("FileOutputNodeMonitoringEvents"));
		assertTrue("HttpInputNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("HttpInputNodeFailureTerminal"));
		assertTrue("HttpInputNodeOutTerminal is not defined as a rule.", ruleKeys.contains("HttpInputNodeOutTerminal"));
		assertTrue("HttpInputNodeTimeoutTerminal is not defined as a rule.", ruleKeys.contains("HttpInputNodeTimeoutTerminal"));
		assertTrue("HttpInputNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("HttpInputNodeCatchTerminal"));
		assertTrue("HttpInputNodeBuildTree is not defined as a rule.", ruleKeys.contains("HttpInputNodeBuildTree"));
		assertTrue("HttpInputNodeRetainMixedContent is not defined as a rule.", ruleKeys.contains("HttpInputNodeRetainMixedContent"));
		assertTrue("HttpInputNodeRetainComments is not defined as a rule.", ruleKeys.contains("HttpInputNodeRetainComments"));
		assertTrue("HttpInputNodeValidation is not defined as a rule.", ruleKeys.contains("HttpInputNodeValidation"));
		assertTrue("HttpInputNodeMessageDomain is not defined as a rule.", ruleKeys.contains("HttpInputNodeMessageDomain"));
		assertTrue("HttpInputNodeMessageSet is not defined as a rule.", ruleKeys.contains("HttpInputNodeMessageSet"));
		assertTrue("HttpInputNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("HttpInputNodeMonitoringEvents"));
		assertTrue("HttpRequestNodeHTTPHeader is not defined as a rule.", ruleKeys.contains("HttpRequestNodeHTTPHeader"));
		assertTrue("HttpRequestNodeMessageLocationTree is not defined as a rule.", ruleKeys.contains("HttpRequestNodeMessageLocationTree"));
		assertTrue("HttpRequestNodeInTerminal is not defined as a rule.", ruleKeys.contains("HttpRequestNodeInTerminal"));
		assertTrue("HttpRequestNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("HttpRequestNodeFailureTerminal"));
		assertTrue("HttpRequestNodeOutTerminal is not defined as a rule.", ruleKeys.contains("HttpRequestNodeOutTerminal"));
		assertTrue("HttpRequestNodeErrorTerminal is not defined as a rule.", ruleKeys.contains("HttpRequestNodeErrorTerminal"));
		assertTrue("HttpRequestNodeBuildTree is not defined as a rule.", ruleKeys.contains("HttpRequestNodeBuildTree"));
		assertTrue("HttpRequestNodeRetainMixedContent is not defined as a rule.", ruleKeys.contains("HttpRequestNodeRetainMixedContent"));
		assertTrue("HttpRequestNodeRetainComments is not defined as a rule.", ruleKeys.contains("HttpRequestNodeRetainComments"));
		assertTrue("HttpRequestNodeValidation is not defined as a rule.", ruleKeys.contains("HttpRequestNodeValidation"));
		assertTrue("HttpRequestNodeMessageDomain is not defined as a rule.", ruleKeys.contains("HttpRequestNodeMessageDomain"));
		assertTrue("HttpRequestNodeMessageSet is not defined as a rule.", ruleKeys.contains("HttpRequestNodeMessageSet"));
		assertTrue("HttpRequestNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("HttpRequestNodeMonitoringEvents"));
		assertTrue("MQInputNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("MQInputNodeFailureTerminal"));
		assertTrue("MQInputNodeOutTerminal is not defined as a rule.", ruleKeys.contains("MQInputNodeOutTerminal"));
		assertTrue("MQInputNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("MQInputNodeCatchTerminal"));
		assertTrue("MQInputNodeBuildTree is not defined as a rule.", ruleKeys.contains("MQInputNodeBuildTree"));
		assertTrue("MQInputNodeRetainMixedContent is not defined as a rule.", ruleKeys.contains("MQInputNodeRetainMixedContent"));
		assertTrue("MQInputNodeRetainComments is not defined as a rule.", ruleKeys.contains("MQInputNodeRetainComments"));
		assertTrue("MQInputNodeValidation is not defined as a rule.", ruleKeys.contains("MQInputNodeValidation"));
		assertTrue("MQInputNodeMessageDomain is not defined as a rule.", ruleKeys.contains("MQInputNodeMessageDomain"));
		assertTrue("MQInputNodeMessageSet is not defined as a rule.", ruleKeys.contains("MQInputNodeMessageSet"));
		assertTrue("MQInputNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("MQInputNodeMonitoringEvents"));
		assertTrue("MQOutputNodeInTerminal is not defined as a rule.", ruleKeys.contains("MQOutputNodeInTerminal"));
		assertTrue("MQOutputNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("MQOutputNodeFailureTerminal"));
		assertTrue("MQOutputNodeOutTerminal is not defined as a rule.", ruleKeys.contains("MQOutputNodeOutTerminal"));
		assertTrue("MQOutputNodeValidation is not defined as a rule.", ruleKeys.contains("MQOutputNodeValidation"));
		assertTrue("MQOutputNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("MQOutputNodeMonitoringEvents"));
		assertTrue("ResetContentDescriptorNodeMessageDomain is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeMessageDomain"));
		assertTrue("ResetContentDescriptorNodeMessageSet is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeMessageSet"));
		assertTrue("ResetContentDescriptorNodeResetMessageDomain is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeResetMessageDomain"));
		assertTrue("ResetContentDescriptorNodeResetMessageSet is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeResetMessageSet"));
		assertTrue("ResetContentDescriptorNodeResetMessageType is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeResetMessageType"));
		assertTrue("ResetContentDescriptorNodeResetMessageFormat is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeResetMessageFormat"));
		assertTrue("ResetContentDescriptorNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("ResetContentDescriptorNodeMonitoringEvents"));
		assertTrue("SoapInputNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("SoapInputNodeFailureTerminal"));
		assertTrue("SoapInputNodeOutTerminal is not defined as a rule.", ruleKeys.contains("SoapInputNodeOutTerminal"));
		assertTrue("SoapInputNodeTimeoutTerminal is not defined as a rule.", ruleKeys.contains("SoapInputNodeTimeoutTerminal"));
		assertTrue("SoapInputNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("SoapInputNodeCatchTerminal"));
		assertTrue("SoapInputNodeBuildTree is not defined as a rule.", ruleKeys.contains("SoapInputNodeBuildTree"));
		assertTrue("SoapInputNodeValidation is not defined as a rule.", ruleKeys.contains("SoapInputNodeValidation"));
		assertTrue("SoapInputNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("SoapInputNodeMonitoringEvents"));
		assertTrue("SoapRequestNodeInTerminal is not defined as a rule.", ruleKeys.contains("SoapRequestNodeInTerminal"));
		assertTrue("SoapRequestNodeOutTerminal is not defined as a rule.", ruleKeys.contains("SoapRequestNodeOutTerminal"));
		assertTrue("SoapRequestNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("SoapRequestNodeFailureTerminal"));
		assertTrue("SoapRequestNodeFaultTerminal is not defined as a rule.", ruleKeys.contains("SoapRequestNodeFaultTerminal"));
		assertTrue("SoapRequestNodeBuildTree is not defined as a rule.", ruleKeys.contains("SoapRequestNodeBuildTree"));
		assertTrue("SoapRequestNodeValidation is not defined as a rule.", ruleKeys.contains("SoapRequestNodeValidation"));
		assertTrue("SoapRequestNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("SoapRequestNodeMonitoringEvents"));
		assertTrue("TimeoutControlNodeInTerminal is not defined as a rule.", ruleKeys.contains("TimeoutControlNodeInTerminal"));
		assertTrue("TimeoutControlNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("TimeoutControlNodeFailureTerminal"));
		assertTrue("TimeoutControlNodeOutTerminal is not defined as a rule.", ruleKeys.contains("TimeoutControlNodeOutTerminal"));
		assertTrue("TimeoutControlNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("TimeoutControlNodeMonitoringEvents"));
		assertTrue("TimeoutNotificationNodeFailureTerminal is not defined as a rule.", ruleKeys.contains("TimeoutNotificationNodeFailureTerminal"));
		assertTrue("TimeoutNotificationNodeOutTerminal is not defined as a rule.", ruleKeys.contains("TimeoutNotificationNodeOutTerminal"));
		assertTrue("TimeoutNotificationNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("TimeoutNotificationNodeCatchTerminal"));
		assertTrue("TimeoutNotificationNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("TimeoutNotificationNodeMonitoringEvents"));
		assertTrue("TryCatchNodeTryCatch is not defined as a rule.", ruleKeys.contains("TryCatchNodeTryCatch"));
		assertTrue("TryCatchNodeInTerminal is not defined as a rule.", ruleKeys.contains("TryCatchNodeInTerminal"));
		assertTrue("TryCatchNodeTryTerminal is not defined as a rule.", ruleKeys.contains("TryCatchNodeTryTerminal"));
		assertTrue("TryCatchNodeCatchTerminal is not defined as a rule.", ruleKeys.contains("TryCatchNodeCatchTerminal"));
		assertTrue("TryCatchNodeMonitoringEvents is not defined as a rule.", ruleKeys.contains("TryCatchNodeMonitoringEvents"));
	}

}
