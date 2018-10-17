package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Iterator;
import java.util.regex.Pattern;

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
 * configuration of a MQ Get Node.
 * 
 * @author Arjav Shah
 */
public class MQGetNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);
	
	public final static String PATTERN_STRING = "^[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.(AI|AO)$";

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMqGetNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(!((String)msgFlowNode.getProperties().get("queueName")).equals(msgFlowNode.getName())){
					createNewIssue(context, inputFile, "MQNodeNameMatchesQueueName",
							"The name of MQ Node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") does not match the underlaying queue name.");
				}
				
				if(! msgFlowNode.getProperties().get("transactionMode").equals("automatic")) {
					createNewIssue(context, inputFile, "MQNodeTxnMode",
							"The 'Transaction Mode' property of " + msgFlowNode.getName() + "(type:"
							+ msgFlowNode.getType() + ") node is not set to Automatic.");

				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("XMLNS")) {
					createNewIssue(context, inputFile, "XMLNSCoverXMLNS",
							"'Message domain' under 'Input Message Parsing' for '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") is set as XMLNS. XMLNSC is preferred over XMLNS.");
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					createNewIssue(context, inputFile, "DisconnectedNode",
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				if (!checkMQQueueName((String) msgFlowNode.getProperties().get("queueName"))) {
					createNewIssue(context, inputFile, "queueNamingConvention",
							"Naming convention for the queue specified on '"
							+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
							+ ") is not correct.");
				}
			}
		}
	}
	
	public static boolean checkMQQueueName(String name) {
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
	}
}