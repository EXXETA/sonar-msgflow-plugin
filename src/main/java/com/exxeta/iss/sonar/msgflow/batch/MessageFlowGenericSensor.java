package com.exxeta.iss.sonar.msgflow.batch;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowConnection;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowProject;

/**
 * The class (sensor) contains the method to analyse the connections  
 * of a message flow nodes
 * 
 * @author Arjav Shah
 */
public class MessageFlowGenericSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(ComputeNodeSensor.class);
	
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
	public MessageFlowGenericSensor(FileSystem fs, ResourcePerspectives perspectives) {
		this.fs = fs;
		this.perspectives = perspectives;
	}
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.CheckProject#shouldExecuteOnProject(org.sonar.api.resources.Project)
	 */
	/**
	 * The method defines the language of the file to be analysed.
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
			
			
			if((msgFlow.getLabelNodes().size()>0 && msgFlow.getRouteToLabelNodes().size()==0)
					||(msgFlow.getLabelNodes().size()==0 && msgFlow.getRouteToLabelNodes().size()>0)){
				Issuable issuable = perspectives.as(Issuable.class, inputFile);
			    issuable.addIssue(issuable.newIssueBuilder()
			    	        	  .ruleKey(RuleKey.of("msgflow", "LabelWithoutRouteTo"))
			    	        	  .message("The Message flow '" + inputFile.relativePath() + "'  does not have RouteToLabel and label in the same flow.")
			    	        	  .build());
			}
			
			if (((msgFlow.getMqInputNodes().size() > 0) && (msgFlow.getMqReplyNodes().size() > 0))
					|| ((msgFlow.getHttpInputNodes().size() > 0) && (msgFlow.getHttpReplyNodes().size() > 0))) {
				boolean isFlowConsistentMq = true;
				boolean isFlowConsistentHttp = true;
				for (MessageFlowNode flowNode : msgFlow.getMqInputNodes()) {
					String srcIdMq = flowNode.getId();
					ArrayList<MessageFlowNode> mqReplyNodes = msgFlow.getMqReplyNodes();
					Set<String> pathMq = new LinkedHashSet<String>();
					pathMq.add(srcIdMq);
					for (Set<String> pathRet : getFullPath(pathMq, srcIdMq, msgFlow)) {
						boolean isPathCompleteMq = false;
						for(MessageFlowNode target : mqReplyNodes){
							if (pathRet.contains(target.getId())) {
								isPathCompleteMq = isPathCompleteMq || true;
								break;
							}
						}
						isFlowConsistentMq = isFlowConsistentMq && isPathCompleteMq;
					}
				}
					
				for(MessageFlowNode flowNodeHttp : msgFlow.getHttpInputNodes()){
					String srcId = flowNodeHttp.getId();
					ArrayList<MessageFlowNode> httpReplyNodes = msgFlow.getHttpReplyNodes();
					Set<String> pathHttp = new LinkedHashSet<String>();
					pathHttp.add(srcId);
					
					for (Set<String> pathRet : getFullPath(pathHttp, srcId, msgFlow)) {
						boolean isPathCompleteHttp = false;
						for(MessageFlowNode target:httpReplyNodes){
							if (pathRet.contains(target.getId())) {
								isPathCompleteHttp = isPathCompleteHttp || true;
								break;
							}
							isFlowConsistentHttp = isFlowConsistentHttp && isPathCompleteHttp;
						}
					}
				}
					
				if(!isFlowConsistentMq || !isFlowConsistentHttp){
					Issuable issuable = perspectives.as(Issuable.class, inputFile);
					issuable.addIssue(
							issuable.newIssueBuilder().ruleKey(RuleKey.of("msgflow", "MessageFlowInconsistentReply"))
									.message("The Message flow '" + inputFile.relativePath()
											+ "'  does not reply to the incoming messages consistently.")
									.build());
				}
				
			}
		}
	}
	
	public static ArrayList<Set<String>> getFullPath(Set<String> path,String srcId,MessageFlow mf){
		ArrayList<Set<String>> paths = new ArrayList<Set<String>>();
		for(String x : getNextNodes(srcId, mf)){
			Set<String> pathTmp = new LinkedHashSet<String>();
			pathTmp.addAll(path);
			pathTmp.add(x);
			if(GetPathCount(x, mf)!=0){
				paths = getFullPath(pathTmp, x, mf);
			}
			else{
				paths.add(pathTmp);
			}			
		}
		return paths;
	}
	
	public static int GetPathCount(String srcId,MessageFlow mf){
		int i = 0;
		for(MessageFlowConnection con : mf.getConnections()){
			if(con.getSrcNode().equals(srcId)){
				i++;
			}
		}
		return i;
	}
	
	public static ArrayList<String> getNextNodes(String srcId,MessageFlow mf){
		ArrayList<String> i = new ArrayList<String>();
		for(MessageFlowConnection con : mf.getConnections()){
			if(con.getSrcNode().equals(srcId)){
				i.add(con.getTargetNode());
			}
		}
		return i;
	}
}