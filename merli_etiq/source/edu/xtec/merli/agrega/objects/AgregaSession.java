package edu.xtec.merli.agrega.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;



public class AgregaSession extends ObjectMerli{

	private String userID;
	private String password;
	private String sessionID;
	

	public AgregaSession() {
		super();

		this.userID = "";
		this.password = "";
		this.sessionID="";
	}

	public AgregaSession(String userId, String password) {
		super();

		this.userID = userId;
		this.password = password;
	}

	public AgregaSession(String userId, String password, String sessionID) {
		super();

		this.userID = userID;
		this.password = password;
		this.sessionID = sessionID;
	}



	public AgregaSession(SOAPElement seSessionResource) throws SOAPException {
		
		Iterator it = seSessionResource.getChildElements(soapFactory.createName("userID"));
		if (it.hasNext())
		userID = ((SOAPElement) it.next()).getValue();

		it = seSessionResource.getChildElements(soapFactory.createName("password"));
		if (it.hasNext())
		password = ((SOAPElement) it.next()).getValue();

		it = seSessionResource.getChildElements(soapFactory.createName("sessionID"));
		if (it.hasNext())
		sessionID = ((SOAPElement) it.next()).getValue();
		
	}

	public String getUserID() {
		return userID;
	}
	

	public void setUserID(String userID) {
		if (userID==null) userID = "";
		this.userID = userID;
	}
	

	public String getPassword() {
		if (password==null) password = "";
		return password;
	}
	

	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seIdResult=null;
	        
		//Crea l'element IdResult
		seIdResult = soapFactory.createElement(soapFactory.createName("createSession"));
		
		//Crea l'element userID
		if (userID == null) userID = "";
		//Afageix l'element UserID al IdResult
		seIdResult.addChildElement(this.getUserIDXml());

		//Crea l'element password
		if (password == null) password = "";
		//Afageix l'element password al IdResult
		seIdResult.addChildElement(this.getPasswordXml());
		
		
		return seIdResult;
	}
	

	public String toString(){
		return "AgregaSession[sessionID="+getSessionID()+", userId="+getUserID()+",password="+getPassword()+"]";
	}

	public SOAPElement getUserIDXml() throws SOAPException {

		SOAPElement seUserID;

		//Crea l'element userID
		seUserID = soapFactory.createElement(soapFactory.createName("userID"));
		seUserID.addTextNode(this.userID);
		
		return seUserID;
	}

	public SOAPElement getPasswordXml() throws SOAPException {
		SOAPElement sePassword;
		
		//Crea l'element password
		sePassword = soapFactory.createElement(soapFactory.createName("password"));
		sePassword.addTextNode(this.password);
		
		return sePassword;
	}

	public SOAPElement getSessionIDXml() throws SOAPException {
		SOAPElement seSessionID;
		
		//Crea l'element password
		seSessionID = soapFactory.createElement(soapFactory.createName("sessionID"));
		seSessionID.addTextNode(this.sessionID);
		
		return seSessionID;
	}
	

	public SOAPElement getResponse(SOAPMessage sm, String response) throws SOAPException {
		Iterator responseValue;
		Node res;
		parseResponse(sm);
		if (!"destroySession".equals(response)){
			responseValue = sm.getSOAPBody().getChildElements(soapFactory.createName(response));
			if (responseValue!=null && responseValue.hasNext()){
				res = (Node)responseValue.next();
				return res.getParentElement();
			}
			return null;//((Node)((SOAPElement) sm.getSOAPBody().getChildElements(soapFactory.createName(response)).next()).getFirstChild().getFirstChild()).getParentElement();
		}else
			return null;
	}
	
}
