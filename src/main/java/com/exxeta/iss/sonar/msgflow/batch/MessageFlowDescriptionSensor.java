package com.exxeta.iss.sonar.msgflow.batch;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the description of the
 * message flow 
 * 
 * @author Arjav Shah
 */
public class MessageFlowDescriptionSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(ComputeNodeSensor.class);

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			String fileName = inputFile.absolutePath();
			if(!fileName.endsWith(".map")){
				MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(fileName);
	
				if ((msgFlow.getShortDescription()==null)||(msgFlow.getShortDescription().toString().isEmpty()) && ((msgFlow.getLongDescription()==null)||(msgFlow.getLongDescription().toString().isEmpty()))) {
					createNewIssue(context, inputFile, "MessageFlowDescription",
							"Description for the message flow '" + inputFile.relativePath()
							+ "' is not present. Always mention flow description inside the message flow.");
				}
			}
		}
	}
}