package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;



public class IdElement extends ObjectMerli{

	private String identifier;
	private String typeElement;
	private static final String CONTENT = "content";
	private static final String AREA = "area";
	private static final String LEVEL = "level";
	

	public IdElement() {
		super();
		// TODO Auto-generated constructor stub
		this.identifier = "";
		this.typeElement = "";
	}
	

	public IdElement(String identifier, String typeElement) {
		super();
		// TODO Auto-generated constructor stub
		this.identifier = identifier;
		this.typeElement = typeElement;
	}




	public IdElement(SOAPElement seIdElement) throws SOAPException {
		
		Iterator it = seIdElement.getChildElements(soapFactory.createName("identifier"));
		identifier = ((SOAPElement) it.next()).getValue();
		
		it = seIdElement.getChildElements(soapFactory.createName("typeElement"));
		setTypeElement(((SOAPElement) it.next()).getValue());
	}


	public String getIdentifier() {
		return identifier;
	}
	


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	


	public String getTypeElement() {
		return typeElement;
	}
	


	public void setTypeElement(String typeElement) {
		if (CONTENT.equals(typeElement) ||
				AREA.equals(typeElement) ||
				LEVEL.equals(typeElement))
			this.typeElement = typeElement;
		else
			this.typeElement = "";
	}
	


	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seIdElement=null;
		SOAPElement seIdentifier;
		SOAPElement seTypeElement;
	        
		//Crea l'element IdElement
		seIdElement = soapFactory.createElement("idElement");
		
		//Crea l'element identifier
		if (identifier == null) identifier = "";
		seIdentifier = soapFactory.createElement("identifier");
		seIdentifier.addTextNode(this.identifier);
		//Afageix l'element Identifier al idElement
		seIdElement.addChildElement(seIdentifier);

		//Crea l'element typeElement
		if (typeElement == null) typeElement = "";
		seTypeElement = soapFactory.createElement("typeElement");
		seTypeElement.addTextNode(this.typeElement);
		//Afageix l'element typeElement al IdElement
		seIdElement.addChildElement(seTypeElement);

		return seIdElement;
	}
	
	public String toString(){
		return "[id="+getIdentifier()+" type="+getTypeElement()+"]";
	}
	
	
	
}
