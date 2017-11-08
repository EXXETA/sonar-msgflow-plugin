/*
 * Sonar Message Flow Plugin 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse configuration of a
 * IMS Request Node.
 *
 * @author Arjav Shah
 */
public class IMSRequestNodeSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(TryCatchNodeSensor.class);

	/**
	 * Variable to hold file system information, e.g. the file names of the
	 * project files.
	 */
	private final FileSystem fs;
	public final static String PATTERN_STRING = "(IMS Request )[0-9]$";

	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;

	/**
	 * Use of IoC to get FileSystem and ResourcePerspectives
	 */
	public IMSRequestNodeSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.
	 * resources.Project)
	 */
	/**
	 * The method defines the language of the file to be analysed.
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
		for (InputFile inputFile : fs.inputFiles(fs.predicates().hasLanguage("msgflow"))) {

			/*
			 * retrieve the message flow object
			 */
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());

			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getImsRequestNodes().iterator();

			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();

				if ((msgFlowNode.getProperties().get("shortDescription") == null
						|| ((String) msgFlowNode.getProperties().get("shortDescription")).isEmpty())
						&& (msgFlowNode.getProperties().get("longDescription") == null
								|| ((String) msgFlowNode.getProperties().get("longDescription")).isEmpty())) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "IMSRequestDescription"))
							.message("The short and Long Description for the Node '" + msgFlowNode.getName()
									+ "' (type: " + msgFlowNode.getType() + ") should be available.")
							.build());
				}

				if ((msgFlowNode.getProperties().get("useNodeProperties")== null|| !((String)msgFlowNode.getProperties().get("useNodeProperties")).equals("false"))
						&& (msgFlowNode.getProperties().get("configurableService") == null
								|| ((String) msgFlowNode.getProperties().get("configurableService")).isEmpty())) {
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "IMSRequestNodeDefinedProperties"))
							.message("'Use Connection properties defined on Node' option is checked or configurable service is not specified for node '" + msgFlowNode.getName()
									+ "' (type: " + msgFlowNode.getType() + ").")
							.build());
				}
				
				if(msgFlowNode.getProperties().get("commitMode")!=null && !((String) msgFlowNode.getProperties().get("commitMode")).equals("commitThenSend")){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "IMSRequestCommitMode"))
							.message("Commit Mode should be set to '0:COMMIT_THEN_SEND' for the node '" + msgFlowNode.getName()
									+ "' (type: " + msgFlowNode.getType() + ").")
							.build());
				}
				
				if(!msgFlowNode.getMessageDomainProperty().isEmpty() && !msgFlowNode.getMessageDomainProperty().equals("BLOB")){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "IMSRequestMessageDomain"))
							.message("Message Domain should be set as 'BLOB' for the node '" + msgFlowNode.getName()
									+ "' (type: " + msgFlowNode.getType() + ").")
							.build());
				}
				
				if(!CheckIMSNodeName(msgFlowNode.getName())){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "IMSRequestNodeName"))
							.message("Node name for'" + msgFlowNode.getName()
									+ "' (type: " + msgFlowNode.getType() + ") should follow the pattern '"+PATTERN_STRING+"'.")
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
			}
		}
	}
	
public static boolean CheckIMSNodeName(String name) {
		
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
		
	}

}
