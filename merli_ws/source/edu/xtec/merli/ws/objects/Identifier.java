package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;



public class Identifier extends ObjectMerli{

	private String catalog;
	private String entry;
	
	private int idLom;
	

	public Identifier() {
		super();
		// TODO Auto-generated constructor stub
		this.catalog = "CELEBRATE";
		this.entry = "MERLI";
		this.idLom=0;
	}
	

	public Identifier(String catalog, String entry) {
		super();
		// TODO Auto-generated constructor stub
		this.catalog = catalog;
		this.entry = entry;
		try{
		this.idLom = Integer.parseInt(entry.substring(5));
		}catch(Exception e){
			idLom=0;
		}
	}



	public Identifier(SOAPElement seIdentifier) throws SOAPException {
		// TODO Auto-generated constructor stub
		
		Iterator It=seIdentifier.getChildElements(soapFactory.createName("catalog"));
		if (It.hasNext())
			catalog= ((SOAPElement)It.next()).getValue();
		
		It=seIdentifier.getChildElements(soapFactory.createName("entry"));
		if (It.hasNext())
			entry= ((SOAPElement)It.next()).getValue();
	}




	public int getIdLom() {
		return idLom;
	}
	


	public void setIdLom(int idLom) {
		this.idLom = idLom;
	}
	


	public String getCatalog() {
		return catalog;
	}
	


	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	


	public String getEntry() {
		return entry;
	}
	


	public void setEntry(String entry) {
		this.entry = entry;
	}
	
	public String getIdEntry(){
		return entry.substring(entry.indexOf("MERLI")+5);
	}

	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seIdentifier=null;
		SOAPElement seCatalog;
		SOAPElement seEntry;
	    
		//Crea l'element identifier
		seIdentifier = soapFactory.createElement("identifier");
		
		//Crea l'element Catalog.
		if (catalog == null) catalog = "";
		seCatalog = soapFactory.createElement("catalog");
		seCatalog.addTextNode(this.catalog);
		//Afageix l'element Catalog a l'Identifier
		seIdentifier.addChildElement(seCatalog);

		//Crea l'element entry
		if (entry == null) entry = "";
		seEntry = soapFactory.createElement("entry");
		seEntry.addTextNode(this.entry);
		//Afageix l'element entry al Identifier
		seIdentifier.addChildElement(seEntry);

		return seIdentifier;
	}
	
	
	
}
