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

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

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
 * The class is the entry point of the plug-in.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowPlugin extends SonarPlugin {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MessageFlowPlugin.class);
	
	/**
	 * The FILE_SUFFIXES_KEY for the plug-in.
	 */
	public static final String FILE_SUFFIXES_KEY = "sonar.msgflow.file.suffixes";
	
	/**
	 * The FILE_SUFFIXES_DEFAULTVALUE for the plug-in.
	 */
	public static final String FILE_SUFFIXES_DEFAULTVALUE = ".msgflow";
	
	/* (non-Javadoc)
	 * @see org.sonar.api.Plugin#getExtensions()
	 */
	/**
	 * The method contains all classes necessary for the plug-in.
	 * 
	 * @return a list of classes necessary for the plug-in
	 */
	@Override
	public List getExtensions() {
		return Arrays.asList(
			// Definitions
			MessageFlowLanguage.class, // class extends AbstractLanguage
			MessageFlowRulesDefinition.class, // class implements RulesDefinition
			MessageFlowProfile.class, // class extends ProfileDefinition
			//MessageFlowMetrics.class, ???
				
			// Batch
			CollectorNodeSensor.class, // class extends Sensor
			ComputeNodeSensor.class, // class extends Sensor
			FileInputNodeSensor.class, // class extends Sensor
			FileOutputNodeSensor.class, // class extends Sensor
			HttpInputNodeSensor.class, // class extends Sensor
			HttpRequestNodeSensor.class, // class extends Sensor
			MQInputNodeSensor.class, // class extends Sensor
			MQOutputNodeSensor.class, // class extends Sensor
			ResetContentDescriptorNodeSensor.class, // class extends Sensor
			SoapInputNodeSensor.class, // class extends Sensor
			SoapRequestNodeSensor.class, // class extends Sensor
			TimeoutControlNodeSensor.class, // class extends Sensor
			TimeoutNotificationNodeSensor.class, // class extends Sensor
			TryCatchNodeSensor.class // class extends Sensor

			// UI
			// ...
		);
	}

}
