package com.exxeta.iss.sonar.msgflow.batch;

import java.util.ArrayList;
import java.util.HashSet;
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

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyze the configuration of a Database Node.
 * 
 * @author Arjav Shah
 */
public class DatabaseNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);
	
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
	public DatabaseNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
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
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			
			/* 
			 * retrieve the message flow object
			 */
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			
			// the actual rule ...
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getDatabaseNodes().iterator();
			ArrayList<String> moduleNameExpressionList = new ArrayList<String>();
			while (iMsgFlowNodes.hasNext()) {
				
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				moduleNameExpressionList.add((String)msgFlowNode.getProperties().get("statement"));
				if(!msgFlowNode.getName().equals(msgFlowNode.getProperties().get("statement"))){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "NodeNameModuleName"))
				    	        	  .message("The node name and the underlaying module name should match for '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				
			}
			Set<String> moduleSet = new HashSet<String>();
			for(String moduleName:moduleNameExpressionList){
				if(!moduleSet.add(moduleName)){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "OneModuleMultipleNodes"))
				    	        	  .message("Multiple Database nodes refers to same module '"+moduleName+"'.")
				    	        	  .build());
				}
			}
		}
	}
}
