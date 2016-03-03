package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;



public class Result extends ObjectMerli{

	private String operation;
	private String resultCode;
	private String resultDescription;
	
	private ObjectMerli resultObject;


	public Result() {
		super();
		// TODO Auto-generated constructor stub
		this.operation = "";
		this.resultCode = "";
		this.resultDescription = "";
		this.resultObject = null;
	}

	
	
	public Result(String operation, String resultCode, String resultDescription, ObjectMerli resultObject) {
		super();
		// TODO Auto-generated constructor stub
		this.operation = operation;
		this.resultCode = resultCode;
		this.resultDescription = resultDescription;
		this.resultObject = resultObject;
	}


	public Result(SOAPElement seResult) throws SOAPException {
		
		Iterator it = seResult.getChildElements(soapFactory.createName("operation"));
		operation = ((SOAPElement) it.next()).getValue();
		
		it = seResult.getChildElements(soapFactory.createName("resultCode"));
		resultCode = ((SOAPElement) it.next()).getValue();
		
		it = seResult.getChildElements(soapFactory.createName("resultDescription"));
		resultDescription = ((SOAPElement) it.next()).getValue();
		
		it = seResult.getChildElements(soapFactory.createName("resultObject"));
		Iterator it2 = ((SOAPElement) it.next()).getChildElements();
		SOAPElement objType = (SOAPElement) it2.next();
		String name = (objType).getElementName().getLocalName();
		
		if ("elementDUC".equals(name))
			resultObject = new ElementDUC(objType);
		else if ("idResource".equals(name))
			resultObject = new IdResource(objType);
		else if ("idElement".equals(name))
			resultObject = new IdElement(objType);
		else if ("lom".equals(name))
			resultObject = new Lom(objType);
	}



	public String getOperation() {
		return operation;
	}
	

	public void setOperation(String operation) {
		this.operation = operation;
	}
	

	public String getResultCode() {
		return resultCode;
	}
	

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	

	public String getResultDescription() {
		return resultDescription;
	}
	

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	

	public ObjectMerli getResultObject() {
		return resultObject;
	}
	

	public void setResultObject(ObjectMerli resultObject) {
		this.resultObject = resultObject;
	}



	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seResult=null;
		SOAPElement seOperation;
		SOAPElement seCode;
		SOAPElement seDescription;
		SOAPElement seObject;
	        
		//Crea l'element Result
		seResult = soapFactory.createElement(soapFactory.createName("result"));
		
		//Crea l'element operation
		if (operation == null) operation = "";
		seOperation = soapFactory.createElement(soapFactory.createName("operation"));
		seOperation.addTextNode(this.operation);
		//Afageix l'element operation al Result
		seResult.addChildElement(seOperation);

		//Crea l'element resultCode
		if (resultCode == null) resultCode = "";
		seCode = soapFactory.createElement(soapFactory.createName("resultCode"));
		seCode.addTextNode(this.resultCode);
		//Afageix l'element resultCode al Result
		seResult.addChildElement(seCode);

		//Crea l'element resultDescription
		if (resultDescription == null) resultDescription = "";
		seDescription = soapFactory.createElement(soapFactory.createName("resultDescription"));
		seDescription.addTextNode(this.resultDescription);
		//Afageix l'element resultDescription al Result
		seResult.addChildElement(seDescription);
		    
		//Crea l'element resultObject
		seObject = soapFactory.createElement(soapFactory.createName("resultObject"));
		seObject.addChildElement(getResultObject().toXml());
		
		seResult.addChildElement(seObject);
		
		return seResult;
	}
	
	
	
}
