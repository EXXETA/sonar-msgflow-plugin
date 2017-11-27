package com.exxeta.iss.sonar.msgflow.model;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * The class is a model of a message map
 * 
 * @author Arjav Shah
 */
public class MessageMapParser {
	
	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MessageFlowParser.class);
	
	/**
	 * @param fileName name of the file to be processed
	 * @param mappingSources mapping of the sources of the map
	 * @param mappings list of all the mappings in the map
	 * @param inputVar the variable name of input source 
	 * @param outputVar the variable name of output source
	 * @param todoCount count of the todo blocks
	 */
	public void parse(String fileName,
			Mapping mappingSources,
			ArrayList<Mapping> mappings,
			StringBuilder inputVar,
			StringBuilder outputVar,
			AtomicInteger todoCount){
		LOG.debug("START");

		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
			
			XPathExpression todoExp = XPathFactory.newInstance().newXPath().compile("count(//task)");
			todoCount = (AtomicInteger) todoExp.evaluate(document,XPathConstants.NUMBER);
			
			XPathExpression nomExp = XPathFactory.newInstance().newXPath().compile("count(//input)");
			int nom = (int) nomExp.evaluate(document,XPathConstants.NUMBER);
			
			for(; nom > 0; nom--) {
				XPathExpression inPathExpr= XPathFactory.newInstance().newXPath().compile("//input[" + nom +  "]/@path");
				XPathExpression outPathExpr= XPathFactory.newInstance().newXPath().compile("//ouput[" + nom +  "]/@path");
			}
		}catch(Exception e){
			
		}

}
	
	
}
