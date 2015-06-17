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

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

/**
 * The class defines the metrics of the plug-in.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowMetrics implements Metrics {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MessageFlowMetrics.class);
	
	/**
	 * Defines the metric for the plug-in.
	 */
	public static final Metric MSGFLOW =
			new Metric.Builder(
		        "msgflow",					// metric identifier
		        "Message Flow", 			// metric name
		        Metric.ValueType.STRING)	// metric data type
		    .setDescription("Message Flow Project")
		    .setQualitative(false)
		    .setDomain(CoreMetrics.DOMAIN_GENERAL)
		    .create();
	
	/* (non-Javadoc)
	 * @see org.sonar.api.measures.Metrics#getMetrics()
	 */
	/**
	 * The method returns a list of defined metrics.
	 * 
	 * @return a list of defined metrics
	 */
	@Override
	public List<Metric> getMetrics() {
		return Arrays.asList(MSGFLOW);
	}

}
