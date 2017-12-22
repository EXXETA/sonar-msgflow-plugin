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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.exxeta.iss.sonar.msgflow.batch.CollectorNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.ComputeNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.FileInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.FileOutputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.HttpInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.HttpRequestNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQOutputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.ResetContentDescriptorNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.SoapInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.SoapRequestNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TimeoutControlNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TimeoutNotificationNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TryCatchNodeSensor;

/**
 * The class ... TODO: add comment
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowPluginTest {

	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.MessageFlowPlugin#getExtensions()}.
	 */
	@Test
	public final void testGetExtensions() {
		MessageFlowPlugin mfp = new MessageFlowPlugin();
		assertFalse("The list of extensions is empty.", mfp.getExtensions().isEmpty());
		
		/* check whether list of sensors is complete */
		assertTrue("CollectorNodeSensor has not been imported.", mfp.getExtensions().contains(CollectorNodeSensor.class));
		assertTrue("ComputeNodeSensor has not been imported.", mfp.getExtensions().contains(ComputeNodeSensor.class));
		assertTrue("FileInputNodeSensor has not been imported.", mfp.getExtensions().contains(FileInputNodeSensor.class));
		assertTrue("FileOutputNodeSensor has not been imported.", mfp.getExtensions().contains(FileOutputNodeSensor.class));
		assertTrue("HttpInputNodeSensor has not been imported.", mfp.getExtensions().contains(HttpInputNodeSensor.class));
		assertTrue("HttpRequestNodeSensor has not been imported.", mfp.getExtensions().contains(HttpRequestNodeSensor.class));
		assertTrue("MQInputNodeSensor has not been imported.", mfp.getExtensions().contains(MQInputNodeSensor.class));
		assertTrue("MQOutputNodeSensor has not been imported.", mfp.getExtensions().contains(MQOutputNodeSensor.class));
		assertTrue("ResetContentDescriptorNodeSensor has not been imported.", mfp.getExtensions().contains(ResetContentDescriptorNodeSensor.class));
		assertTrue("SoapInputNodeSensor has not been imported.", mfp.getExtensions().contains(SoapInputNodeSensor.class));
		assertTrue("SoapRequestNodeSensor has not been imported.", mfp.getExtensions().contains(SoapRequestNodeSensor.class));
		assertTrue("TimeoutControlNodeSensor has not been imported.", mfp.getExtensions().contains(TimeoutControlNodeSensor.class));
		assertTrue("TimeoutNotificationNodeSensor has not been imported.", mfp.getExtensions().contains(TimeoutNotificationNodeSensor.class));
		assertTrue("TryCatchNodeSensor has not been imported.", mfp.getExtensions().contains(TryCatchNodeSensor.class));
	}

}
