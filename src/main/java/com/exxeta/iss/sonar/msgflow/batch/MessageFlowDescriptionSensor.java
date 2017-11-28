package com.exxeta.iss.sonar.msgflow.batch;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the description of the
 * message flow 
 * 
 * @author Arjav Shah
 */
public class MessageFlowDescriptionSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(ComputeNodeSensor.class);

	/**
	 * Variable to hold file system information, e.g. the file names of the project
	 * files.
	 */
	private final FileSystem fs;

	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;

	/**
	 * Use of IoC to get FileSystem and ResourcePerspectives
	 */
	public MessageFlowDescriptionSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.
	 * resources.Project)
	 */
	/**
	 * The method defines the language of the file to be analysed.
	 */
	@Override
	public boolean shouldExecuteOnProject(Project arg0) {
		// This sensor is executed only when there are msgflow files
		return fs.hasFiles(fs.predicates().hasLanguage("msgflow"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sonar.api.batch.Sensor#analyse(org.sonar.api.resources.Project,
	 * org.sonar.api.batch.SensorContext)
	 */
	/**
	 * The method where the analysis of the connections and configuration of the
	 * message flow node takes place.
	 */
	@Override
	public void analyse(Project arg0, SensorContext arg1) {
		for (InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage("msgflow"))) {

			/*
			 * retrieve the message flow object
			 */
			if(!inputFile.absolutePath().endsWith(".map")){
				MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
	
				if ((msgFlow.getShortDescription()==null)||(msgFlow.getShortDescription().toString().isEmpty()) && ((msgFlow.getLongDescription()==null)||(msgFlow.getLongDescription().toString().isEmpty()))) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "MessageFlowDescription"))
							.message("Description for the message flow '" + inputFile.relativePath()
									+ "' is not present. Always mention flow description inside the message flow.")
							.build());
				}
			}
		}
	}
}