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

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;
import org.sonar.api.batch.sensor.issue.Issue;

/**
 * The test class for {@link com.exxeta.iss.sonar.msgflow.batch.DSNSensor}
 * 
 * @author Marcel de Jong
 */
public class DSNSensorTest extends AbstractSensorTest {
	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.batch.DSNSensor#execute()}.
	 */
	//@Test
	public void analysis_with_issue_should_add_error_to_context()
	{
		inputFile("Compute.msgflow");

		createSensor().execute(getContext());
		Collection<Issue> issues = getContext().allIssues();
		assertEquals(1, issues.size());
		
		Issue issue = issues.iterator().next();
		assertEquals("DSNWithoutDBCall", issue.ruleKey().rule());
		assertEquals("DSN property is set without DB interactions for 'Compute' (type: Compute).", issue.primaryLocation().message());
	}
	
	/**
	 * Test method for {@link com.exxeta.iss.sonar.msgflow.batch.DSNSensor#execute()}.
	 */
	@Test
	public void analysis_with_no_issue_should_not_add_error_to_context()
	{
		inputFile("Trace.msgflow");

		createSensor().execute(getContext());
		Collection<Issue> issues = getContext().allIssues();
		assertEquals(0, issues.size());
	}
	
	private DSNSensor createSensor() {
		return new DSNSensor();
	}
}
