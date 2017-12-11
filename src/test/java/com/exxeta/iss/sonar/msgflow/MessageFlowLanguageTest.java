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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sonar.api.config.Settings;

/**
 * The class ... TODO: add comment
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowLanguageTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.MessageFlowLanguage#getFileSuffixes()}.
	 */
	@Test
	public final void testGetFileSuffixes() {
		MessageFlowLanguage mfl = new MessageFlowLanguage(new Settings());
		assertEquals(3, mfl.getFileSuffixes().length);
		assertEquals(".msgflow", mfl.getFileSuffixes()[0]);
		assertEquals(".subflow", mfl.getFileSuffixes()[1]);
		assertEquals(".map", mfl.getFileSuffixes()[2]);
//		assertEquals(".esql", mfl.getFileSuffixes()[3]);
	}

}
