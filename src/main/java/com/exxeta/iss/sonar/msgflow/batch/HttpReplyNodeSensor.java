package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Iterator;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections and
 * configuration of a HTTP Reply Node.
 * 
 * @author Arjav Shah
 */
public class HttpReplyNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(HttpReplyNodeSensor.class);

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getHttpReplyNodes().iterator();

			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(!((String)msgFlowNode.getProperties().get("ignoreTransportFailures")).isEmpty()) {
					createNewIssue(context, inputFile, "HttpReplyIgnoreTransportFailuresCheck",
							"The 'Ignore transport failures' property of " + msgFlowNode.getName() + "(type:"
									+ msgFlowNode.getType() + ") node is not checked.");
				}
				
				if(!((String)msgFlowNode.getProperties().get("generateDefaultHttpHeaders")).isEmpty()) {
					createNewIssue(context, inputFile, "HttpReplyGenerateDefaultHttpHeadersCheck",
							"The 'Generate default HTTP headers from reply or response' property of " + msgFlowNode.getName() + "(type:"
									+ msgFlowNode.getType() + ") node is not checked.");
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					createNewIssue(context, inputFile, "DisconnectedNode",
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
			}
		}
	}
}
