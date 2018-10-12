package com.exxeta.iss.sonar.msgflow.batch;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections  
 * of a message flow nodes
 * 
 * @author Arjav Shah
 */
public class MessageFlowConnectionSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(ComputeNodeSensor.class);
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			for(MessageFlowConnection con : msgFlow.getConnections()){
				if(con.getSrcNode().equalsIgnoreCase(con.getTargetNode())){
					createNewIssue(context, inputFile, "SelfConnectingNodes",
							"Self Connecting node '"+con.getSrcNodeName()+"'. Use of self Connecting node is discouraged.");
				}
				
			}
//			for(MessageFlowNode filter : msgFlow.getFilterNodes()){
//				boolean isTrueConnected = false;
//				boolean isFalseConnected = false;
//				boolean isUnknownConnected = false;
//				for(MessageFlowConnection con : msgFlow.getConnections()){
//					if(filter.getId().equals(con.getSrcNode())){
//						if(con.getSrcTerminal().equals("OutTerminal.unknown")){
//							isUnknownConnected = true;
//						} else if(con.getSrcTerminal().equals("OutTerminal.false")){
//							isFalseConnected = true;
//						} else if(con.getSrcTerminal().equals("OutTerminal.true")){
//							isTrueConnected = true;
//						}
//					}
//				}
//				if(!(isFalseConnected && isTrueConnected && isUnknownConnected)){
//					Issuable issuable = perspectives.as(Issuable.class, inputFile);
//				    issuable.addIssue(issuable.newIssueBuilder()
//				    	        	  .ruleKey(RuleKey.of("msgflow", "FilterNode"))
//				    	        	  .message("The node '"+filter.getName()+"' (type: "+filter.getType()+") has inconsistent connections.")
//				    	        	  .build());
//				}
//			}
			
		}
	}
}