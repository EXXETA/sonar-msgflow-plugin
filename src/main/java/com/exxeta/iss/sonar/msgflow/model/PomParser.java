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
public class PomParser {

	/**
	 * The logger for the class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MessageFlowParser.class);

	public void parse(String fileName, ArrayList<String> modules, StringBuilder artifact) {
		LOG.debug("START");

		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);

			XPathExpression artifactExp = XPathFactory.newInstance().newXPath().compile("//project/artifactId");
			String inputSrcPath = (String) artifactExp.evaluate(document, XPathConstants.STRING);
			artifact.delete(0, artifact.length());
			artifact.append(inputSrcPath);
			XPathExpression NoOfmodule = XPathFactory.newInstance().newXPath().compile("count(//modules/module)");
			int nom = Integer.parseInt((String) NoOfmodule.evaluate(document, XPathConstants.STRING));
			for (; nom > 0; nom--) {
				XPathExpression ModuleNameExp = XPathFactory.newInstance().newXPath()
						.compile("//modules/module[" + nom + "]");
				String moduleName = (String) ModuleNameExp.evaluate(document, XPathConstants.STRING);
				modules.add(moduleName);
			}

		} catch (XPathExpressionException e) {
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
