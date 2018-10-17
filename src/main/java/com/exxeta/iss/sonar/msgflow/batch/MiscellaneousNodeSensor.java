package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowNodeWithInputTerminals;
import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;
import com.google.common.collect.ImmutableSet;

/**
 * The class (sensor) contains the method to analyse the connections and 
 * configuration of a Miscellaneous/Uncategorized Node.
 * 
 * @author Arjav Shah
 */
public class MiscellaneousNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(TraceNodeSensor.class);
	
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMiscellaneousNodes().iterator();
			
			String [] nodesWithMultipleInputs = {"CDOutput","FileRead","FTEOutput","TCPIPServerOutput","AggregateReply"};
			
			String [] deprecatedNodes = {"Check"};
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(IsNodeWithInputTerminals(msgFlowNode.getType()) && msgFlowNode.getInputTerminals().size()==0){
					createNewIssue(context, inputFile, "DisconnectedNode",
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				
				if(Arrays.asList(nodesWithMultipleInputs).contains(msgFlowNode.getType()) && msgFlowNode.getInputTerminals().size()<2){
					createNewIssue(context, inputFile, "AllInputTerminalsNotConnected",
							"One or more input terminals of node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") are not connected.");
				}
				
				if(msgFlowNode.getType().equals("SOAPAsyncResponse") && !msgFlowNode.getOutputTerminals().contains("OutTerminal.fault")){
					createNewIssue(context, inputFile, "SOAPAsyncNodeFault",
							"In SOAP Async node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") 'fault' terminal is not connected.");
				}
				
				if(Arrays.asList(deprecatedNodes).contains(msgFlowNode.getType())){
					createNewIssue(context, inputFile, "DeprecatedNodeCheck",
							"Deprecated node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is used in the message flow.");
				}
			}
		}
	}
	
	public static boolean IsNodeWithInputTerminals(String type){
		Set<String> nodeTypes = ImmutableSet.copyOf(MessageFlowNodeWithInputTerminals.keywordValues());
		if (nodeTypes.contains(type)) {
			return true;
		}else{
			return false;
		}
	}
}