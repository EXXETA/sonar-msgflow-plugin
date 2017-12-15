package com.exxeta.iss.sonar.msgflow.batch;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;
import com.exxeta.iss.sonar.msgflow.model.PomObject;
import com.exxeta.iss.sonar.msgflow.model.PomParser;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a MQ Get Node.
 * 
 * @author Arjav Shah
 */
public class NamingConventionSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);
	
	/**
	 * Variable to hold file system information, e.g. the file names of the project files.
	 */
	private final FileSystem fs;
	
	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;
	
	/**
	  * Use of IoC to get FileSystem and ResourcePerspectives
	  */
	public NamingConventionSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.resources.Project)
	 */
	@Override
	public boolean shouldExecuteOnProject(Project arg0) {
		// This sensor is executed only when there are msgflow files
	    return fs.hasFiles(fs.predicates().hasLanguage("msgflow"));
	}

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.Sensor#analyse(org.sonar.api.resources.Project, org.sonar.api.batch.SensorContext)
	 */
	/**
	 * The method where the analysis of the connections and configuration of 
	 * the message flow node takes place.
	 */
	@Override
	public void analyse(Project arg0, SensorContext arg1) {
		
		
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			String fullPath = inputFile.absolutePath();
			
			if(fullPath.endsWith("_MF.msgflow")){
				boolean violationDetected = false;
				File file = new File(inputFile.absolutePath());
				PomObject pomObj = new PomObject(file.getParentFile().getParentFile().getAbsolutePath()+File.separator+"pom.xml", new PomParser());
				String artifactName = pomObj.getArtifact().toString();
				if(!inputFile.absolutePath().endsWith(artifactName+"_MF.msgflow")){
					violationDetected = true;
				}
				for(String module : pomObj.getModules()){
					if((module.indexOf(artifactName+"_App") != -1) || (module.indexOf(artifactName+"_Lib") != -1) 
							|| (module.indexOf(artifactName+"_Properties") != -1)){
						violationDetected = true;
					}
				}
				if(violationDetected){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    		.ruleKey(RuleKey.of("msgflow", "MavenProjectNamingConventions"))
				    		.message("The Naming conventions for the message flow and the artifacts is not followed").build());
				}
				
			}else if(fullPath.substring(fullPath.lastIndexOf(File.separator)+1, fullPath.indexOf(".msgflow")).matches("^[a-zA-Z]*(_App_v)[0-9]")){
				boolean violationDetected = false;
				File file = new File(inputFile.absolutePath());
				PomObject pomObj = new PomObject(file.getParentFile().getParentFile().getParentFile().getAbsolutePath()+File.separator+"pom.xml", new PomParser());
				String artifactName = pomObj.getArtifact().toString();
				if(!inputFile.absolutePath().contains(artifactName)){
					violationDetected = true;
				}
				for(String module : pomObj.getModules()){
					if(module.contains(artifactName) && ((module.indexOf(artifactName+"_App") != -1) || (module.indexOf(artifactName+"_Lib") != -1) 
							|| (module.indexOf(artifactName+"_DAR") != -1))){
						violationDetected = true;
					}
				}
				if(violationDetected){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    		.ruleKey(RuleKey.of("msgflow", "MavenProjectNamingConventions"))
				    		.message("The Naming conventions for the message flow and the artifacts is not followed").build());
				}
				
			}
			
		}
	}
	

}
