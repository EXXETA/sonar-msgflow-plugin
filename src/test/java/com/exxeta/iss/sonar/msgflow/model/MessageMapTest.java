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

import org.junit.Test;

/**
 * The class tests the parser of the message map files
 * 
 * @author Arjav Shah
 */
public class MessageMapTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.model.MessageMap}.
	 */
	@Test
	public final void testMessageMaps() {
		MessageMap mp = new MessageMap("src/test/resources/Mapping.map", new MessageMapParser());
		assertEquals(1, mp.getTodoCount().get());
		assertEquals("MessageAssembly", mp.getInputVar().toString());
	}

	
}

