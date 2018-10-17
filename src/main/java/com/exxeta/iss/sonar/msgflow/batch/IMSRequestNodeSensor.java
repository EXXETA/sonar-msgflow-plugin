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

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse configuration of a
 * IMS Request Node.
 *
 * @author Arjav Shah
 */
public class IMSRequestNodeSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	// private static final Logger LOG =
	// LoggerFactory.getLogger(TryCatchNodeSensor.class);

	public final static String PATTERN_STRING = "(IMS Request )[0-9]$";

	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());

			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getImsRequestNodes().iterator();

			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();

				if ((msgFlowNode.getProperties().get("shortDescription") == null
						|| ((String) msgFlowNode.getProperties().get("shortDescription")).isEmpty())
						&& (msgFlowNode.getProperties().get("longDescription") == null
								|| ((String) msgFlowNode.getProperties().get("longDescription")).isEmpty())) {
					createNewIssue(context, inputFile, "IMSRequestDescription",
							"The short and Long Description for the Node '" + msgFlowNode.getName()
							+ "' (type: " + msgFlowNode.getType() + ") should be available.");
				}

				if (msgFlowNode.getProperties().get("useNodeProperties")== null|| !((String)msgFlowNode.getProperties().get("useNodeProperties")).equals("false")) {
					createNewIssue(context, inputFile, "IMSRequestNodeDefinedProperties",
							"'Use Connection properties defined on Node' option is checked or configurable service is not specified for node '" + msgFlowNode.getName()
							+ "' (type: " + msgFlowNode.getType() + ").");
				}
				
				if(msgFlowNode.getProperties().get("commitMode")!=null && !((String) msgFlowNode.getProperties().get("commitMode")).equals("commitThenSend")){
					createNewIssue(context, inputFile, "IMSRequestCommitMode",
							"Commit Mode should be set to '0:COMMIT_THEN_SEND' for the node '" + msgFlowNode.getName()
							+ "' (type: " + msgFlowNode.getType() + ").");
				}
				
				if(!msgFlowNode.getMessageDomainProperty().isEmpty() && !msgFlowNode.getMessageDomainProperty().equals("BLOB")){
					createNewIssue(context, inputFile, "IMSRequestMessageDomain",
							"Message Domain should be set as 'BLOB' for the node '" + msgFlowNode.getName()
							+ "' (type: " + msgFlowNode.getType() + ").");
				}
				
				if(!CheckIMSNodeName(msgFlowNode.getName())){
					createNewIssue(context, inputFile, "IMSRequestNodeName",
							"Node name for'" + msgFlowNode.getName()
							+ "' (type: " + msgFlowNode.getType() + ") should follow the pattern '"+PATTERN_STRING+"'.");
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
			}
		}
	}
	
	public static boolean CheckIMSNodeName(String name) {
		Pattern pattern = Pattern.compile(PATTERN_STRING);
		return pattern.matcher(name).find();
		
	}
}
