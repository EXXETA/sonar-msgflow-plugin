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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

/**
 * The class defines the language of the plug-in. In this case the language 
 * for message flows (msgflow) is defined in the current class.
 * 
 * @author Hendrik Scholz (EXXETA AG)
 */
public class MessageFlowLanguage extends AbstractLanguage {

	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MessageFlowLanguage.class);
	
	/**
	 * The KEY of the language.
	 */
	public static final String KEY = "msgflow";
	
	/**
	 * The NAME of the language.
	 */
	public static final String NAME = "Message Flow";
	
	/**
	 * Variable to hold the configuration settings.
	 */
	private  Configuration configuration;
	
	/**
	 * Construtor
	 * 
	 * @param configuration The configuration settings set by IoC.
	 */
	public MessageFlowLanguage(Configuration configuration) {
		super(KEY, NAME);
		this.configuration = configuration;
	}
	
	/* (non-Javadoc)
	 * @see org.sonar.api.resources.Language#getFileSuffixes()
	 */
	/**
	 * The method returns a list of file suffixes for the defined language.
	 * 
	 * @return a list of file suffixes for the defined language
	 */
	@Override
	public String[] getFileSuffixes() {
		String[] suffixes = configuration.getStringArray(MessageFlowPlugin.FILE_SUFFIXES_KEY);
		if (suffixes == null || suffixes.length == 0) {
			suffixes = MessageFlowPlugin.FILE_SUFFIXES_DEFAULTVALUE;
		}
		
		LOG.debug(suffixes.toString());
		
		return suffixes;
	}

}
