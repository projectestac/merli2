package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Relacions extends ObjectMerli {

	private ArrayList relacionsList;
	
	public Relacions() {
		super();
		this.relacionsList = new ArrayList();
	}
	
	public ArrayList getRelacionsList() {
		return relacionsList;
	}
	public void setRelacionsList(ArrayList relacionsList) {
		this.relacionsList = relacionsList;
	}	

	public Relacions(SOAPElement seRelacions) throws SOAPException {
		Iterator It=seRelacions.getChildElements(soapFactory.createName("relacionsList"));
		relacionsList = new ArrayList();
		while (It.hasNext())
        	relacionsList.add(new Relacio((SOAPElement)It.next()));
	}

	public SOAPElement toXml() throws SOAPException {

		SOAPElement seRelacions=null;
	    
		//Crea l'element relacions
		seRelacions= soapFactory.createElement(soapFactory.createName("relation"));
		
		if (relacionsList == null) relacionsList = new ArrayList();
		for (int i=0; i < this.relacionsList.size();i++){			
			seRelacions.addChildElement(((Relacio)this.relacionsList.get(i)).toXml());
		}

		return seRelacions;
	}

}
