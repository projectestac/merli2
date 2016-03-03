package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class TaxonList extends ObjectMerli {
	
	private ArrayList taxonList;		
	

	public TaxonList(ArrayList taxonList) {
		super();
		// TODO Auto-generated constructor stub
		this.taxonList = taxonList;
	}

	public TaxonList(SOAPElement seTaxonPath) throws SOAPException {
		
		Iterator it = seTaxonPath.getChildElements(soapFactory.createName("taxon"));
		while (it.hasNext())
			taxonList.add(new Taxon((SOAPElement) it.next()));
		
	}
	
	
	public ArrayList getTaxonList() {
		return taxonList;
	}
	

	public void setTaxonList(ArrayList taxonList) {
		this.taxonList = taxonList;
	}
	

	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seTaxon;
		SOAPElement seTaxonList;
		
		//Crea l'element TaxonList
		seTaxonList = soapFactory.createElement("taxonList");
		
		
		//Crea els elements taxon
		if (taxonList == null) taxonList = new ArrayList();
		for(int i=0;i<taxonList.size();i++){
			seTaxon = soapFactory.createElement("taxon");
			seTaxon.addChildElement(((Taxon)this.taxonList.get(i)).toXml());
			//Afageix l'element taxon al TaxonList
			seTaxonList.addChildElement(seTaxon);
		}
		
		return seTaxonList;
	}

}
