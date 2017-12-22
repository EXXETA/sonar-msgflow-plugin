package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Iterator;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.Mapping;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;
import com.exxeta.iss.sonar.msgflow.model.MessageMap;

/**
 * The class (sensor) contains the method to analyse the connections and
 * configuration of a AggregateControl Node.
 * 
 * @author Arjav Shah
 */
public class MessageMapSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(AggregateControlSensor.class);

	/**
	 * Variable to hold file system information, e.g. the file names of the
	 * project files.
	 */
	private final FileSystem fs;

	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;

	/**
	 * Use of IoC to get FileSystem and ResourcePerspectives
	 */
	public MessageMapSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.
	 * resources.Project)
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
		
		
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.MAP_PATH_PATTERNS))) {
			/*
			 * retrieve the message flow object
			 */
//			if (inputFile.absolutePath().endsWith(".map")) {
				MessageMap msgMap = MessageFlowProject.getInstance().getMessageMap(inputFile.absolutePath());

				// the actual rule ...
				Iterator<Mapping> mappings = msgMap.getMappings().iterator();
				int inCount = 0;
				int outCount = 0;
				while (mappings.hasNext()) {
					Mapping map = mappings.next();

					if (map.getInputPath().endsWith("/Properties") || map.getInputPath().equals("Properties")) {
						inCount++;
					}
					if (map.getOutputPath().endsWith("/Properties") || map.getOutputPath().equals("Properties")) {
						outCount++;
					}

				}
				if (inCount == 0 || outCount == 0) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(
							issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "mappingNodePropertiesMissing"))
									.message("Mapping node does not propagate properties.").build());
				}
				
				
				if(msgMap.getTodoCount().get()>0){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(
							issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "todoFoundInMapping"))
									.message("Mapping contains TODO block.").build());
				}
			}
		}
//	}
}
