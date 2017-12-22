package com.exxeta.iss.sonar.msgflow.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

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
public class FilterNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(MQOutputNodeSensor.class);

	/**
	 * Variable to hold file system information, e.g. the file names of the project
	 * files.
	 */
	private final FileSystem fs;
	public final static String PATTERN_STRING = "(Is|Has|Can|TrueIf|FalseIf)[A-Z][a-zA-Z0-9]*$";

	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;

	/**
	 * Use of IoC to get FileSystem and ResourcePerspectives
	 */
	public FilterNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.
	 * resources.Project)
	 */
	@Override
	public boolean shouldExecuteOnProject(Project arg0) {
		// This sensor is executed only when there are msgflow files
		return fs.hasFiles(fs.predicates().hasLanguage("msgflow"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sonar.api.batch.Sensor#analyse(org.sonar.api.resources.Project,
	 * org.sonar.api.batch.SensorContext)
	 */
	/**
	 * The method where the analysis of the connections and configuration of the
	 * message flow node takes place.
	 */
	@Override
	public void analyse(Project arg0, SensorContext arg1) {
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {

			/*
			 * retrieve the message flow object
			 */
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());

			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getFilterNodes().iterator();
			ArrayList<String> moduleNameExpressionList = new ArrayList<String>();
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				moduleNameExpressionList.add((String)msgFlowNode.getProperties().get("filterExpression"));
				if (!CheckFilterNodeName(msgFlowNode.getName())) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "FilterNodeNameCheck"))
							.message(
									"The name of Node '" + msgFlowNode.getName() + "' (type: \"" + msgFlowNode.getType() + "\") should follow '"+PATTERN_STRING+"' pattern.")
							.build());

				}
				
				
				
				if((!msgFlowNode.getOutputTerminals().contains("OutTerminal.unknown"))
						||(!msgFlowNode.getOutputTerminals().contains("OutTerminal.false"))
						||(!msgFlowNode.getOutputTerminals().contains("OutTerminal.true"))){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "FilterNodeConnection"))
				    	        	  .message("One or more terminal (output) for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") is not connected.")
				    	        	  .build());
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
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "DisconnectedNode"))
				    	        	  .message("There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				
				if(!msgFlowNode.getName().equals(msgFlowNode.getProperties().get("filterExpression"))){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "NodeNameModuleName"))
				    	        	  .message("The node name and the underlaying module name should match for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				
			}
			Set<String> moduleSet = new TreeSet<String>();
			for(String moduleName:moduleNameExpressionList){
				if(!moduleSet.add(moduleName)){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "OneModuleMultipleNodes"))
				    	        	  .message("Multiple Filter nodes refers to same module '"+moduleName+"'.")
				    	        	  .build());
				}
			}
		}
	}
	public static boolean CheckFilterNodeName(String name) {
		
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
		
	}
}
