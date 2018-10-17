package com.exxeta.iss.sonar.msgflow.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;

import com.exxeta.iss.sonar.msgflow.MessageFlowPlugin;
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
public class MessageFlowGenericSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	//private static final Logger LOG = LoggerFactory.getLogger(ComputeNodeSensor.class);
	
	/* (non-Javadoc)
	 * @see org.sonar.api.batch.sensor.Sensor#execute(org.sonar.api.batch.sensor.SensorContext)
	 */
	@Override
	public void execute(SensorContext context) {
		 ArrayList<String> subflowList = new ArrayList<String>();
		 FileSystem fs = context.fileSystem();		
		 for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			// retrieve the message flow object
			String path = inputFile.absolutePath();
			
			if("subflow".equals(path.substring(path.lastIndexOf(".")+1))){
				subflowList.add(path.replace("/","_"));
			}
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(path);
			
			
			if((msgFlow.getLabelNodes().size()>0 && msgFlow.getRouteToLabelNodes().size()==0)
					||(msgFlow.getLabelNodes().size()==0 && msgFlow.getRouteToLabelNodes().size()>0)){
				createNewIssue(context, inputFile, "LabelWithoutRouteTo",
						"The Message flow '" + path + "'  does not have RouteToLabel and label in the same flow.");
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
					createNewIssue(context, inputFile, "MessageFlowInconsistentReply",
							"The Message flow '" + inputFile.filename()
							+ "'  does not reply to the incoming messages consistently.");
				}
				
			}
		}
		 
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			MessageFlow msgFlow = MessageFlowProject.getInstance().getMessageFlow(inputFile.absolutePath());
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getMiscellaneousNodes().iterator();
			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if(subflowList.contains(msgFlowNode.getType())) {
					subflowList.remove(msgFlowNode.getType());
				}
			}
		}
		
		for (InputFile inputFile : fs.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {
			for(String subflow:subflowList){
				if(inputFile.relativePath().equals(subflow)){
					createNewIssue(context, inputFile, "UnusedSubFlow",
							"The sub flow '" + subflow + "'  is not referenced anywhere. Hence, it should be removed");
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