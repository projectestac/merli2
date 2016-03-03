package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Unitat extends ObjectMerli{

	private Integer identifier;
	private String name;
	

	public Unitat() {
		super();
		this.identifier = new Integer(0);
		this.name = "";
	}

	public Unitat(Integer identifier, String name) {
		super();
		this.identifier = identifier;
		this.name = name;
	}



	public Unitat(SOAPElement seUnitat) throws SOAPException {

        Iterator It=seUnitat.getChildElements(soapFactory.createName("identifier"));
		if (It.hasNext())
			identifier= new Integer(((SOAPElement)It.next()).getValue());
		
		It = seUnitat.getChildElements(soapFactory.createName("name"));
		name = ((SOAPElement) It.next()).getValue();
	}


	public SOAPElement toXml() throws SOAPException {
		SOAPElement seUnitat=null;
		SOAPElement seIdentifier;
		SOAPElement seName;
	        
		//Crea l'element result
		seUnitat = soapFactory.createElement(soapFactory.createName("unitat"));
		
		//Crea l'element identifier i l'afegeix al result
		seIdentifier = soapFactory.createElement(soapFactory.createName("identifier"));
		seIdentifier.addTextNode(String.valueOf(this.identifier));
		seUnitat.addChildElement(seIdentifier);

		//Crea l'element name i l'afegeix al result
		if (name == null) name = "";
		seName = soapFactory.createElement(soapFactory.createName("name"));
		seName.addTextNode(this.name);
		seUnitat.addChildElement(seName);

		return seUnitat;
	}
	
	public String toString(){
		return "Unitat[id="+getIdentifier()+",type="+getName()+"]";
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toUrl(String prefix) {
		String params="";
		params+=prefix+"id="+String.valueOf(this.identifier)+"&";
		if (name == null) name = "";
		params+=prefix+"n="+this.name+"&";
		return params;
	}
	
	
}
