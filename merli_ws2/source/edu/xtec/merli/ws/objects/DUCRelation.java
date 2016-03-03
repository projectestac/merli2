package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class DUCRelation extends ObjectMerli {

	private String DUCRelationType;
	private ElementDUC elementDUC;
	
	

	public DUCRelation() {
		super();
		// TODO Auto-generated constructor stub
		this.DUCRelationType = "";
		this.elementDUC = null;
	}
	public DUCRelation(String DUCRelationType, ElementDUC elementDUC) {
		super();
		// TODO Auto-generated constructor stub
		this.DUCRelationType = DUCRelationType;
		this.elementDUC = elementDUC;
	}




	public DUCRelation(SOAPElement seDUCRelation) throws SOAPException {
		
		Iterator it = seDUCRelation.getChildElements(soapFactory.createName("DUCRelationType"));
		DUCRelationType = ((SOAPElement) it.next()).getValue();
		
		it = seDUCRelation.getChildElements(soapFactory.createName("elementDUC"));
		elementDUC = new ElementDUC((SOAPElement) it.next());
	}
	
	
	
	public String getDUCRelationType() {
		return DUCRelationType;
	}
	



	public void setDUCRelationType(String relationType) {
		DUCRelationType = relationType;
	}
	



	public ElementDUC getElementDUC() {
		return elementDUC;
	}
	



	public void setElementDUC(ElementDUC elementDUC) {
		this.elementDUC = elementDUC;
	}
	



	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seDUCRelation;
		SOAPElement seDUCRelationType;
		
		//Crea l'element DUCRelation
		seDUCRelation = soapFactory.createElement("DUCRelation");
		
		//Crea l'element DUCRelationType
		if (DUCRelationType == null) DUCRelationType = "";
		seDUCRelationType = soapFactory.createElement("DUCRelationType");
		seDUCRelationType.addTextNode(this.DUCRelationType);
		//afageix l'element DUCRelationType al DUCRelation
		seDUCRelation.addChildElement(seDUCRelationType);
		
		//Afageix l'element elementDUC al DUCRelation
		if (elementDUC == null) elementDUC = new ElementDUC();
		seDUCRelation.addChildElement(this.elementDUC.toXml());
		
		return seDUCRelation;		
	}

}
