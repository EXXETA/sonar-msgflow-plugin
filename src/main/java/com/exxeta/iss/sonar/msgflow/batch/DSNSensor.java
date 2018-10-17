package com.exxeta.iss.sonar.msgflow.batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * configuration of a DSN property check for compute node
 * 
 * @author Arjav Shah
 */
public class DSNSensor extends AbstractSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DSNSensor.class);

	private static ArrayList<String> calledProcs = new ArrayList<String>();

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
			Iterator<MessageFlowNode> iMsgFlowNodes = msgFlow.getComputeNodes().iterator();

			while (iMsgFlowNodes.hasNext()) {
				MessageFlowNode msgFlowNode = iMsgFlowNodes.next();
				if (msgFlowNode.getProperties().get("dataSource") != null
						&& !(((String) msgFlowNode.getProperties().get("dataSource")).isEmpty())) {

					String moduleName = (String) msgFlowNode.getProperties().get("computeExpression");
					String moduleNameFull = (String) msgFlowNode.getProperties().get("computeExpressionFull");
					String folderName = moduleNameFull.substring(moduleNameFull.indexOf("esql://routine/")+15, moduleNameFull.indexOf("#"));
					folderName = folderName.replace(".", File.separator);
					File msgflow = new  File(inputFile.absolutePath());
					String directoryEsqlPath = "";
					File projectDir = getProjectDirectory(msgflow);
					if(folderName.isEmpty()) {
						directoryEsqlPath = projectDir.getAbsolutePath();
					}else {
						directoryEsqlPath = projectDir.getAbsolutePath()+File.separator+folderName;
					}
					File directoryEsql = new File(directoryEsqlPath);
					boolean isDbCalled = false;
					List<File> esqlList = Arrays.asList(directoryEsql.listFiles());
					for (File esqlfile : esqlList) {
						if (esqlfile.getAbsolutePath().endsWith(".esql")) {
							if(checkForModule(esqlfile, moduleName)){
								isDbCalled = isDbCalled || checkForDbcall(esqlfile, moduleName);
							}
						}
					}
					if(!isDbCalled){
						createNewIssue(context, inputFile, "DSNWithoutDBCall", 
								"DSN property is set without DB interactions for '" + msgFlowNode.getName()
								+ "' (type: " + msgFlowNode.getType() + ").");
					}
				}
			}
		}
	}

	public static boolean checkForDbcall(File file, String moduleName) {
		boolean dbCall = false;
		ArrayList<String> moduleLines = new ArrayList<String>();
		boolean isModuleLine =false;
		try {
			boolean commentSection = false;
			for (String line : FileUtils.readLines(file, "UTF-8")) {
				if (!line.trim().isEmpty() && !line.trim().startsWith("--") && !line.trim().startsWith("/*")
						&& !commentSection) {
					if(line.toUpperCase().replaceAll("\\s+", "").startsWith("CREATECOMPUTEMODULE"+moduleName.toUpperCase())){
						isModuleLine = true;
					}else if(line.toUpperCase().replaceAll("\\s+", "").startsWith("ENDMODULE;")){
						isModuleLine = false;
					}
					if(isModuleLine){
						moduleLines.add(line);
					}
				} else if (line.trim().startsWith("/*") && !commentSection && !line.trim().endsWith("*/")) {
					commentSection = true;
				} else if (commentSection && line.trim().endsWith("*/")) {
					commentSection = false;
				}

			}
			
			for(String line : moduleLines){
				if (line.trim().toUpperCase().startsWith("SELECT ")
						|| line.trim().toUpperCase().startsWith("UPDATE ")
						|| line.trim().toUpperCase().startsWith("DELETE ")
						|| line.trim().toUpperCase().startsWith("INSERT ")) {
					dbCall = true;
					break;
				} else if (line.contains("=") && ((line.toUpperCase()).substring(line.indexOf("=") + 1).trim()
						.startsWith("SELECT ")
						|| (line.toUpperCase()).substring(line.indexOf("=") + 1).trim().startsWith("UPDATE ")
						|| (line.toUpperCase()).substring(line.indexOf("=") + 1).trim().startsWith("DELETE ")
						|| (line.toUpperCase()).substring(line.indexOf("=") + 1).trim().startsWith("INSERT "))) {
					dbCall = true;
					break;
				} else if (line.toUpperCase().replaceAll("\\s+", "").contains("PASSTHRU(")||
						line.toUpperCase().replaceAll("\\s+", "").contains("PASSTHRU")) {
					dbCall = true;
					break;
				} else if (line.toUpperCase().replaceAll("\\s+", "").startsWith("PASSTHRU")) {
					dbCall = true;
					break;
				} else if (line.toUpperCase().trim().startsWith("CALL")) {
					String tmpLine = line.replaceAll("\\s+", " ").toUpperCase();
					String procName = tmpLine.substring(tmpLine.indexOf("CALL ") + 5, tmpLine.indexOf("("));
					calledProcs.add(procName);
					//enhancement for checking the called procedure from the module for the DB calls
				}
			}

		} catch (IOException e) {
			LOG.error(e.getMessage());
			dbCall = false;

		}
		return dbCall;
	}
	
	public static boolean checkForModule(File file, String moduleName) {
		boolean moduleExists = false;
		try{
			String fileAsString = FileUtils.readFileToString(file, "UTF-8");
			if (fileAsString.contains(moduleName) && (fileAsString.toUpperCase().replaceAll("\\s+", ""))
					.contains("CREATECOMPUTEMODULE" + moduleName.toUpperCase())) {
				moduleExists = true;
			}else{
				moduleExists = false;
			}
		}
		 catch (IOException e) {
				LOG.error(e.getMessage());
				moduleExists = false;

			}
		return moduleExists;
	}
	
	
	private File getProjectDirectory(File msgflowFile) {
		File projectDirectory = new File(msgflowFile.getAbsolutePath());
		if (!projectDirectory.isDirectory()){
			projectDirectory=projectDirectory.getParentFile();
		}
		while (projectDirectory != null) {
			LOG.info("Checking " + projectDirectory.getAbsolutePath());
			if (new File(projectDirectory, ".project").exists()) {
				LOG.info("Returning " + projectDirectory.getAbsolutePath());
				return projectDirectory;
			}
			projectDirectory = projectDirectory.getParentFile();
		}
		LOG.info("Nothing found.");
		return null;
	}
}