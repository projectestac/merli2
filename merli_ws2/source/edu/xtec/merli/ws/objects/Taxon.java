package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Taxon extends ObjectMerli {

	private String id;
	private LangStringList entryList;
	
	
	

	public Taxon() {
		super();
		// TODO Auto-generated constructor stub
		this.id = "";
		this.entryList = new LangStringList();
	}

	public Taxon(String id, LangString entry) {
		super();
		this.id = id;
		setEntry(entry);
	}



	public Taxon(SOAPElement seTaxon) throws SOAPException {
		
		Iterator it = seTaxon.getChildElements(soapFactory.createName("id"));
		if (it.hasNext())
			id = ((SOAPElement) it.next()).getValue();
		
		it = seTaxon.getChildElements(soapFactory.createName("entry"));
		if (it.hasNext()){
			setEntry(new LangString((SOAPElement) it.next()));
		}
	}

	public LangStringList getEntryList() {
		return this.entryList;
	}
	public LangString getEntry() {
		return getEntry("ca");
	}
	public LangString getEntry(String sLang) {
		return getEntryList().getLangString(sLang);
	}
	
	public void setEntry(LangString entry) {
		if (this.entryList==null)this.entryList=new LangStringList();
		this.entryList.setLangString(entry);
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
		//Afegeix l'element id al taxon
		seTaxon.addChildElement(seId);
		
		//Afageix l'element entry al taxon
/*		if (entry == null) entry = new LangString();
		seEntry = soapFactory.createElement(soapFactory.createName("entry"));
		seEntry.addChildElement(this.entry.toXml());
		seTaxon.addChildElement(seEntry);
*/
		seTaxon.addChildElement(getEntryList().toXml("entry"));
	
		return seTaxon;
	}

	public String toUrl(String prefix) {
		String params="";
		if (id == null) id = "";
		params+=prefix+"id="+this.id+"&";
		params+=getEntryList().toUrl(prefix+"entry_");
		return params;
	}

}
