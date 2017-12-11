/**
 * 
 */
package com.exxeta.iss.sonar.msgflow.model;

import java.util.ArrayList;

/**
 * @author Arjav Shah
 *
 */
public class PomObject {
	/**
	 * name of the artifact from POM
	 */
	private StringBuilder artifact;
	/**
	 * a list of the modules of the POM
	 */
	private ArrayList<String> modules;
	
	/**
	 * @param artifact
	 * @param modules
	 */
	public PomObject(String fileName, PomParser parser) {
		super();
		artifact = new StringBuilder("");
		modules = new ArrayList<String>();
		parser.parse(fileName, modules, artifact);
	}
	/**
	 * @return the artifact
	 */
	public StringBuilder getArtifact() {
		return artifact;
	}
	/**
	 * @return the modules
	 */
	public ArrayList<String> getModules() {
		return modules;
	}
	
}
