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
public class MessageFlowProjectTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowProject#getInstance()}.
	 */
	@Test
	public final void testGetInstance() {
		assertNotNull(MessageFlowProject.getInstance());
		assertTrue(MessageFlowProject.getInstance() instanceof MessageFlowProject);
	}

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageFlowProject#getMessageFlow(java.lang.String)}.
	 */
	@Test
	public final void testGetMessageFlow() {
		MessageFlowProject mfp = MessageFlowProject.getInstance();
		
		long start = System.nanoTime();
		MessageFlow mf = mfp.getMessageFlow("src/test/resources/Collector.msgflow");
		long stop = System.nanoTime();
		long diff1 = stop - start;
		
		start = System.nanoTime();
		mf = mfp.getMessageFlow("src/test/resources/Collector.msgflow");
		stop = System.nanoTime();
		long diff2 = stop - start;
		
		assertEquals(1, mf.getCollectorNodes().size());
		
		/* loading the message flow object the second time should be faster */
		assertTrue(diff1 > diff2);
	}

}
