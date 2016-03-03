package edu.xtec.merli.agrega.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;



public class IdResource extends ObjectMerli{

	private String identifier;
	private String type;
	private boolean lomEs;
	

	public IdResource() {
		super();

		this.identifier = "";
		this.type = "";
	}

	public IdResource(String identifier, String type) {
		super();

		this.identifier = identifier;
		this.type = type;
	}

	public IdResource(String identifier, String type, boolean isLomEs) {
		super();

		this.identifier = identifier;
		this.type = type;
		this.setLomEs(isLomEs);
	}



	public IdResource(SOAPElement seIdResource) throws SOAPException {
		
		Iterator it = seIdResource.getChildElements(soapFactory.createName("identifier"));
		identifier = ((SOAPElement) it.next()).getValue();
		
		it = seIdResource.getChildElements(soapFactory.createName("type"));
		type = ((SOAPElement) it.next()).getValue();
		
		setLomEs("true".equals(seIdResource.getAttribute("lomEs")));
	}

	public String getIdentifier() {
		return identifier;
	}
	

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	

	public String getType() {
		return type;
	}
	

	public void setType(String type) {
		this.type = type;
	}
	

	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seIdResult=null;
		SOAPElement seIdentifier;
		SOAPElement seType;
	        
		//Crea l'element IdResult
		seIdResult = soapFactory.createElement(soapFactory.createName("idResource"));
		
		//Crea l'element identifier
		if (identifier == null) identifier = "";
		seIdentifier = soapFactory.createElement(soapFactory.createName("identifier"));
		seIdentifier.addTextNode(this.identifier);
		//Afageix l'element Identifier al IdResult
		seIdResult.addChildElement(seIdentifier);

		//Crea l'element type
		if (type == null) type = "";
		seType = soapFactory.createElement(soapFactory.createName("type"));
		seType.addTextNode(this.type);
		//Afageix l'element type al IdResult
		seIdResult.addChildElement(seType);

		if (isLomEs())
			seIdResult.addAttribute(soapFactory.createName("lomEs"), "true");
		
		return seIdResult;
	}
	
	public boolean isLomEs() {
		return lomEs;
	}

	public void setLomEs(boolean lomEs) {
		this.lomEs = lomEs;
	}

	public String toString(){
		return "IdResource[id="+getIdentifier()+",type="+getType()+"]";
	}
	
	
}
