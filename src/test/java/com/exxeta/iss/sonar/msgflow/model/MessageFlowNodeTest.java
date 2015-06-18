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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * The class ... TODO: add comment
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowNodeTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getId()}.
	 */
	@Test
	public final void testGetId() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertEquals("FCMComposite_1_1", mf.getFileInputNodes().get(0).getId());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getName()}.
	 */
	@Test
	public final void testGetName() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertEquals("File Input", mf.getFileInputNodes().get(0).getName());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getType()}.
	 */
	@Test
	public final void testGetType() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertEquals("FileInput", mf.getFileInputNodes().get(0).getType());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isBuildTreeUsingSchema()}.
	 */
	@Test
	public final void testIsBuildTreeUsingSchema() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertTrue(mf.getFileInputNodes().get(0).isBuildTreeUsingSchema());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isMixedContentRetainMode()}.
	 */
	@Test
	public final void testIsMixedContentRetainMode() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertTrue(mf.getFileInputNodes().get(0).isMixedContentRetainMode());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isCommentsRetainMode()}.
	 */
	@Test
	public final void testIsCommentsRetainMode() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertTrue(mf.getFileInputNodes().get(0).isCommentsRetainMode());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isValidateMaster()}.
	 */
	@Test
	public final void testIsValidateMaster() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertTrue(mf.getFileInputNodes().get(0).isValidateMaster());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getMessageDomainProperty()}.
	 */
	@Test
	public final void testGetMessageDomainProperty() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertEquals("XMLNSC", mf.getFileInputNodes().get(0).getMessageDomainProperty());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getMessageSetProperty()}.
	 */
	@Test
	public final void testGetMessageSetProperty() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\FileInput.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getFileInputNodes().size());
		assertEquals("TestHttp", mf.getFileInputNodes().get(0).getMessageSetProperty());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getRequestMsgLocationInTree()}.
	 */
	@Test
	public final void testGetRequestMsgLocationInTree() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\HttpRequest.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getHttpRequestNodes().size());
		assertEquals("InputRoot.XMLNSC", mf.getHttpRequestNodes().get(0).getRequestMsgLocationInTree());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getMessageDomain()}.
	 */
	@Test
	public final void testGetMessageDomain() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		assertEquals("XMLNSC", mf.getResetContentDescriptorNodes().get(0).getMessageDomain());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getMessageSet()}.
	 */
	@Test
	public final void testGetMessageSet() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		assertEquals("TestHttp", mf.getResetContentDescriptorNodes().get(0).getMessageSet());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isResetMessageDomain()}.
	 */
	@Test
	public final void testIsResetMessageDomain() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		assertTrue(mf.getResetContentDescriptorNodes().get(0).isResetMessageDomain());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isResetMessageSet()}.
	 */
	@Test
	public final void testIsResetMessageSet() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		assertTrue(mf.getResetContentDescriptorNodes().get(0).isResetMessageSet());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isResetMessageType()}.
	 */
	@Test
	public final void testIsResetMessageType() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		assertTrue(mf.getResetContentDescriptorNodes().get(0).isResetMessageType());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#isResetMessageFormat()}.
	 */
	@Test
	public final void testIsResetMessageFormat() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\ResetContentDescriptor.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getResetContentDescriptorNodes().size());
		assertTrue(mf.getResetContentDescriptorNodes().get(0).isResetMessageFormat());
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getInputTerminals()}.
	 */
	@Test
	public final void testGetInputTerminals() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\Collector.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getCollectorNodes().size());
		
		assertTrue("No InTerminal.control found.", mf.getCollectorNodes().get(0).getInputTerminals().contains("InTerminal.control"));
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowNode#getOutputTerminals()}.
	 */
	@Test
	public final void testGetOutputTerminals() {
		MessageFlow mf = new MessageFlow("src\\test\\resources\\Collector.msgflow", new MessageFlowParser());
		assertEquals(1, mf.getCollectorNodes().size());
		
		assertTrue("No OutTerminal.failure found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.failure"));
		assertTrue("No OutTerminal.out found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.out"));
		assertTrue("No OutTerminal.expire found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.expire"));
		assertTrue("No OutTerminal.catch found.", mf.getCollectorNodes().get(0).getOutputTerminals().contains("OutTerminal.catch"));
	}

}
