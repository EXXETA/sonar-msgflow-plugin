package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Iterator;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

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
public class MessageMapSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(AggregateControlSensor.class);

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.MAP_PATH_PATTERNS))) {
			// retrieve the message flow object
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
					createNewIssue(context, inputFile, "mappingNodePropertiesMissing", "Mapping node does not propagate properties.");
				}
				
				
				if(msgMap.getTodoCount().get()>0){
					createNewIssue(context, inputFile, "todoFoundInMapping", "Mapping contains TODO block.");
				}
			}
		}
//	}
}
