package com.exxeta.iss.sonar.msgflow.batch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * configuration of a DSN property check for compute node
 * 
 * @author Arjav Shah
 */
public class DSNSensor implements Sensor {

	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DSNSensor.class);

	private static ArrayList<String> calledProcs = new ArrayList<String>();

	/**
	 * Variable to hold file system information, e.g. the file names of the
	 * project files.
	 */
	private final FileSystem fs;

	/**
	 * 
	 */
	private final ResourcePerspectives perspectives;

	/**
	 * Use of IoC to get FileSystem and ResourcePerspectives
	 */
	public DSNSensor(FileSystem fs, ResourcePerspectives perspectives) {
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


		for (InputFile inputFile : fs
				.inputFiles(fs.predicates().matchesPathPatterns(MessageFlowPlugin.FLOW_PATH_PATTERNS))) {

			/*
			 * retrieve the message flow object
			 */
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
					System.out.println(folderName);
					File msgflow = new  File(inputFile.absolutePath());
					System.out.println(msgflow.getParent()+File.separator+folderName);
					File directoryEsql = new File(msgflow.getParent()+File.separator+folderName);
					System.out.println(directoryEsql.getAbsolutePath());
					boolean isDbCalled = false;
					
					for (File esqlfile : directoryEsql.listFiles()) {
						if (esqlfile.getAbsolutePath().endsWith(".esql")) {
							if(checkForModule(esqlfile, moduleName)){
								isDbCalled = isDbCalled || checkForDbcall(esqlfile, moduleName);
							}
						}
					}
					if(!isDbCalled){
						Issuable issuable = perspectives.as(Issuable.class, inputFile);
						issuable.addIssue(issuable.newIssueBuilder()
								.ruleKey(RuleKey.of("msgflow", "DSNWithoutDBCall"))
								.message("DSN property is set without DB interactions for '" + msgFlowNode.getName()
						+ "' (type: " + msgFlowNode.getType() + ").").build());
					}
				}
			}
		}
	}

	public static boolean checkForDbcall(File file, String moduleName) {
		boolean dbCall = false;
//		File file = new File(inputfile);
		ArrayList<String> moduleLines = new ArrayList<String>();
		boolean isModuleLine =false;
		try {
			boolean commentSection = false;
			for (String line : FileUtils.readLines(file, "UTF-8")) {
				if (!line.trim().isEmpty() && !line.trim().startsWith("--") && !line.trim().startsWith("/*")
						&& !commentSection) {
					if(line.toUpperCase().replaceAll("\\s+", "").startsWith("CREATECOMPUTEMODULE"+moduleName)){
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
//			System.out.println("catch");
			dbCall = false;

		}
//		System.out.println("==============================================");
//		System.out.println(dbCall);
//		System.out.println("==============================================");
		return dbCall;
	}
	
	public static boolean checkForModule(File file, String moduleName) {
		boolean moduleExists = false;
		try{
//			File file = new File(inputfile);
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
//		if(moduleExists){
//			System.out.println("ModuleExists====="+moduleExists);
//			System.out.println("File============"+file);
//			System.out.println("Module============"+moduleName);
//		}
		return moduleExists;
	}

}
