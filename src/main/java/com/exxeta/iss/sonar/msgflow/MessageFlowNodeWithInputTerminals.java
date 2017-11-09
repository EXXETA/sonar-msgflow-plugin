/**
 * 
 */
package com.exxeta.iss.sonar.msgflow;

/**
 * @author Arjav Shah
 *
 */
public enum MessageFlowNodeWithInputTerminals {


	MQOUTPUT("MQOutput"), 
	COMIBMMQREPLY("ComIbmMQReply"), 
	MQGET("MQGet"), 
	MQHEADER("MQHeader"), 
	JMSCLIENTOUTPUT("JMSClientOutput"), 
	JMSCLIENTREPLY("JMSClientReply"), 
	JMSCLIENTRECEIVE("JMSClientReceive"), 
	JMSHEADER("JMSHeader"), 
	JMSMQTRANSFORM("JMSMQTransform"), 
	MQJMSTRANSFORM("MQJMSTransform"), 
	WSREPLY("WSReply"), 
	WSREQUEST("WSRequest"), 
	HTTPHEADER("HTTPHeader"), 
	SOAPREPLY("SOAPReply"), 
	SOAPASYNCREQUEST("SOAPAsyncRequest"), 
	SOAPENVELOPE("SOAPEnvelope"), 
	SOAPEXTRACT("SOAPExtract"), 
	SRRETRIEVEENTITY("SRRetrieveEntity"), 
	SRRETRIEVEITSERVICE("SRRetrieveITService"), 
	SCAREPLY("SCAReply"), 
	SCAREQUEST("SCARequest"), 
	SCAASYNCREQUEST("SCAAsyncRequest"), 
	PEOPLESOFTREQUEST("PeopleSoftRequest"), 
	SAPREQUEST("SAPRequest"), 
	SAPREPLY("SAPReply"), 
	SIEBELREQUEST("SiebelRequest"), 
	JDEDWARDSREQUEST("JDEdwardsRequest"), 
	TWINEBALLREQUEST("TwineballRequest"), 
	FILTER("Filter"), 
	PUBLICATION("Publication"), 
	ROUTETOLABEL("RouteToLabel"), 
	ROUTE("Route"), 
	AGGREGATECONTROL("AggregateControl"), 
	AGGREGATEREPLY("AggregateReply"), 
	AGGREGATEREQUEST("AggregateRequest"), 
	COLLECTOR("Collector"), 
	RESEQUENCE("ReSequence"), 
	SEQUENCE("Sequence"), 
	COMPUTE("Compute"), 
	DOTNETCOMPUTE("DotNetCompute"), 
	MSLMAPPING("MSLMapping"), 
	XSLMQSI("XslMqsi"), 
	JAVACOMPUTE("JavaCompute"), 
	PHPCOMPUTE("PhpCompute"), 
	THROW("Throw"), 
	TRACE("Trace"), 
	TRYCATCH("TryCatch"), 
	FLOWORDER("FlowOrder"), 
	PASSTHRU("Passthru"), 
	RESETCONTENTDESCRIPTOR("ResetContentDescriptor"), 
	DATABASERETRIEVE("DatabaseRetrieve"), 
	DATABASEROUTE("DatabaseRoute"), 
	FILEOUTPUT("FileOutput"), 
	FILEREAD("FileRead"), 
	FTEOUTPUT("FTEOutput"), 
	CDOUTPUT("CDOutput"), 
	EMAILOUTPUT("EmailOutput"), 
	TCPIPCLIENTOUTPUT("TCPIPClientOutput"), 
	TCPIPCLIENTRECEIVE("TCPIPClientReceive"), 
	TCPIPSERVEROUTPUT("TCPIPServerOutput"), 
	TCPIPSERVERRECEIVE("TCPIPServerReceive"), 
	CORBAREQUEST("CORBARequest"), 
	CICSIPICREQUEST("CICSIPICRequest"), 
	IMSREQUEST("IMSRequest"), 
	VALIDATE("Validate"), 
	CHECK("Check"), 
	SECURITYPEP("SecurityPEP"), 
	TIMEOUTCONTROL("TimeoutControl")
	;

	private final String value;

	private MessageFlowNodeWithInputTerminals(String value) {
		this.value = value;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	

	public static String[] keywordValues() {
		MessageFlowNodeWithInputTerminals[] keywordsEnum = MessageFlowNodeWithInputTerminals.values();
		String[] keywords = new String[keywordsEnum.length];
		for (int i = 0; i < keywords.length; i++) {
			keywords[i] = keywordsEnum[i].getValue();
		}
		return keywords;
	}



	

}