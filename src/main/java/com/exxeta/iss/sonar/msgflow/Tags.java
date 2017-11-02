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

/**
 * The class defines the tags for a rule.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public final class Tags {

	/**
	 * The string for BAD_PRACTICE.
	 */
	public static final String BAD_PRACTICE = "bad-practice"; 
	
	/**
	 * The string for PERFORMANCE.
	 */
	public static final String PERFORMANCE = "performance";
	
	/**
	 * The string for SECURITY.
	 */
	public static final String SECURITY = "security";
	
	/**
	 * The string for READABILITY.
	 */
	public static final String READABILITY = "readability";
	
	/**
	 * The string for READABILITY.
	 */
	public static final String STANDARD = "standard";
	
	/**
	 * The string for READABILITY.
	 */
	public static final String PITFALL = "pitfall";
	/**
	 * Constructor
	 * 
	 * Direct instantiation is not possible.
	 */
	private Tags() {}
}
