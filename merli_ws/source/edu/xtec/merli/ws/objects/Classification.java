package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Classification extends ObjectMerli {

	private SourceValue purpose;
	private TaxonPath taxonPath;
	private LangString description;
	
	
	
	public Classification() {
		super();
		// TODO Auto-generated constructor stub
		this.purpose = null;
		this.taxonPath = null;
		this.description = null;
	}
	public Classification(SourceValue purpose, TaxonPath taxonPath, LangString description) {
		super();
		// TODO Auto-generated constructor stub
		this.purpose = purpose;
		this.taxonPath = taxonPath;
		this.description = description;
	}




	public Classification(SOAPElement seClassification) throws SOAPException {
		
		Iterator it = seClassification.getChildElements(soapFactory.createName("purpose"));
		if (it.hasNext())
			purpose = new SourceValue((SOAPElement) it.next());
		
		it = seClassification.getChildElements(soapFactory.createName("taxonPath"));
		if (it.hasNext())
			taxonPath = new TaxonPath((SOAPElement) it.next());
		
		it = seClassification.getChildElements(soapFactory.createName("description"));
		if (it.hasNext())
			description = new LangString((SOAPElement) it.next());
	}
	
	
	
	public LangString getDescription() {
		return description;
	}
	



	public void setDescription(LangString description) {
		this.description = description;
	}
	



	public SourceValue getPurpose() {
		return purpose;
	}
	



	public void setPurpose(SourceValue purpose) {
		this.purpose = purpose;
	}
	



	public TaxonPath getTaxonPath() {
		return taxonPath;
	}
	



	public void setTaxonPath(TaxonPath taxonPath) {
		this.taxonPath = taxonPath;
	}
	



	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seClassification;
		SOAPElement seDescription;
		
		seClassification = soapFactory.createElement(soapFactory.createName("classification"));

		if (purpose == null) purpose = new SourceValue("purpose");
		purpose.setName("purpose");
		seClassification.addChildElement(purpose.toXml());				

		if (taxonPath == null) taxonPath = new TaxonPath();
		seClassification.addChildElement(taxonPath.toXml());

		if (description == null) description = new LangString();
		seDescription = soapFactory.createElement(soapFactory.createName("description"));
		seDescription.addChildElement(description.toXml());
		seClassification.addChildElement(seDescription);
		
		return seClassification;
	}

}
