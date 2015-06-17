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

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * The class defines the quality profile of type message flow.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public final class MessageFlowCheckList {
	
	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MessageFlowCheckList.class);
	
	/**
	 * The REPOSITORY_KEY of the Sonar repository.
	 */
	public static final String REPOSITORY_KEY = "msgflow";
	
	/**
	 * The REPOSITORY_NAME of the Sonar repository.
	 */
	public static final String REPOSITORY_NAME = "SonarQube";
	
	/**
	 * The SONAR_WAY_PROFILE of the Sonar repository.
	 */
	public static final String SONAR_WAY_PROFILE = "Message Flow";
	
	/**
	 * Constructor
	 * 
	 * Direct instantiation is not possible.
	 */
	private MessageFlowCheckList() {
	}
	
	/**
	 * The methods returns a list of checks.
	 * 
	 * There are currently no checks implemented.
	 * 
	 * @return a list of checks
	 */
	public static List<Class> getChecks() {
		return ImmutableList.<Class>of(
				// add checks if necessary
				);
	}
}
