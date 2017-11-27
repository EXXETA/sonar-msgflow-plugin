package com.exxeta.iss.sonar.msgflow.model;
/**
 * The class is a model of a message map
 * 
 * @author Arjav Shah
 */
public class Mapping {
	/**
	 * a input path of the mapping
	 */
	private String inputPath;
	/**
	 * a output path of the mapping
	 */
	private String outputPath;
	
	/**
	 * @param inputPath
	 * @param outputPath
	 */
	public Mapping(String inputPath, String outputPath) {
		super();
		this.inputPath = inputPath;
		this.outputPath = outputPath;
	}
	/**
	 * returns xpath of the input message involved in the mapping
	 * 
	 * @return the inputPath
	 */
	public String getInputPath() {
		return inputPath;
	}
	/**
	 * returns xpath of the output message involved in the mapping
	 * 
	 * @return the outputPath
	 */
	public String getOutputPath() {
		return outputPath;
	}
	
	
	
}
