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

import org.sonar.api.Plugin;

import com.exxeta.iss.sonar.msgflow.batch.AggregateControlSensor;
import com.exxeta.iss.sonar.msgflow.batch.CollectorNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.ComputeNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.DSNSensor;
import com.exxeta.iss.sonar.msgflow.batch.DatabaseNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.FileInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.FileOutputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.FilterNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.HttpInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.HttpReplyNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.HttpRequestNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.IMSRequestNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.LabelNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQGetNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQHeaderNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQOutputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MQReplyNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.MessageFlowConnectionSensor;
import com.exxeta.iss.sonar.msgflow.batch.MessageFlowDescriptionSensor;
import com.exxeta.iss.sonar.msgflow.batch.MessageFlowGenericSensor;
import com.exxeta.iss.sonar.msgflow.batch.MessageMapSensor;
import com.exxeta.iss.sonar.msgflow.batch.MiscellaneousNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.NamingConventionSensor;
import com.exxeta.iss.sonar.msgflow.batch.ResetContentDescriptorNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.RouteNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.SoapInputNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.SoapRequestNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TimeoutControlNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TimeoutNotificationNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TraceNodeSensor;
import com.exxeta.iss.sonar.msgflow.batch.TryCatchNodeSensor;

/**
 * The class is the entry point of the plug-in.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowPlugin implements Plugin {

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
	public static final String[] FILE_SUFFIXES_DEFAULTVALUE = new String[]{".msgflow",".subflow",".map"};
	
	public static final String [] FLOW_PATH_PATTERNS = {"**/*.msgflow","**/*.subflow"};
	public static final String [] MAP_PATH_PATTERNS = {"**/*.map"};
	public static final String [] ESQL_PATH_PATTERNS = {"**/*.esql"};
	public static final String [] POM_PATH_PATTERNS = {"**/*pom.xml"};

	@Override
	public void define(Context context) {
		context.addExtensions(getExtensions());
	}
	
	/**
	 * The method contains all classes necessary for the plug-in.
	 * 
	 * @return a list of classes necessary for the plug-in
	 */
	public List<Class<? extends Object>> getExtensions() {
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
			HttpReplyNodeSensor.class, // class extends Sensor
			MQInputNodeSensor.class, // class extends Sensor
			MQOutputNodeSensor.class, // class extends Sensor
			MQGetNodeSensor.class, // class extends Sensor
			MQHeaderNodeSensor.class, // class extends Sensor
			MQReplyNodeSensor.class, // class extends Sensor
			ResetContentDescriptorNodeSensor.class, // class extends Sensor
			SoapInputNodeSensor.class, // class extends Sensor
			SoapRequestNodeSensor.class, // class extends Sensor
			TimeoutControlNodeSensor.class, // class extends Sensor
			TimeoutNotificationNodeSensor.class, // class extends Sensor
			TryCatchNodeSensor.class,		// class extends Sensor
			MessageFlowConnectionSensor.class,		// class extends Sensor 
			IMSRequestNodeSensor.class,// class extends Sensor 
			MessageFlowDescriptionSensor.class, // class extends Sensor 
			FilterNodeSensor.class, //class extends Sensor 
			TraceNodeSensor.class, // class extends Sensor
			MiscellaneousNodeSensor.class, // class extends Sensor
			LabelNodeSensor.class, // class extends Sensor
			MessageFlowGenericSensor.class, // class extends Sensor
			AggregateControlSensor.class, // class extends Sensor
			DatabaseNodeSensor.class, // class extends Sensor
			RouteNodeSensor.class, // class extends Sensor
			MessageMapSensor.class, // class extends Sensor
			DSNSensor.class, //class extends Sensor
			NamingConventionSensor.class //class extends Sensor
			// UI
			// ...
		);
	}

}
