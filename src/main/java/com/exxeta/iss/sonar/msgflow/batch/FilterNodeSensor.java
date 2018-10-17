package com.exxeta.iss.sonar.msgflow.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
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
 * configuration of a Filter Node.
 * 
 * @author Arjav Shah
 */
public class FilterNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(MQOutputNodeSensor.class);

	public final static String PATTERN_STRING = "(Is|Has|Can|TrueIf|FalseIf)[A-Z][a-zA-Z0-9]*$";

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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getFilterNodes().iterator();
			ArrayList<String> moduleNameExpressionList = new ArrayList<String>();
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				moduleNameExpressionList.add((String)msgFlowNode.getProperties().get("filterExpression"));
				if (!CheckFilterNodeName(msgFlowNode.getName())) {
					createNewIssue(context, inputFile, "FilterNodeNameCheck",
							"The name of Node '" + msgFlowNode.getName() + "' (type: \"" + msgFlowNode.getType() + "\") should follow '"+PATTERN_STRING+"' pattern.");
				}
				
				if((!msgFlowNode.getOutputTerminals().contains("OutTerminal.unknown"))
						||(!msgFlowNode.getOutputTerminals().contains("OutTerminal.false"))
						||(!msgFlowNode.getOutputTerminals().contains("OutTerminal.true"))){
					createNewIssue(context, inputFile, "FilterNodeConnection",
							"One or more terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.");
				}
				
//				if(!msgFlowNode.getOutputTerminals().contains("OutTerminal.false")){
//					Issuable issuable = perspectives.as(Issuable.class, inputFile);
//				    issuable.addIssue(issuable.newIssueBuilder()
//				    	        	  .ruleKey(RuleKey.of("msgflow", "FilterNodeConnection"))
//				    	        	  .message("The false terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
//				    	        	  .build());
//				}
//				
//				if(!msgFlowNode.getOutputTerminals().contains("OutTerminal.true")){
//					Issuable issuable = perspectives.as(Issuable.class, inputFile);
//				    issuable.addIssue(issuable.newIssueBuilder()
//				    	        	  .ruleKey(RuleKey.of("msgflow", "FilterNodeConnection"))
//				    	        	  .message("The true terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
//				    	        	  .build());
//				}
				
//				if(!msgFlowNode.getInputTerminals().contains("InTerminal.in")){
//					Issuable issuable = perspectives.as(Issuable.class, inputFile);
//				    issuable.addIssue(issuable.newIssueBuilder()
//				    	        	  .ruleKey(RuleKey.of("msgflow", "DisconnectedNode"))
//				    	        	  .message("The in terminal (input) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
//				    	        	  .build());
//				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					createNewIssue(context, inputFile, "DisconnectedNode",
							"There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				
				if(!msgFlowNode.getName().equals(msgFlowNode.getProperties().get("filterExpression"))){
					createNewIssue(context, inputFile, "NodeNameModuleName",
							"The node name and the underlaying module name should match for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").");
				}
				
			}
			Set<String> moduleSet = new TreeSet<String>();
			for(String moduleName : moduleNameExpressionList){
				if(!moduleSet.add(moduleName)){
					createNewIssue(context, inputFile, "OneModuleMultipleNodes",
							"Multiple Filter nodes refers to same module '"+moduleName+"'.");
				}
			}
		}
	}
	
	public static boolean CheckFilterNodeName(String name) {
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
	}
}
