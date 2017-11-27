package com.exxeta.iss.sonar.msgflow.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
			todoCount = new AtomicInteger(Integer.parseInt((String)todoExp.evaluate(document,XPathConstants.STRING)));
			
			XPathExpression inputSrcPathExp = XPathFactory.newInstance().newXPath().compile("//MappingDeclaration/input/@path");
			String inputSrcPath = (String) inputSrcPathExp.evaluate(document, XPathConstants.STRING);
			XPathExpression outputSrcPathExp = XPathFactory.newInstance().newXPath().compile("//MappingDeclaration/output/@path");
			String outputSrcPath = (String) outputSrcPathExp.evaluate(document, XPathConstants.STRING);
			mappingSources = new Mapping(inputSrcPath, outputSrcPath);
			XPathExpression inputVarExp = XPathFactory.newInstance().newXPath().compile("//MappingDeclaration/input/@var");
			inputVar.delete(0, inputVar.length());
			inputVar.append((String) inputVarExp.evaluate(document,XPathConstants.STRING));
			XPathExpression outputVarExp = XPathFactory.newInstance().newXPath().compile("//MappingDeclaration/output/@var");
			outputVar.delete(0, outputVar.length());
			outputVar.append((String) outputVarExp.evaluate(document,XPathConstants.STRING));
			
			XPathExpression inPathExpr= XPathFactory.newInstance().newXPath().compile("//input");
			NodeList inpath = (NodeList) inPathExpr.evaluate(document, XPathConstants.NODESET);
			for(int noi=0;noi<inpath.getLength();noi++) {
				Node inpathNode = inpath.item(noi);
				if(!inpathNode.getParentNode().getNodeName().equals("MappingDeclaration")) {
					String inputPath = inpathNode.getAttributes().getNamedItem("path").getNodeValue();
					String outputPath = inpathNode.getNextSibling().getAttributes().getNamedItem("path").getNodeValue();
					Mapping mapping = new Mapping(inputPath, outputPath);
					mappings.add(mapping);
				}
			}

			
		}catch(XPathExpressionException e){
			LOG.error(e.getMessage());
		} catch (SAXException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} catch (ParserConfigurationException e) {
			LOG.error(e.getMessage());
		} catch (FactoryConfigurationError e) {
			LOG.error(e.getMessage());
		}

}
	
	
}
