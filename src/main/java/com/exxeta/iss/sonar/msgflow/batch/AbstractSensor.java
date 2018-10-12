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
package com.exxeta.iss.sonar.msgflow.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.MessageFlowLanguage;

/**
 * The class contains methods which all Sensors share.
 * 
 * @author Marcel de Jong
 */
public abstract class AbstractSensor {
	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(AbstractSensor.class);

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#describe(org.sonar.api.batch.sensor.SensorDescriptor)
	 */
	public void describe(SensorDescriptor descriptor) {
		descriptor
			.onlyOnLanguage(MessageFlowLanguage.KEY)
			.name("Message Flow Sensor")
			.onlyOnFileType(Type.MAIN);
	}
	
	/**
	 * Creates a new issue.
	 *
	 * @param context the context
	 * @param inputFile the input file
	 * @param rule the rule
	 * @param message the message
	 */
	protected void createNewIssue(SensorContext context, InputFile inputFile, String rule, String message) {
		NewIssue newIssue = context.newIssue().forRule(RuleKey.of("msgflow", rule));
		NewIssueLocation primaryLocation = newIssue.newLocation()
				.on(inputFile)
				.message(message);

		newIssue.at(primaryLocation);
		newIssue.save();
	}
	
	/**
	 * Creates a new issue.
	 *
	 * @param context the context
	 * @param inputFile the input file
	 * @param rule the rule
	 * @param message the message
	 * @param line the line number
	 */
	protected void createNewIssue(SensorContext context, InputFile inputFile, String rule, String message, int line) {
		NewIssue newIssue = context.newIssue().forRule(RuleKey.of("msgflow", rule));
		NewIssueLocation primaryLocation = newIssue.newLocation()
				.on(inputFile)
				.message(message)
				.at(inputFile.selectLine(line));

		newIssue.at(primaryLocation);
		newIssue.save();
	}
}
