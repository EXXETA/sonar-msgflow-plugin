package com.exxeta.iss.sonar.msgflow.batch;

import java.util.Iterator;
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
 * configuration of a MQ Get Node.
 * 
 * @author Arjav Shah
 */
public class MQGetNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(MQOutputNodeSensor.class);
	
	/**
	 * Variable to hold file system information, e.g. the file names of the project files.
	 */
	private final FileSystem fs;
	
	public final static String PATTERN_STRING = "^[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.[A-Za-z0-9_]+\\.(AI|AO)$";
	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;
	
	/**
	  * Use of IoC to get FileSystem and ResourcePerspectives
	  */
	public MQGetNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMqGetNodes().iterator();
			
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(!((String)msgFlowNode.getProperties().get("queueName")).equals(msgFlowNode.getName())){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    		.ruleKey(RuleKey.of("msgflow", "MQNodeNameMatchesQueueName"))
				    		.message("The name of MQ Node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ") does not match the underlaying queue name.")
				    	        	  .build());
				}
				
				if(! msgFlowNode.getProperties().get("transactionMode").equals("automatic")) {

					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "MQNodeTxnMode"))
							.message("The 'Transaction Mode' property of " + msgFlowNode.getName() + "(type:"
									+ msgFlowNode.getType() + ") node is not set to Automatic.")
							.build());

				}
				
				if (msgFlowNode.getMessageDomainProperty().equals("XMLNS")) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "XMLNSCoverXMLNS"))
							.message("'Message domain' under 'Input Message Parsing' for '"
									+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
									+ ") is set as XMLNS. XMLNSC is preferred over XMLNS.")
							.build());
				}
				
				if (msgFlowNode.getInputTerminals().size()==0) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
				    issuable.addIssue(issuable.newIssueBuilder()
				    	        	  .ruleKey(RuleKey.of("msgflow", "DisconnectedNode"))
				    	        	  .message("There are no input connections to node '" + msgFlowNode.getName() + "' (type: " + msgFlowNode.getType() + ").")
				    	        	  .build());
				}
				if (!checkMQQueueName((String) msgFlowNode.getProperties().get("queueName"))) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(
							issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "queueNamingConvention"))
									.message("Naming convention for the queue specified on '"
											+ msgFlowNode.getName() + "' (type: " + msgFlowNode.getType()
											+ ") is not correct.")
									.build());
				}
			}
		}
	}
	public static boolean checkMQQueueName(String name) {

		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();

	}

}
