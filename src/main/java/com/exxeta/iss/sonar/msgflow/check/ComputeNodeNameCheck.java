package com.exxeta.iss.sonar.msgflow.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
@Rule(key = ComputeNodeNameCheck.CHECK_KEY)
public class ComputeNodeNameCheck {
	public static final String CHECK_KEY = "ComputeNodeNameCheck";
		
		private static final String DEFAULT_FORMAT = "[A-Z][a-zA-Z0-9]*$";
		
		@RuleProperty(key = "format", 
				description="regular expression",
				defaultValue = "" + DEFAULT_FORMAT)
		public String format = DEFAULT_FORMAT;
	}