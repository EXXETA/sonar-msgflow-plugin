package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.MessageFlowNodeWithInputTerminals;
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
public class MiscellaneousNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(TraceNodeSensor.class);
	
	/**
	 * Variable to hold file system information, e.g. the file names of the project files.
	 */
	private final FileSystem fs;
	
	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;
	
	/**
	  * Use of IoC to get FileSystem and ResourcePerspectives
	  */
	public MiscellaneousNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.resources.Project)
	 */
	@Override
	public boolean shouldExecuteOnProject(Project arg0) {
		// This sensor is executed only when there are msgflow files
	    return fs.hasFiles(fs.predicates().hasLanguage("msgflow"));
	}

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.Sensor#analyse(org.sonar.api.resources.Project, org.sonar.api.batch.SensorContext)
	 */
	/**
	 * The method where the analysis of the connections and configuration of 
	 * the message flow node takes place.
	 */
	@Override
	public void analyse(Project arg0, SensorContext arg1) {
		for (InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage("msgflow"))) {
			
			/* 
			 * retrieve the message flow object
			 */
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMiscellaneousNodes().iterator();
			
			String [] nodesWithMultipleInputs = {"CDOutput","FileRead","FTEOutput","TCPIPServerOutput"};
			
			String [] deprecatedNodes = {"Check"};
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(IsNodeWithInputTerminals(msgFlowNode.getType()) && msgFlowNode.getInputTerminals().size()==0){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "DisconnectedNode"))
				    	        	  .message("There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				
				if(Arrays.asList(nodesWithMultipleInputs).contains(msgFlowNode.getType()) && msgFlowNode.getInputTerminals().size()<2){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "AllInputTerminalsNotConnected"))
				    	        	  .message("One or more input terminals of node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") are not connected.")
				    	        	  .build());
				}
				
				if(msgFlowNode.getType().equals("SOAPAsyncResponse") && !msgFlowNode.getOutputTerminals().contains("OutTerminal.fault")){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "SOAPAsyncNodeFault"))
				    	        	  .message("In SOAP Async node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") 'fault' terminal is not connected.")
				    	        	  .build());
				}
				
				if(Arrays.asList(deprecatedNodes).contains(msgFlowNode.getType())){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "DeprecatedNodeCheck"))
				    	        	  .message("Deprecated node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is used in the message flow.")
				    	        	  .build());
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
