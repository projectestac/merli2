package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class TaxonPath extends ObjectMerli {

	private LangString source;
	private ArrayList taxonList;
	private LangString description;
	
	

	
	public TaxonPath() {
		super();
		// TODO Auto-generated constructor stub
		this.source = new LangString();
		this.taxonList = new ArrayList();
		this.description = new LangString();
	}
	
	public TaxonPath(LangString source, ArrayList taxonList, LangString description) {
		super();
		// TODO Auto-generated constructor stub
		this.source = source;
		this.taxonList = taxonList;
		this.description = description;
	}





	public TaxonPath(SOAPElement seTaxonPath) throws SOAPException {
		
		Iterator it = seTaxonPath.getChildElements(soapFactory.createName("source"));
		if (it.hasNext())
			source = new LangString((SOAPElement) it.next());
		
		taxonList = new ArrayList();
		it = seTaxonPath.getChildElements(soapFactory.createName("taxon"));
		while (it.hasNext())
			taxonList.add(new Taxon((SOAPElement) it.next()));
		
	}

	public LangString getDescription() {
		return description;
	}
	




	public void setDescription(LangString description) {
		this.description = description;
	}
	




	public LangString getSource() {
		return source;
	}
	




	public void setSource(LangString source) {
		this.source = source;
	}
	




	public ArrayList getTaxonList() {
		return taxonList;
	}
	




	public void setTaxonList(ArrayList taxonList) {
		this.taxonList = taxonList;
	}
	




	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seTaxonPath;
		SOAPElement seSource;
		SOAPElement seDescription;
		
		seTaxonPath = soapFactory.createElement(soapFactory.createName("taxonPath"));

		if (source == null) source = new LangString();
		seSource = soapFactory.createElement(soapFactory.createName("source"));
		seSource.addChildElement(source.toXml());
		seTaxonPath.addChildElement(seSource);

		if (taxonList == null) taxonList = new ArrayList();
		for(int i=0;i<taxonList.size();i++){
			seTaxonPath.addChildElement(((Taxon)taxonList.get(i)).toXml());
		}
		
		//seDescription = soapFactory.createElement("description");
		//seDescription.addChildElement(description.toXml());
		//seTaxonPath.addChildElement(seDescription);
		
		return seTaxonPath;
	}

}
