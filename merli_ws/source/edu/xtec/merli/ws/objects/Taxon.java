package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Taxon extends ObjectMerli {

	private String id;
	private LangString entry;
	
	
	

	public Taxon() {
		super();
		// TODO Auto-generated constructor stub
		this.id = "";
		this.entry = new LangString();
	}

	public Taxon(String id, LangString entry) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.entry = entry;
	}



	public Taxon(SOAPElement seTaxon) throws SOAPException {
		
		Iterator it = seTaxon.getChildElements(soapFactory.createName("id"));
		if (it.hasNext())
			id = ((SOAPElement) it.next()).getValue();
		
		it = seTaxon.getChildElements(soapFactory.createName("entry"));
		if (it.hasNext())
			entry = new LangString((SOAPElement) it.next());
	}

	public LangString getEntry() {
		return entry;
	}
	


	public void setEntry(LangString entry) {
		this.entry = entry;
	}
	


	public String getId() {
		return id;
	}
	


	public void setId(String id) {
		this.id = id;
	}
	


	public SOAPElement toXml() throws SOAPException {
		SOAPElement seTaxon;
		SOAPElement seId;
		SOAPElement seEntry;
        
		//Crea l'element taxon
		seTaxon = soapFactory.createElement(soapFactory.createName("taxon"));

		//Crea l'element id
		if (id == null) id = "";
		seId = soapFactory.createElement(soapFactory.createName("id"));
		seId.addTextNode(this.id);
		//Afageix l'element id al taxon
		seTaxon.addChildElement(seId);
		
		//Afageix l'element entry al taxon
		if (entry == null) entry = new LangString();
		seEntry = soapFactory.createElement(soapFactory.createName("entry"));
		seEntry.addChildElement(this.entry.toXml());
		seTaxon.addChildElement(seEntry);
	
		return seTaxon;
	}

}
