package com.exxeta.iss.sonar.msgflow.batch;

import java.io.File;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.PomObject;
import com.exxeta.iss.sonar.msgflow.model.PomParser;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a MQ Get Node.
 * 
 * @author Arjav Shah
 */
public class NamingConventionSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			String fullPath = inputFile.absolutePath();

			if (fullPath.endsWith("_MF.msgflow")) {
				boolean violationDetected = false;
				File file = new File(fullPath);
				String ProjectDirectory = getProjectDirectory(file).getAbsolutePath();
				if (new File(ProjectDirectory + File.separator + "pom.xml").exists()) {
					PomObject pomObj = new PomObject(ProjectDirectory + File.separator + "pom.xml", new PomParser());
					String artifactName = pomObj.getArtifact().toString();
					if (!fullPath.endsWith(artifactName + "_MF.msgflow")) {
						violationDetected = true;
					}
					for (String module : pomObj.getModules()) {
						if ((module.contains(artifactName) && (module.indexOf(artifactName + "_App") == -1)
								&& (module.indexOf(artifactName + "_Lib") == -1)
								&& (module.indexOf(artifactName + "_DAR") == -1))
							||(!(module.contains(artifactName)) && (module.indexOf("pipeline_config") == -1)
								&& (module.indexOf("XLD_Dictionaries") == -1))) {
							violationDetected = true;
						}
					}
					if (violationDetected) {
						createNewIssue(context, inputFile, "MavenProjectNamingConventions",
								"The Naming conventions for the message flow and the artifacts is not followed");
					}

				}
			} else if ((!fullPath.contains(".subflow")) && (fullPath.substring(fullPath.lastIndexOf(File.separator)+1, fullPath.indexOf(".msgflow")).matches("^[a-zA-Z]*(_App_v)[0-9]"))){
				
				boolean violationDetected = false;
				File file = new File(fullPath);
				String ProjectDirectory = getProjectDirectory(file).getAbsolutePath();
				if (new File(ProjectDirectory + File.separator + "pom.xml").exists()) {
					PomObject pomObj = new PomObject(ProjectDirectory + File.separator + "pom.xml",
							new PomParser());
					String artifactName = pomObj.getArtifact().toString();
					if (!fullPath.contains(artifactName)) {
						violationDetected = true;
					}
					for (String module : pomObj.getModules()) {
						if (module.contains(artifactName) && ((module.indexOf(artifactName + "_App") == -1)
								&& (module.indexOf(artifactName + "_Lib") == -1)
								&& (module.indexOf(artifactName + "_DAR") == -1))
							||(!(module.contains(artifactName)) && (module.indexOf("pipeline_config") == -1)
								&& (module.indexOf("XLD_Dictionaries") == -1))) {
							violationDetected = true;
						}
					}
					if (violationDetected) {
						createNewIssue(context, inputFile, "MavenProjectNamingConventions",
								"The Naming conventions for the message flow and the artifacts is not followed");
					}
				}
			}
		}
	}
	
	private File getProjectDirectory(File msgFlowFile) {
		File projectDirectory = new File(msgFlowFile.getAbsolutePath());
		while (projectDirectory != null) {
			projectDirectory = projectDirectory.getParentFile();
			if (new File(projectDirectory, ".project").exists()) {
				return projectDirectory.getParentFile();
			}
		}
		return null;
	}
}
