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
package com.exxeta.iss.sonar.msgflow.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * The class ... TODO: add comment
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getCollectorNodes()}.
	 */
	@Test
	public final void testGetCollectorNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/Collector.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getCollectorNodes().size());
		
		assertTrue("No InTerminal.control found.", mf.getCollectorNodes().get(0).getInputTerminals().contains("InTerminal.control"));
		assertTrue("No OutTerminal.failure found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.failure"));
		assertTrue("No OutTerminal.out found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.out"));
		assertTrue("No OutTerminal.expire found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.expire"));
		assertTrue("No OutTerminal.catch found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.catch"));
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getComputeNodes()}.
	 */
	@Test
	public final void testGetComputeNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/Compute.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getComputeNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getFileInputNodes()}.
	 */
	@Test
	public final void testGetFileInputNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getFileOutputNodes()}.
	 */
	@Test
	public final void testGetFileOutputNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/FileOutput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileOutputNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getHttpInputNodes()}.
	 */
	@Test
	public final void testGetHttpInputNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/HttpInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getHttpInputNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getHttpRequestNodes()}.
	 */
	@Test
	public final void testGetHttpRequestNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/HttpRequest.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getHttpRequestNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getMqInputNodes()}.
	 */
	@Test
	public final void testGetMqInputNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/MQInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getMqInputNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getMqOutputNodes()}.
	 */
	@Test
	public final void testGetMqOutputNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/MQOutput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getMqOutputNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getResetContentDescriptorNodes()}.
	 */
	@Test
	public final void testGetResetContentDescriptorNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getSoapInputNodes()}.
	 */
	@Test
	public final void testGetSoapInputNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/SoapInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getSoapInputNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getSoapRequestNodes()}.
	 */
	@Test
	public final void testGetSoapRequestNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/SoapRequest.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getSoapRequestNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getTimeoutControlNodes()}.
	 */
	@Test
	public final void testGetTimeoutControlNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/TimeoutControl.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getTimeoutControlNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getTimeoutNotificationNodes()}.
	 */
	@Test
	public final void testGetTimeoutNotificationNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/TimeoutNotification.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getTimeoutNotificationNodes().size());
		
		/* TODO: insert additional tests here */
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlow#getTryCatchNodes()}.
	 */
	@Test
	public final void testGetTryCatchNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/TryCatch.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getTryCatchNodes().size());
		
		/* TODO: insert additional tests here */
	}
	
	@Test
	public final void testMQGetNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/MQNodes.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getMqGetNodes().size());
		assertNotEquals(mf.getMqGetNodes().get(0).getName(), mf.getMqGetNodes().get(0).getProperties().get("queueName"));
	}
	@Test
	public final void testIMSRequestNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/IMSRequest.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getImsRequestNodes().size());
		assertEquals(mf.getImsRequestNodes().get(0).getProperties().get("shortDescription"), "Short Description");
		assertEquals(mf.getImsRequestNodes().get(0).getProperties().get("longDescription"), "Long Description goes here");
		assertEquals(mf.getImsRequestNodes().get(0).getProperties().get("commitMode"), "commitThenSend");
		assertEquals(mf.getImsRequestNodes().get(0).getProperties().get("configurableService"), "XYZ");
		assertEquals(mf.getImsRequestNodes().get(0).getMessageDomainProperty(), "BLOB");
		assertEquals(mf.getImsRequestNodes().get(0).getProperties().get("useNodeProperties"), "false");
	}
	@Test
	public final void testConnections() {
		MessageFlow mf = new MessageFlow("src/test/resources/SelfConnectingNode.msgflow", new MessageFlowParser());
		assertEquals(4, mf.getConnections().size());
		/*checking Self connecting Nodes*/ 
		assertEquals(mf.getConnections().get(1).getSrcNode(), mf.getConnections().get(1).getTargetNode());
		assertNotEquals(mf.getConnections().get(2).getSrcNode(), mf.getConnections().get(2).getTargetNode());
	}
	@Test
	public final void testDescription() {
		MessageFlow mf = new MessageFlow("src/test/resources/IMSRequest.msgflow", new MessageFlowParser());
		assertEquals("Short Description", mf.getShortDescription().toString());
		assertEquals("Long Description goes here", mf.getLongDescription().toString());
		/*checking Self connecting Nodes*/ 
		
		}
	@Test
	public final void testMqHeaderNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/MessageFlowComment.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getMqHeaderNodes().size());
		}
	@Test
	public final void testFilterNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/FilterNode.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFilterNodes().size());
	}
	@Test
	public final void testMQReplyNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/MQTxnMode.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getMqReplyNodes().size());
		assertFalse(!((String)mf.getMqReplyNodes().get(0).getProperties().get("transactionMode")).isEmpty());
	}
	@Test
	public final void testHttpReplyNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/HttpReply.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getHttpReplyNodes().size());
		assertEquals("false", mf.getHttpReplyNodes().get(0).getProperties().get("generateDefaultHttpHeaders"));
		assertEquals("false", mf.getHttpReplyNodes().get(0).getProperties().get("ignoreTransportFailures"));
	}
	@Test
	public final void testTraceNodes() {
		MessageFlow mf = new MessageFlow("src/test/resources/Trace.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getTraceNodes().size());
	}
	@Test
	public final void testMiscellaneousNode(){
		MessageFlow mf = new MessageFlow("src/test/resources/MiscellaneousNode.msgflow", new MessageFlowParser());
		assertEquals(2, mf.getMiscellaneousNodes().size());
	}
	
}

