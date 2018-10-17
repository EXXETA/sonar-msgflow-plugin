package com.exxeta.iss.sonar.msgflow.batch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyze the configuration of a Database Node.
 * 
 * @author Arjav Shah
 */
public class DatabaseNodeSensor extends AbstractSensor implements Sensor {

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
			
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getDatabaseNodes().iterator();
			ArrayList<String> moduleNameExpressionList = new ArrayList<String>();
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				moduleNameExpressionList.add((String)msgFlowNode.getProperties().get("statement"));
				if(!msgFlowNode.getName().equals(msgFlowNode.getProperties().get("statement"))){
					createNewIssue(context, inputFile, "NodeNameModuleName", 
							"The node name and the underlaying module name should match for '" 
									+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				
			}

			Set<String> moduleSet = new HashSet<String>();
			for(String moduleName : moduleNameExpressionList){
				if(!moduleSet.add(moduleName)){
					createNewIssue(context, inputFile, "OneModuleMultipleNodes", 
							"Multiple Database nodes refers to same module '" + moduleName + "'.");
				}
			}
		}
	}
}
