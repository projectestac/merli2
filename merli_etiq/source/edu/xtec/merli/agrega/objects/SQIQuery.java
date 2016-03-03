package edu.xtec.merli.agrega.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;



public class SQIQuery extends ObjectMerli{

	private String targetSessionID;
	private String queryStatement;
	private int startResult;
	private String queryLanguageID;
	

	public SQIQuery() {
		super();

		this.targetSessionID = "";
		this.queryStatement = "";
		this.queryLanguageID = "";
		this.startResult = 0;
	}

	public SQIQuery(String targetSessionID, String queryStatement) {
		super();

		this.targetSessionID = targetSessionID;
		this.queryStatement = queryStatement;
	}

	public SQIQuery(String targetSessionID, String queryStatement, int startResult) {
		super();

		this.targetSessionID = targetSessionID;
		this.queryStatement = queryStatement;
		this.startResult = startResult;
	}
	public SQIQuery(String targetSessionID, String queryStatement, int startResult, String queryLanguage) {
		super();

		this.targetSessionID = targetSessionID;
		this.queryStatement = queryStatement;
		this.startResult = startResult;
		this.queryLanguageID = queryLanguage;
	}



	public SQIQuery(SOAPElement seSQIQuery) throws SOAPException {
		
		Iterator it = seSQIQuery.getChildElements(soapFactory.createName("targetSessionID"));
		this.targetSessionID = ((SOAPElement) it.next()).getValue();
		
		it = seSQIQuery.getChildElements(soapFactory.createName("queryStatement"));
		this.queryStatement = ((SOAPElement) it.next()).getValue();

		it = seSQIQuery.getChildElements(soapFactory.createName("startResult"));
		this.startResult = Integer.parseInt(((SOAPElement) it.next()).getValue());

		it = seSQIQuery.getChildElements(soapFactory.createName("queryLanguageID"));
		this.queryLanguageID = ((SOAPElement) it.next()).getValue();
	}



	public String getQueryStatement() {
		return queryStatement;
	}

	public void setQueryStatement(String queryStatement) {
		this.queryStatement = queryStatement;
	}

	public int getStartResult() {
		return startResult;
	}

	public void setStartResult(int startResult) {
		this.startResult = startResult;
	}

	public String getTargetSessionID() {
		return targetSessionID;
	}

	public void setTargetSessionID(String targetSessionID) {
		this.targetSessionID = targetSessionID;
	}

	public String getQueryLanguageID() {
		return queryLanguageID;
	}

	public void setQueryLanguageID(String queryLanguageID) {
		this.queryLanguageID = queryLanguageID;
	}

	public SOAPElement getQueryLanguageXml() throws SOAPException {

		SOAPElement seQueryLanguageID;

		//Crea l'element queryLanguage
		seQueryLanguageID = soapFactory.createElement(soapFactory.createName("queryLanguageID"));
		seQueryLanguageID.addTextNode(this.getQueryLanguageID());
		
		return seQueryLanguageID;
	}

	public SOAPElement getSessionIDXml() throws SOAPException {

		SOAPElement seSessionID;

		//Crea l'element queryLanguage
		seSessionID = soapFactory.createElement(soapFactory.createName("targetSessionID"));
		seSessionID.addTextNode(this.targetSessionID);
		
		return seSessionID;
	}

	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seSyncroQuery=null;
		SOAPElement seIdentifier;
		SOAPElement seType;
	        
		//Crea l'element IdResult
		seSyncroQuery = soapFactory.createElement(soapFactory.createName("synchronousQuery"));
		
		//Crea l'element targetSessionID
		if (targetSessionID == null) targetSessionID = "";
		seIdentifier = soapFactory.createElement(soapFactory.createName("targetSessionID"));
		seIdentifier.addTextNode(this.targetSessionID);
		//Afageix l'element Identifier al IdResult
		seSyncroQuery.addChildElement(seIdentifier);

		//Crea l'element queryStatement
		if (queryStatement == null) queryStatement = "";
		seType = soapFactory.createElement(soapFactory.createName("queryStatement"));
		seType.addTextNode(this.queryStatement);
		//Afageix l'element type al IdResult
		seSyncroQuery.addChildElement(seType);

		//Crea l'element queryStatement
		if (startResult<0) startResult=0;
		
		seType = soapFactory.createElement(soapFactory.createName("startResult"));
		seType.addTextNode(String.valueOf(this.startResult));
		//Afageix l'element type al IdResult
		seSyncroQuery.addChildElement(seType);	
		
		return seSyncroQuery;
	}
	

	public String toString(){
		return "synchronousQuery[targetSessionID="+getTargetSessionID()+",queryStatement="+getQueryStatement()+",startResult="+getStartResult()+"]";
	}
	
	
}
