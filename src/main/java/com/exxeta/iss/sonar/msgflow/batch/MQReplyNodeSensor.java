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
 * configuration of a MQ Reply Node.
 * 
 * @author Arjav Shah
 */
public class MQReplyNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMqReplyNodes().iterator();

			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(!((String)msgFlowNode.getProperties().get("transactionMode")).isEmpty()
						|| msgFlowNode.getProperties().get("transactionMode").equals("yes")
						|| msgFlowNode.getProperties().get("transactionMode").equals("no")) {

					createNewIssue(context, inputFile, "MQNodeTxnMode",
							"The 'Transaction Mode' property of " + msgFlowNode.getName() + "(type:"
							+ msgFlowNode.getType() + ") node is not set to Automatic.");
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					createNewIssue(context, inputFile, "DisconnectedNode",
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
			}

			if(msgFlow.getMqReplyNodes().size()!=0 && msgFlow.getMqInputNodes().size()==0){
				createNewIssue(context, inputFile, "MQReplyWithoutMQInput",
						"The Flow contains 'MQ Reply' Node without 'MQ Input' node.");
			}
		}
	}
}